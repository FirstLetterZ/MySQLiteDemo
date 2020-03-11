package com.zpf.modelsqlite.anno.additional

import com.zpf.modelsqlite.constant.ColumnEnum
import com.zpf.modelsqlite.constant.SQLiteRelation

/**
 * 数据库操作限制，分组，仅在执行 @QUERY 时生效
 * Created by ZPF on 2020/3/6.
 */
@Target(AnnotationTarget.VALUE_PARAMETER)
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
annotation class Havinng(
        val column: ColumnEnum,
        val relation: SQLiteRelation = SQLiteRelation.RELATION_EQUALITY,
        val sqlFunc: String = "",
        val useAndConnector: Boolean = true,
        val ignoreOnNull: Boolean = true
)