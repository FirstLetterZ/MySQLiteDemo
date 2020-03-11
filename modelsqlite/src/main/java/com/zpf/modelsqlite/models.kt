package com.zpf.modelsqlite

import com.zpf.modelsqlite.constant.ColumnEnum
import com.zpf.modelsqlite.constant.SQLiteRelation
import java.util.ArrayList

/**
 * 数据库查询辅助信息（列信息）
 * Created by ZPF on 2020/3/6.
 */
data class SqlColumnInfo(
        val columnName: ColumnEnum,
        var columnValue: Any?

) {
    var relation: SQLiteRelation = SQLiteRelation.RELATION_EQUALITY
    var useAndConnector: Boolean = true
    var funcName: String? = null
    var ignoreOnNull: Boolean = true
}

/**
 * 数据库查询辅助信息（分组信息）
 * Created by ZPF on 2020/3/6.
 */
data class SqlGroupInfo(
        val columnArray: ArrayList<ColumnEnum> = ArrayList(),
        var having: ArrayList<SqlColumnInfo>? = null
)

/**
 * 数据库查询辅助信息（排序信息）
 * Created by ZPF on 2020/3/6.
 */
data class SqlOrderInfo(
        val columnArray: ArrayList<ColumnEnum> = ArrayList(),
        var asc: Boolean = true
)

/**
 * 数据库查询辅助信息（数量信息）
 * Created by ZPF on 2020/3/6.
 */
data class SqlLimitInfo(
        var pageSize: Int = 20,
        var pageNumber: Int = 1,
        var offset: Int = 0
)

/**
 * 数据库查询辅助信息
 * Created by ZPF on 2018/1/24.
 */
data class SQLiteInfo(val tableName: String) {
    var limitInfo: SqlLimitInfo? = null
    var orderInfo: SqlOrderInfo? = null
    var groupInfo: SqlGroupInfo? = null
    val queryInfoList: ArrayList<SqlColumnInfo> = ArrayList()
    val changeValueList: ArrayList<SqlColumnInfo> = ArrayList()
    var transaction: Boolean = false

    fun addQueryCondition(column: ColumnEnum, value: Any?): SQLiteInfo {
        queryInfoList.add(SqlColumnInfo(column, value))
        return this
    }
}