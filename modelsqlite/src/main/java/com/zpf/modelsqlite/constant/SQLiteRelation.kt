package com.zpf.modelsqlite.constant

/**
 * 条件判别枚举
 * Created by ZPF on 2020/3/6.
 */
enum class SQLiteRelation(val value: String) {
    RELATION_EQUALITY(" = "),
    RELATION_LIKE(" LIKE "),
    RELATION_LESS_THAN(" < "),
    RELATION_MORE_THAN(" > "),
    RELATION_LESS_OR_EQUAL(" <= "),
    RELATION_MORE_OR_EQUAL(" >= "),
    RELATION_IS_NULL(" IS NULL "),
    RELATION_NOT_NULL(" IS NOT NULL ");
}