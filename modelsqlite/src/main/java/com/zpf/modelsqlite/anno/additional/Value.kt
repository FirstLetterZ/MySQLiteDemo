package com.zpf.modelsqlite.anno.additional

import com.zpf.modelsqlite.constant.ColumnEnum
import com.zpf.modelsqlite.constant.SQLiteRelation

/**
 * 数据库操作，赋值，仅在执行 @UPDATE 时生效
 * Created by ZPF on 2020/3/6.
 */
@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class Value(
        val column: ColumnEnum,
        val ignoreOnNull: Boolean = false
)