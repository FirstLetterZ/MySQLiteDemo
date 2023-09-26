package com.zpf.modelsqlite.anno.operation

/**
 * 数据库操作，删除符合条件的全部数据，如果没有额外数据，删除table之下全部数据
 * 返回数据类型 boolean
 * Created by ZPF on 2020/3/6.
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class DELETE(
        val table: String,
        val transaction: Boolean = false//开启事务
)