package com.zpf.modelsqlite.anno.operation

/**
 * 数据库操作，查询并返回符合条件的全部数据
 * 返回数据类型无限制
 * Created by ZPF on 2020/3/6.
 */
@Target(AnnotationTarget.FUNCTION)
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
annotation class QUERY(
        val table: String,//表
        val startIndex: Int = 0,
        val endIndex: Int = -1,
        val transaction: Boolean = false//开启事务
)
