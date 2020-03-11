package com.zpf.modelsqlite.anno.operation

/**
 * 数据库操作，尝试更新
 * 返回数据类型 boolean
 * Created by ZPF on 2020/3/6.
 */
@Target(AnnotationTarget.FUNCTION)
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
annotation class UPDATE(
        val table: String,
        val transaction: Boolean = false//开启事务
)