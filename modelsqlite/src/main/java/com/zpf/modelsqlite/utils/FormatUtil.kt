package com.zpf.modelsqlite.utils

import com.zpf.modelsqlite.SqlColumnInfo
import com.zpf.modelsqlite.constant.ColumnEnum
import com.zpf.modelsqlite.constant.SQLiteConfig
import com.zpf.modelsqlite.constant.SQLiteRelation
import java.lang.reflect.Type

object FormatUtil {

    fun addConditionPart(builder: StringBuilder, columnInfo: SqlColumnInfo, useConnector: Boolean): Boolean {
        if (columnInfo.ignoreOnNull && columnInfo.columnValue == null) {
            return false
        }
        if (useConnector) {
            if (columnInfo.useAndConnector) {
                builder.append(SQLiteConfig.AND)
            } else {
                builder.append(SQLiteConfig.OR)
            }
        }
        val columnValue = if (columnInfo.relation == SQLiteRelation.RELATION_NOT_NULL) {
            ""
        } else if (columnInfo.relation == SQLiteRelation.RELATION_IS_NULL) {
            ""
        } else {
            formatString(columnInfo.columnValue)
        }
        builder.append(SQLiteConfig.COLUMN_NAME + columnInfo.columnName.value + columnInfo.relation.value + columnValue)
        return true
    }

    fun addConditionString(builder: StringBuilder, list: List<SqlColumnInfo>?, table: String) {
        var skipFirst = false
        if (builder.endsWith(SQLiteConfig.WHERE)) {
            builder.append(formatString(table))
        } else if (builder.endsWith(SQLiteConfig.HAVING)) {
            skipFirst = true
        } else if (!builder.endsWith("$table'")) {
            builder.append(SQLiteConfig.WHERE)
                    .append(formatString(table))
        }
        list?.map {
            skipFirst = !addConditionPart(builder, it, !skipFirst)
        }
    }

    fun addGroupString(builder: StringBuilder, groupColumns: List<ColumnEnum>?, havingList: List<SqlColumnInfo>?) {
        if (!groupColumns.isNullOrEmpty()) {
            builder.append(SQLiteConfig.GROUP)
            groupColumns.forEachIndexed { i, item ->
                if (i > 0) {
                    builder.append(SQLiteConfig.COMMA)
                }
                builder.append(SQLiteConfig.COLUMN_NAME)
                        .append(item.value)

            }
            if (!havingList.isNullOrEmpty()) {
                builder.append(SQLiteConfig.HAVING)
                addConditionString(builder, havingList, "")
            }
        }
    }

    fun addOrderString(builder: StringBuilder, orderColumns: List<ColumnEnum>?, asc: Boolean) {
        if (!orderColumns.isNullOrEmpty()) {
            builder.append(SQLiteConfig.ORDER)
            orderColumns.forEachIndexed { i, item ->
                if (i > 0) {
                    builder.append(SQLiteConfig.COMMA)
                }
                builder.append(SQLiteConfig.COLUMN_NAME)
                        .append(item.value)
            }
        }
        if (asc) {
            builder.append(SQLiteConfig.ASC)
        }
    }

    fun addLimitString(builder: StringBuilder, offset: Int, pageSize: Int, pageNum: Int) {
        if (offset < 0 || pageSize < 0 || pageNum < 1) {
            Logger.e("Invalid arguments: offset=$offset,pageSize=$pageSize,pageNum=$pageNum")
        } else {
            val endIndex = offset + pageNum * pageSize
            builder.append(SQLiteConfig.LIMIT + (endIndex - pageNum) + SQLiteConfig.COMMA + endIndex)
        }
    }


    fun addUpdateString(builder: StringBuilder, valueList: List<SqlColumnInfo>?) {
        if (!valueList.isNullOrEmpty()) {
            valueList.forEachIndexed { i, item ->
                if (i > 0) {
                    builder.append(SQLiteConfig.COMMA)
                }
                builder.append(SQLiteConfig.COLUMN_NAME)
                        .append(item.columnName.value)
                        .append(SQLiteRelation.RELATION_EQUALITY.value)
                        .append(formatString(item.columnValue))
            }
            builder.append(SQLiteConfig.COMMA)
                    .append(SQLiteConfig.TIME_UPDATE)
                    .append(SQLiteRelation.RELATION_EQUALITY.value)
                    .append(System.currentTimeMillis())
        }
    }

    fun addInsertString(builder: StringBuilder, valueList: List<SqlColumnInfo>?, table: String) {
        if (!valueList.isNullOrEmpty()) {
            if (!builder.endsWith(SQLiteConfig.FIRST_KEY_WORD)) {
                builder.append(SQLiteConfig.FIRST_KEY_WORD)
            }
            val bindArgs = StringBuilder(SQLiteConfig.VALUES)
            bindArgs.append(formatString(table))
            valueList.map {
                builder.append(SQLiteConfig.COMMA)
                        .append(SQLiteConfig.COLUMN_NAME)
                        .append(it.columnName.value)
                bindArgs.append(SQLiteConfig.COMMA)
                        .append(formatString(it.columnValue))

            }
            builder.append(SQLiteConfig.COMMA)
                    .append(SQLiteConfig.TIME_CREATE)
                    .append(SQLiteConfig.COMMA)
                    .append(SQLiteConfig.TIME_UPDATE)
            bindArgs.append(SQLiteConfig.COMMA)
                    .append(System.currentTimeMillis())
                    .append(SQLiteConfig.COMMA)
                    .append(System.currentTimeMillis())
            builder.append(bindArgs)
                    .append(')')
        }
    }

    internal fun formatString(any: Any?): String {
        if (any == null) {
            return ""
        } else if (any is Number) {
            return any.toString()
        } else {
            return "'" + SqlUtil.defJsonUtil.toJsonString(any) + "'"
        }

    }

    internal fun <T> fromJson(json: String?, type: Type): T? = SqlUtil.defJsonUtil.fromJson(json, type)

}