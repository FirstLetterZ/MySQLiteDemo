package com.zpf.modelsqlite

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.text.TextUtils
import com.zpf.modelsqlite.anno.SQLiteClassify
import com.zpf.modelsqlite.anno.SQLiteColumn
import com.zpf.modelsqlite.anno.SQLiteRelevance
import com.zpf.modelsqlite.constant.ColumnEnum
import com.zpf.modelsqlite.constant.SQLiteConfig
import com.zpf.modelsqlite.interfaces.ISqlDao
import com.zpf.modelsqlite.utils.*
import java.lang.reflect.Type
import kotlin.collections.ArrayList

/**
 * 数据库操作
 * Created by ZPF on 2018/1/29.
 */
class CacheDao : ISqlDao {
    private var mSQLiteDatabase: SQLiteDatabase? = null
    private var mBuilderLength = 128
    private val mSQLiteOpenHelper: SQLiteOpenHelper

    constructor(dbPath: String?) {
        val path = if (dbPath == null || dbPath.isEmpty()) {
            SQLiteConfig.DB_USER_CACHE
        } else if (dbPath.endsWith("/")) {
            dbPath + SQLiteConfig.DB_USER_CACHE
        } else {
            dbPath + "/" + SQLiteConfig.DB_USER_CACHE
        }
        mSQLiteOpenHelper = CacheSQLiteHelper(SQLiteConfig.getAppContext(), path)
    }

    constructor(openHelper: SQLiteOpenHelper) {
        mSQLiteOpenHelper = openHelper
    }

    override fun isDbOpen() = mSQLiteDatabase?.isOpen == true

    override fun closeDB() {
        if (mSQLiteDatabase?.isOpen == true) {
            mSQLiteDatabase?.close()
        }
    }

    /**
     * 查询数据库中的总条数.
     */
    override fun totalCount(): Long {
        val sql = "select count(" + SQLiteConfig.PRIMARY_KEY + ") from " + SQLiteConfig.TB_CACHE
        val cursor = getDatabase().rawQuery(sql, null)
        cursor.moveToFirst()
        val count = cursor.getLong(0)
        cursor.close()
        return count
    }

    override fun saveValue(value: Any): Int {
        return saveValue(value, null)
    }

    override fun saveValue(value: Any, whereClause: SQLiteInfo?): Int {
        var result = -1
        if (whereClause == null) {
            var insertInfo: SQLiteInfo? = null
            val classify = value.javaClass.getAnnotation(SQLiteClassify::class.java)
            if (classify != null) {
                val firstKeyWord: String = classify.tableName
                if (!TextUtils.isEmpty(firstKeyWord)) {
                    insertInfo = SQLiteInfo(firstKeyWord)
                }
            }
            if (insertInfo == null) {
                Logger.e("未找到数据对应的表格信息：" + value.javaClass.name)
            } else {
                initChangeValueList(value, insertInfo.changeValueList)
                insertValue(insertInfo)
                result = 0
            }
        } else {
            initChangeValueList(value, whereClause.changeValueList)
            val cursor = queryCursor(whereClause)
            result = if (cursor.moveToFirst()) {
                updateValue(whereClause)
                1
            } else {
                insertValue(whereClause)
                0
            }
        }
        return result
    }

    /**
     * 单条更新
     */
    override fun updateValue(info: SQLiteInfo): Boolean {
        val valuesSize = info.changeValueList.size
        if (valuesSize == 0) {
            return false
        }
        val sql = StringBuilder(mBuilderLength)
        sql.append(SQLiteConfig.SQL_UPDATE)
        FormatUtil.addUpdateString(sql, info.changeValueList)
        sql.append(SQLiteConfig.WHERE)
        FormatUtil.addConditionString(sql, info.queryInfoList, info.tableName)
        return execSqlString(sql.toString(), info.transaction)
    }

    /**
     * 单条插入
     */
    override fun insertValue(info: SQLiteInfo): Boolean {
        val valuesSize = info.changeValueList.size
        if (valuesSize == 0) {
            return false
        }
        val sql = StringBuilder(mBuilderLength)
        sql.append(SQLiteConfig.SQL_INSERT)
        FormatUtil.addInsertString(sql, info.changeValueList, info.tableName)
        return execSqlString(sql.toString(), info.transaction)
    }


    /**
     * 保存多条
     */
    override fun saveByArray(valueArray: Map<Any, SQLiteInfo?>): Boolean {
        if (valueArray.isNotEmpty()) {
            getDatabase().beginTransaction()
            try {
                for ((key, value) in valueArray) {
                    saveValue(key, value)
                }
                getDatabase().setTransactionSuccessful()
                return true
            } catch (e: Exception) {
                Logger.e(e.toString())
                return false
            } finally {
                getDatabase().endTransaction()
            }
        }
        return false
    }

