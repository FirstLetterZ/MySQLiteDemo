package com.zpf.modelsqlite.anno.additional

/**
 * 数据库操作字段替换，占位处需要使用 {} 包裹，仅在 @ORIGINAL 时生效
 * Created by ZPF on 2020/3/6.
 */
@Target(AnnotationTarget.FIELD)
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
annotation class Replace(val holder: String)