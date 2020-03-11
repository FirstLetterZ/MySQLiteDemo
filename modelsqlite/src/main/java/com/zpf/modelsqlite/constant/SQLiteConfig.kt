package com.zpf.modelsqlite.constant

import android.app.Application

/**
 * 数据可辅助常量
 * Created by ZPF on 2018/1/24.
 */
object SQLiteConfig {
    const val DB_CACHE_VERSION = 1
    const val DB_USER_CACHE = "userCache.db"
    const val TB_CACHE = "userCache"
    const val PRIMARY_KEY = "_id" //主键
    const val FIRST_KEY_WORD = "keyword" //表名
    const val OTHER = "other" //预留列
    const val TIME_CREATE = "create_time" //数据插入时间--long类型
    const val TIME_UPDATE = "update_time" //数据更新时间--long类型
    const val COLUMN_NAME = "column"

    const val TYPE_SHORT = 1000
    const val TYPE_DOUBLE = 2000
    const val TYPE_LONG = 3000
    const val TYPE_FLOAT = 4000
    const val TYPE_BOOLEAN = 5000
    const val TYPE_INT = 6000
    const val TYPE_STRING = 7000

    const val DEF_NUMBER = 0
    const val DEF_BOOLEAN = false
    const val DEF_STRING = ""

    const val COMMA = " , "
    const val ORDER = " ORDER BY "
    const val GROUP = " GROUP BY "
    const val HAVING = " HAVING "
    const val ASC = " ASC"
    const val DESC = " DESC"
    const val LIMIT = " LIMIT "
    const val AND = " AND "
    const val OR = " OR "

    const val WHERE = " WHERE $FIRST_KEY_WORD = "
    const val VALUES = ")VALUES("
    const val SQL_SELECT = "SELECT * FROM $TB_CACHE$WHERE"
    const val SQL_UPDATE = "UPDATE $TB_CACHE SET "
    const val SQL_INSERT = "INSERT INTO $TB_CACHE ("
    const val SQL_DELETE = "DELETE FROM $TB_CACHE$WHERE"

    internal var DB_PATH: String? = null
    internal var appContext: Application? = null
    internal var LOG_TAG = "ModelSQLite"
    internal var DEBUG = false
}