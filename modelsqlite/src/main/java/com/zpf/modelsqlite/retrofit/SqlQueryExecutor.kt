package com.zpf.modelsqlite.retrofit

import com.zpf.modelsqlite.*
import com.zpf.modelsqlite.anno.operation.QUERY
import com.zpf.modelsqlite.utils.ResultConvert
import java.lang.reflect.Type

class SqlQueryExecutor(builder: SqlExecutor.Builder, private val type: Type,
                       private val queryResultConvert: ResultConvert<Any>) : SqlExecutor {
    val transaction = builder.transaction
    val tableName = builder.tableName
    private val whereList: List<SqlColumnInfo>? = builder.whereArgs
    private val orderInfo: SqlOrderInfo? = builder.orderInfo
    private val groupInfo: SqlGroupInfo? = builder.groupInfo
    private val limitInfo: SqlLimitInfo? = builder.limitInfo
    private val pageInfoIndex = builder.pageInfoIndex

    override fun execute(sqlDao: ISqlDao, args: Array<Any?>?): Any? {
        val sqlInfo = SQLiteInfo(tableName)
        var index: Int
        var value: Any?
        var tempColumn: SqlColumnInfo
        whereList?.map { item ->
            index = (item.columnValue as? Int) ?: -1
            if (index >= 0) {
                value = args?.getOrNull(index)
                tempColumn = item.copy(columnValue = value)
                sqlInfo.queryInfoList.add(tempColumn)
            }
        }
        groupInfo?.let {
            sqlInfo.groupInfo = SqlGroupInfo()
            it.columnArray.map { item ->
                sqlInfo.groupInfo?.columnArray?.add(item)
            }
            val havingList = ArrayList<SqlColumnInfo>()
            it.having?.map { item ->
                havingList.add(item.copy())
            }
            if (havingList.isNotEmpty()) {
                sqlInfo.groupInfo?.having = havingList
            }
        }
        orderInfo?.let {
            sqlInfo.orderInfo = SqlOrderInfo()
            it.columnArray.map { item ->
                sqlInfo.orderInfo?.columnArray?.add(item)
            }
            sqlInfo.orderInfo?.asc = it.asc
        }
        if (limitInfo != null) {
            val pageInfo = limitInfo.copy()
            pageInfo.pageNumber = 1
            if (pageInfoIndex >= 0) {
                value = args?.getOrNull(pageInfoIndex)
                if (value is Number) {
                    pageInfo.pageNumber = (value as Number).toInt()
                }
            }
            sqlInfo.limitInfo = pageInfo
        }
        sqlInfo.transaction = transaction
        return queryResultConvert.convert(sqlDao.queryArray(type, sqlInfo),type)
    }

    override fun methodType(): Type = QUERY::class.java
}