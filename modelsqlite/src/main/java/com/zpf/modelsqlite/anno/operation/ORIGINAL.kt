package com.zpf.modelsqlite.anno.operation

/**
 * 数据库操作，忽略其他注解，执行参数内的sqlite语句
 * 返回数据类型 boolean
 * Created by ZPF on 2020/3/6.
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class ORIGINAL(val sqlString: String = "")
