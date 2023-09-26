package com.zpf.modelsqlite.anno

/**
 * 数据库表格‘表’的注解
 * Created by ZPF on 2018/1/24.
 */
@Target(AnnotationTarget.ANNOTATION_CLASS, AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class SQLiteClassify(val tableName: String)