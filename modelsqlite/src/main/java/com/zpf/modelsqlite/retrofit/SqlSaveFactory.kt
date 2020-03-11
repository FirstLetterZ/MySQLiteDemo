package com.zpf.modelsqlite.retrofit

import com.zpf.modelsqlite.ISqlDao
import com.zpf.modelsqlite.SQLiteInfo
import com.zpf.modelsqlite.SqlColumnInfo
import com.zpf.modelsqlite.anno.operation.SAVE
import java.lang.IllegalArgumentException
import java.lang.reflect.Type

class SqlSaveFactory(builder: SqlExecutor.Builder) : SqlExecutor {
    val tableName = builder.tableName
    val transaction = builder.transaction
    private val whereList = builder.whereArgs
    private val bodyIndex = builder.bodyIndex
    private val valueMap = builder.valueMap

    override fun execute(sqlDao: ISqlDao, args: Array<Any?>?): Int {
        if (bodyIndex < 0 && valueMap.isNullOrEmpty()) {
            throw IllegalArgumentException("Unable to find data to save!")
        }
        val saveValue = args?.getOrNull(bodyIndex)
        var index: Int
        var value: Any?
        var tempColumn: SqlColumnInfo
        if (saveValue != null) {
            val info = if (whereList.isNullOrEmpty()) {
                null
            } else {
                val temp = SQLiteInfo(tableName)
                whereList.forEachIndexed { i, item ->
                    index = (item.columnValue as? Int) ?: -1
                    if (index >= 0) {
                        value = args.getOrNull(index)
                        tempColumn = item.copy(columnValue = value)
                        temp.queryInfoList.add(tempColumn)
                    }
                }
                temp.queryInfoList.addAll(whereList)
                temp
            }
            return sqlDao.saveValue(saveValue, info)
        } else {
            val sqlInfo = SQLiteInfo(tableName)
            valueMap?.map {
                index = it.value
                if (index >= 0) {
                    value = args?.getOrNull(index)
                    tempColumn = SqlColumnInfo(it.key, value)
                    sqlInfo.changeValueList.add(tempColumn)
                }
            }
            whereList?.forEachIndexed { i, item ->
                index = (item.columnValue as? Int) ?: -1
                if (index >= 0) {
                    value = args?.getOrNull(index)
                    tempColumn = item.copy(columnValue = value)
                    sqlInfo.queryInfoList.add(tempColumn)
                }
            }
            sqlInfo.transaction = transaction
            return if (sqlInfo.queryInfoList.isEmpty()) {
                if (sqlDao.insertValue(sqlInfo)) {
                    0
                } else {
                    -1
                }
            } else {
                if (sqlDao.updateValue(sqlInfo)) {
                    1
                } else {
                    -1
                }
            }
        }
    }

    override fun methodType(): Type = SAVE::class.java
}