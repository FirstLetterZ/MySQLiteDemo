package com.zpf.modelsqlite.retrofit

import com.zpf.modelsqlite.interfaces.ISqlDao
import com.zpf.modelsqlite.SQLiteInfo
import com.zpf.modelsqlite.SqlColumnInfo
import com.zpf.modelsqlite.anno.operation.UPDATE
import java.lang.reflect.Type

class SqlUpdateFactory(builder: SqlExecutor.Builder) : SqlExecutor {
    val tableName = builder.tableName
    val transaction = builder.transaction
    private val whereList = builder.whereArgs
    private val valueMap = builder.valueMap

    override fun execute(sqlDao: ISqlDao, args: Array<Any?>?): Boolean {
        if (valueMap.isNullOrEmpty()) {
            return false
        }
        val sqlInfo = SQLiteInfo(tableName)
        var index: Int
        var value: Any?
        var tempColumn: SqlColumnInfo
        valueMap.forEach {
            index = (it.columnValue as? Int) ?: -1
            if (index >= 0) {
                value = args?.getOrNull(index)
                if (value != null || !it.ignoreOnNull) {
                    tempColumn = it.copy(columnValue = value)
                    sqlInfo.changeValueList.add(tempColumn)
                }
            }
        }
        if (sqlInfo.changeValueList.isEmpty()) {
            return false
        }
        whereList?.forEach { item ->
            index = (item.columnValue as? Int) ?: -1
            if (index >= 0) {
                value = args?.getOrNull(index)
                tempColumn = item.copy(columnValue = value)
                sqlInfo.queryInfoList.add(tempColumn)
            }
        }
        sqlInfo.transaction = transaction
        return sqlDao.updateValue(sqlInfo)
    }

    override fun methodType(): Type = UPDATE::class.java

}