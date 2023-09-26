package com.zpf.modelsqlite.anno

import com.zpf.modelsqlite.constant.ColumnEnum

/**
 * 注解字段与其他表关联
 * 关联对象必须支持newInstance()
 * Created by ZPF on 2018/4/24.
 */
@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class SQLiteRelevance(
    val saveColumn: ColumnEnum, //保存的位置,应查找类型一致
    val targetColumn: ColumnEnum //查找的关键字,应与保存类型一致
)