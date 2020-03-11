package com.zpf.modelsqlite

import android.database.Cursor
import com.zpf.modelsqlite.constant.ColumnEnum
import java.lang.reflect.Type

interface ISqlDao {
    fun isDbOpen(): Boolean
    fun closeDB()
    fun totalCount(): Long
    fun saveValue(value: Any): Int
    fun saveValue(value: Any, whereClause: SQLiteInfo?): Int
    fun saveByArray(valueArray: Map<Any, SQLiteInfo?>): Boolean
    fun updateValue(info: SQLiteInfo): Boolean
    fun insertValue(info: SQLiteInfo): Boolean
    fun delete(info: SQLiteInfo): Boolean
    fun deleteByArray(infoList: List<SQLiteInfo>): Boolean
    fun queryCursor(info: SQLiteInfo): Cursor
    fun getColumnValue(column: ColumnEnum, info: SQLiteInfo): Any?
    fun <T> queryFirst(type: Type, info: SQLiteInfo): T?
    fun <T> queryArray(type: Type, info: SQLiteInfo): List<T>
    fun execSqlString(sqlString: String, openTransaction: Boolean): Boolean
    fun execSqlArray(sqlStringArray: List<String>, openTransaction: Boolean): Boolean
}