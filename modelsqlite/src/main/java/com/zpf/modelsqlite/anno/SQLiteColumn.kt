package com.zpf.modelsqlite.anno

import com.zpf.modelsqlite.constant.ColumnEnum

/**
 * 数据库表格‘列’的注解
 * Created by ZPF on 2018/1/29.
 */
@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class SQLiteColumn(val column: ColumnEnum)