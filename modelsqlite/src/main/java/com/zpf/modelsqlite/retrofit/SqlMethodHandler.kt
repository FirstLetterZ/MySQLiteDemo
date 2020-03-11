package com.zpf.modelsqlite.retrofit

import com.zpf.modelsqlite.ISqlDao
import com.zpf.modelsqlite.utils.FormatUtil
import java.lang.reflect.Type

class SqlMethodHandler(private val factory: SqlExecutor, private val type: Type) {

    fun invoke(sqlDao: ISqlDao, args: Array<Any?>?): Any? {
        val result = factory.execute(sqlDao, args)
        if (result == null || type.javaClass == result.javaClass || type.javaClass.isAssignableFrom(result.javaClass)) {
            return result
        }
        if (type is Number) {
            if (result is Boolean) {
                return if (result) {
                    1
                } else {
                    -1
                }
            } else {
                return try {
                    result.toString().toInt()
                } catch (e: Exception) {
                    -1
                }
            }
        } else if (type.javaClass == Boolean::class.java) {
            if (result is Number) {
                return result.toInt() > 0
            } else if (result is String) {
                return result.isNotEmpty()
            } else if (result is Map<*, *>) {
                return result.size > 0
            } else if (result is Iterable<*>) {
                return result.iterator().hasNext()
            } else {
                return true
            }
        } else if (type is CharSequence) {
            return FormatUtil.formatString(result)
        } else {
            return result
        }
    }
}