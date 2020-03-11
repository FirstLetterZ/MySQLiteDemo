package com.zpf.modelsqlite.utils

import com.zpf.modelsqlite.interfaces.ISqlJsonUtil
import org.json.JSONArray
import org.json.JSONObject
import java.lang.reflect.Type
import java.math.BigDecimal

class SqlJsonUtilImpl private constructor() : ISqlJsonUtil {

    companion object {
        private val impl = SqlJsonUtilImpl()
        fun get(): SqlJsonUtilImpl {
            return impl
        }
    }

    private var realJsonUtil: ISqlJsonUtil? = null

    override fun toJsonString(obj: Any?): String {
        return when (obj) {
            null -> {
                ""
            }
            is String -> {
                obj
            }
            is BigDecimal -> {
                obj.toPlainString()
            }
            is Number -> {
                obj.toString()
            }
            is Boolean -> {
                if (obj) {
                    "1"
                } else {
                    "0"
                }
            }
            is JSONObject -> {
                obj.toString()
            }
            is JSONArray -> {
                obj.toString()
            }
            else -> {
                realJsonUtil?.toJsonString(obj) ?: obj.toString()
            }
        }
    }

    override fun <T> fromJson(json: String?, type: Type): T? {
        val result = when (type) {
            Boolean::class.java -> {
                "1" == json
            }
            Int::class.java -> {
                json?.toIntOrNull()
            }
            Float::class.java -> {
                json?.toFloatOrNull()
            }
            Short::class.java -> {
                json?.toShortOrNull()
            }
            Long::class.java -> {
                json?.toLongOrNull()
            }
            Double::class.java -> {
                json?.toDoubleOrNull()
            }
            String::class.java -> {
                json
            }
            BigDecimal::class.java -> {
                try {
                    BigDecimal(json)
                } catch (e: Exception) {
                    null
                }
            }
            JSONObject::class.java -> {
                if (json == null) {
                    null
                } else {
                    try {
                        JSONObject(json)
                    } catch (e: Exception) {
                        null
                    }
                }
            }
            JSONArray::class.java -> {
                try {
                    JSONArray(json)
                } catch (e: Exception) {
                    null
                }
            }
            else -> {
                realJsonUtil?.fromJson(json, type)
            }
        }
        return result as? T
    }

    fun setJsonUtil(util: ISqlJsonUtil?) {
        realJsonUtil = util
    }
}