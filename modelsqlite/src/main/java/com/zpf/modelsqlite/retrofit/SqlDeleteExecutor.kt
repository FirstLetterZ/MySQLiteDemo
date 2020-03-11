package com.zpf.modelsqlite.retrofit

import com.zpf.modelsqlite.interfaces.ISqlDao
import com.zpf.modelsqlite.SQLiteInfo
import com.zpf.modelsqlite.SqlColumnInfo
import com.zpf.modelsqlite.anno.operation.DELETE
import java.lang.reflect.Type

class SqlDeleteExecutor(builder: SqlExecutor.Builder) : SqlExecutor {
    val tableName = builder.tableName
    val transaction = builder.transaction
    private val whereList = builder.whereArgs

    override fun execute(sqlDao: ISqlDao, args: Array<Any?>?): Boolean {
        val sqlInfo = SQLiteInfo(tableName)
        var index: Int
        var value: Any?
        var tempColumn: SqlColumnInfo
        whereList?.forEachIndexed { i, item ->
            index = (item.columnValue as? Int) ?: -1
            if (index >= 0) {
                value = args?.getOrNull(index)
                tempColumn = item.copy(columnValue = value)
                sqlInfo.queryInfoList.add(tempColumn)
            }
        }
        sqlInfo.transaction = transaction
        return sqlDao.delete(sqlInfo)
    }

    override fun methodType(): Type = DELETE::class.java

}