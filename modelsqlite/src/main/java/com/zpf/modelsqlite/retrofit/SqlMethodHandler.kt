package com.zpf.modelsqlite.retrofit

import com.zpf.modelsqlite.interfaces.ISqlDao
import com.zpf.modelsqlite.utils.SqlJsonUtilImpl
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
                return returnAsNumber(result)
            }
        } else if (type == Boolean::class.java) {
            return returnAsBoolean(result)
        } else if (type is CharSequence) {
            return SqlJsonUtilImpl.get().toJsonString(result)
        } else if (type is Class<*> && type.isPrimitive) {
            return when (type.name) {
                "boolean" -> returnAsBoolean(result)
                "char" -> result.toString().toCharArray().getOrNull(0)
                "byte" -> result.toString().toByte()
                "short" -> returnAsNumber(result)
                "int" -> returnAsNumber(result)
                "float" -> returnAsNumber(result)
                "long" -> returnAsNumber(result)
                "double" -> returnAsNumber(result)
                else -> null
            }
        } else {
            return result
        }
    }

    private fun returnAsNumber(result: Any): Int {
        return try {
            result.toString().toInt()
        } catch (e: Exception) {
            -1
        }
    }

    private fun returnAsBoolean(result: Any): Boolean {
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
    }
}