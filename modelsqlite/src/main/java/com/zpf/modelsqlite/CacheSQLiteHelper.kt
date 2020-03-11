package com.zpf.modelsqlite

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.zpf.modelsqlite.constant.ColumnEnum
import com.zpf.modelsqlite.constant.SQLiteConfig
import com.zpf.modelsqlite.utils.Logger

/**
 * 数据库构建
 * Created by ZPF on 2018/1/29.
 */
class CacheSQLiteHelper internal constructor(context: Context, name: String?) : SQLiteOpenHelper(context, name, null, SQLiteConfig.DB_CACHE_VERSION) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(createTbCache())
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {}

    protected fun createTbCache(): String {
        val builder = StringBuilder()
        builder.append("create table if not exists ")
                .append(SQLiteConfig.TB_CACHE)
                .append("(")
                .append(SQLiteConfig.PRIMARY_KEY)
                .append(" integer primary key autoincrement,")
                .append(SQLiteConfig.FIRST_KEY_WORD)
                .append(" text")
        val columnEnums = ColumnEnum.values()
        for (columnEnum in columnEnums) {
            val column = columnEnum.value
            val str = getColumnType(column)
            if (str != null) {
                builder.append(",").append(SQLiteConfig.COLUMN_NAME).append(column).append(str)
            }
        }
        builder.append(",")
                .append(SQLiteConfig.TIME_CREATE)
                .append(" integer,")
                .append(SQLiteConfig.TIME_UPDATE)
                .append(" integer,")
                .append(SQLiteConfig.OTHER)
                .append(" text")
                .append(")")
        val result = builder.toString()
        Logger.i(result)
        return result
    }

    private fun getColumnType(column: Int): String? {
        return when (column / 1000 * 1000) {
            SQLiteConfig.TYPE_STRING -> " text"
            SQLiteConfig.TYPE_DOUBLE, SQLiteConfig.TYPE_FLOAT -> " real"
            SQLiteConfig.TYPE_INT, SQLiteConfig.TYPE_BOOLEAN, SQLiteConfig.TYPE_LONG, SQLiteConfig.TYPE_SHORT -> " integer"
            else -> null
        }
    }
}