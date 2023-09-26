package com.zpf.modelsqlite.retrofit

import com.zpf.modelsqlite.interfaces.ISqlDao
import com.zpf.modelsqlite.utils.SqlJsonUtilImpl
import com.zpf.modelsqlite.utils.Utils
import java.lang.reflect.Type

class SqlMethodHandler(private val factory: SqlExecutor, private val type: Type) {

    fun invoke(sqlDao: ISqlDao, args: Array<Any?>?): Any? {
        val result = factory.execute(sqlDao, args)
        if (result == null || type.javaClass == result.javaClass || type.javaClass.isAssignableFrom(result.javaClass)) {
            return result
        }
        if (Utils.checkNumber(type)) {
            return if (Utils.checkNumber(result.javaClass)) {
                result as Number
            } else if (Utils.checkBoolean(result.javaClass)) {
                if (result as Boolean) {
                    1
                } else {
                    -1
                }
            } else {
                try {
                    result.toString().toInt()
                } catch (e: Exception) {
                    -1
                }
            }
        } else if (Utils.checkBoolean(type)) {
            return if (Utils.checkBoolean(result.javaClass)) {
                result as Boolean
            } else if (Utils.checkNumber(result.javaClass)) {
                ((result as Number).toInt() > 0)
            } else if (result is CharSequence) {
                result.isNotEmpty()
            } else if (result is Map<*, *>) {
                result.size > 0
            } else if (result is Iterable<*>) {
                result.iterator().hasNext()
            } else {
                true
            }
        } else if (type is CharSequence) {
            return SqlJsonUtilImpl.toJsonString(result)
        } else if (type is Class<*> && type.isPrimitive) {
            return when (type.name) {
                "char" -> result.toString().toCharArray().getOrNull(0)
                "byte" -> result.toString().toByte()
                else -> null
            }
        } else {
            return result
        }
    }
}