    /**
     * 删除单条
     */
    override fun delete(info: SQLiteInfo): Boolean {
        val sql = StringBuilder(mBuilderLength)
        sql.append(SQLiteConfig.SQL_DELETE)
        FormatUtil.addConditionString(sql, info.queryInfoList, info.tableName)
        return execSqlString(sql.toString(), info.transaction)
    }

    /**
     * 删除多条
     */
    override fun deleteByArray(infoList: List<SQLiteInfo>): Boolean {
        val sql = StringBuilder(mBuilderLength)
        val sqlArray = infoList.map {
            if (sql.isNotEmpty()) {
                sql.delete(0, sql.length - 1)
            }
            sql.append(SQLiteConfig.SQL_DELETE)
            FormatUtil.addConditionString(sql, it.queryInfoList, it.tableName)
            sql.toString()
        }
        return execSqlArray(sqlArray)
    }

    /**
     * 获得查找光标
     */
    override fun queryCursor(info: SQLiteInfo): Cursor {
        val sql = StringBuilder(SQLiteConfig.SQL_SELECT)
        FormatUtil.addConditionString(sql, info.queryInfoList, info.tableName)
        info.groupInfo?.let {
            FormatUtil.addGroupString(sql, it.columnArray, it.having)
        }
        info.orderInfo?.let {
            FormatUtil.addOrderString(sql, it.columnArray, it.asc)
        }
        info.limitInfo?.let {
            if (it.pageSize > 0 && it.pageNumber > 0) {
                FormatUtil.addLimitString(sql, it.offset, it.pageSize, it.pageNumber)
            }
        }
        val sqkString = sql.toString()
        Logger.i(sqkString)
        return getDatabase().rawQuery(sqkString, null)
    }

    /**
     * 查询某列值，仅返回基本类型格式数据
     */
    override fun getColumnValue(column: ColumnEnum, info: SQLiteInfo): Any? {
        val cursor = queryCursor(info)
        val result: Any?
        result = if (cursor.moveToFirst()) {
            getValueByCursor(cursor, column)
        } else {
            Utils.getDefaultValue(column)
        }
        cursor.close()
        return result
    }


    /**
     * 单条查询第一条符合条件的结果
     */
    override fun <T> queryFirst(type: Type, info: SQLiteInfo): T? {
        val value: T? = SqlCreatorImpl.get().newInstance(type)
        if (value != null) {
            val cursor = queryCursor(info)
            if (cursor.moveToFirst()) {
                putValueToReceiver(value, cursor)
            }
            cursor.close()
        }
        return value
    }

    /**
     * 多条查询，返回list
     */
    override fun <T> queryArray(type: Type, info: SQLiteInfo): List<T> {
        val list = ArrayList<T>()
        val cursor = queryCursor(info)
        while (cursor.moveToNext()) {
            val listValue: T? = SqlCreatorImpl.get().newInstance(type)
            if (listValue == null) {
                break
            } else {
                putValueToReceiver(listValue, cursor)
                list.add(listValue)
            }
        }
        return list
    }

    /**
     * 将model值转转为赋值条件
     */
    private fun initChangeValueList(model: Any, changeValueList: ArrayList<SqlColumnInfo>) {
        for (field in Utils.getAllFields(model.javaClass)) {
            val note = field.getAnnotation(SQLiteColumn::class.java)
            if (note != null) {
                field.isAccessible = true
                try {
                    val value = field[model]
                    changeValueList.add(SqlColumnInfo(note.column, value))
                } catch (e: IllegalAccessException) {
                    Logger.e("fail on step initChangeValueList : ${e.message}")
                } finally {
                    field.isAccessible = false
                }
            } else {
                val relevance = field.getAnnotation(SQLiteRelevance::class.java)
                if (relevance != null) {
                    val clz = field.type
                    val classify = clz.getAnnotation(SQLiteClassify::class.java)
                    if (classify != null) {
                        //查找对应列的值
                        for (f in Utils.getAllFields(field.type)) {
                            if (relevance.targetColumn == f.getAnnotation(SQLiteColumn::class.java)?.column) {
                                field.isAccessible = true
                                try {
                                    val obj = field[model]
                                    if (obj != null) {
                                        f.isAccessible = true
                                        val value = f[obj]
                                        changeValueList.add(SqlColumnInfo(relevance.saveColumn, value))
                                        val sqLiteInfo = SQLiteInfo(classify.tableName)
                                        sqLiteInfo.queryInfoList.add(SqlColumnInfo(relevance.targetColumn, value))
                                        saveValue(obj, sqLiteInfo)
                                    }
                                } catch (e: IllegalAccessException) {
                                    Logger.e("fail on step initChangeValueList : init @SQLiteRelevance fail & ${e.message}")
                                } finally {
                                    f.isAccessible = false
                                    field.isAccessible = false
                                }
                                break
                            }

                        }
                    } else {
                        Logger.e("fail on step initChangeValueList : @SQLiteRelevance class lack @SQLiteClassify")
                    }
                }
            }
        }
    }

