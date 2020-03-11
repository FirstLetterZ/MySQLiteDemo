package com.zpf.modelsqlite.anno.additional

import com.zpf.modelsqlite.constant.ColumnEnum

/**
 * 数据库操作限制，排序规则，仅在执行 @QUERY 时生效
 * Created by ZPF on 2020/3/6.
 */
@Target(AnnotationTarget.FUNCTION)
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
annotation class ORDER(val columns: Array<ColumnEnum>, val asc: Boolean = true)