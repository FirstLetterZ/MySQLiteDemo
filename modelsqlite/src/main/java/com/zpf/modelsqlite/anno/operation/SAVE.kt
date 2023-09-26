package com.zpf.modelsqlite.anno.operation

/**
 * 数据库操作，尝试更新，失败后新建，如果没有额外查询条件，仅尝试新建
 * 返回数据类型 number 或 boolean
 * Created by ZPF on 2020/3/6.
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class SAVE(
        val table: String,
        val transaction: Boolean = false//开启事务
)