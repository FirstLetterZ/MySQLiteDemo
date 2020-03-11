package com.zpf.modelsqlite.anno.additional

/**
 * 数据库操作限制，分段，数字从1开始，会将 @LIMIT 覆盖
 * Created by ZPF on 2020/3/6.
 */
@Target(AnnotationTarget.FIELD)
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
annotation class Page(val pageSize: Int, val offset: Int = 0)