    /**
     * 对receiver赋值
     */
    private fun putValueToReceiver(receiver: Any, cursor: Cursor) {
        for (field in Utils.getAllFields(receiver.javaClass)) {
            val note = field.getAnnotation(SQLiteColumn::class.java)
            if (note != null) {
                try {
                    field.isAccessible = true
                    val value = getValueByCursor(cursor, note.column)
                    if (value != null && value is String
                            && field.type != String::class.java) {
                        val newValue: Any? = SqlJsonUtilImpl.get().fromJson(value.toString(), field.type)
                        field[receiver] = newValue
                    } else {
                        field[receiver] = value
                    }
                } catch (e: Exception) {
                    Logger.e("putValueToReceiver SQLiteColumn!=null：$e")
                } finally {
                    field.isAccessible = false
                }
            } else {
                val relevance = field.getAnnotation(SQLiteRelevance::class.java)
                if (relevance != null) {
                    val classify = field.type.getAnnotation(SQLiteClassify::class.java)
                    if (classify != null) {
                        val value = getValueByCursor(cursor, relevance.saveColumn)
                        try {
                            val sqlInfo = SQLiteInfo(classify.tableName)
                            sqlInfo.queryInfoList.add(SqlColumnInfo(relevance.targetColumn, value))
                            val relevanceObj: Any? = queryFirst(field.type, sqlInfo)
                            field.isAccessible = true
                            field[receiver] = relevanceObj
                        } catch (e: Exception) {
                            Logger.e("putValueToReceiver SQLiteRelevance!=null：$e")
                        } finally {
                            field.isAccessible = false
                        }
                    }
                }
            }
        }
    }


    /**
     * 根据光标获取对应值
     */
    private fun getValueByCursor(cursor: Cursor, column: ColumnEnum): Any? {
        val index = cursor.getColumnIndex(SQLiteConfig.COLUMN_NAME + column.value)
        val result: Any?
        result = if (index >= 0) {
            when (Utils.getColumnType(column.value)) {
                SQLiteConfig.TYPE_STRING -> cursor.getString(index)
                SQLiteConfig.TYPE_INT -> cursor.getInt(index)
                SQLiteConfig.TYPE_BOOLEAN -> cursor.getInt(index) == 1
                SQLiteConfig.TYPE_FLOAT -> cursor.getFloat(index)
                SQLiteConfig.TYPE_LONG -> cursor.getLong(index)
                SQLiteConfig.TYPE_SHORT -> cursor.getShort(index)
                SQLiteConfig.TYPE_DOUBLE -> cursor.getDouble(index)
                else -> cursor.getString(index)
            }
        } else {
            Utils.getDefaultValue(column)
        }
        return result
    }

    private fun getDatabase(): SQLiteDatabase {
        if (mSQLiteDatabase?.isOpen != true) {
            mSQLiteDatabase = mSQLiteOpenHelper.writableDatabase
        }
        return mSQLiteDatabase!!
    }

    fun execSqlArray(sqlStringArray: List<String>): Boolean {
        return execSqlArray(sqlStringArray, true)
    }

    override fun execSqlArray(sqlStringArray: List<String>, openTransaction: Boolean): Boolean {
        return getDatabase().let {
            if (openTransaction) {
                it.beginTransaction()
            }
            try {
                sqlStringArray.map { sql ->
                    Logger.i("execSqlArray->>$sql")
                    it.execSQL(sql)
                }
                if (openTransaction) {
                    it.setTransactionSuccessful()
                    it.endTransaction()
                }
                true
            } catch (e: Exception) {
                Logger.e("execSQL fail : ${e.message}")
                if (openTransaction) {
                    it.endTransaction()
                }
                false
            }
        }
    }

    override fun execSqlString(sqlString: String, openTransaction: Boolean): Boolean {
        Logger.i("execSqlString->$sqlString")
        return getDatabase().let {
            if (openTransaction) {
                it.beginTransaction()
            }
            try {
                it.execSQL(sqlString)
                if (openTransaction) {
                    it.setTransactionSuccessful()
                    it.endTransaction()
                }
                true
            } catch (e: Exception) {
                Logger.e("execSQL fail : ${e.message}")
                if (openTransaction) {
                    it.endTransaction()
                }
                false
            }
        }

    }

}