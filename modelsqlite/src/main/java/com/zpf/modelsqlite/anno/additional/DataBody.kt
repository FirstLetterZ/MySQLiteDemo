package com.zpf.modelsqlite.anno.additional


/**
 * 数据库操作条件注解，@SAVE 和 @UPDATE 有效
 * Created by ZPF on 2020/3/6.
 */
@Target(AnnotationTarget.FIELD)
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
annotation class DataBody
