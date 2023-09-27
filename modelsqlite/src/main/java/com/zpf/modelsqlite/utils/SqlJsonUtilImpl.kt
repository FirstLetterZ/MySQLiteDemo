package com.zpf.modelsqlite.utils

import com.zpf.modelsqlite.interfaces.ISqlJsonUtil
import org.json.JSONArray
import org.json.JSONObject
import java.lang.reflect.Type
import java.math.BigDecimal

internal object SqlJsonUtilImpl : ISqlJsonUtil {
    private var realJsonUtil: ISqlJsonUtil? = null

    override fun toJsonString(obj: Any?): String {
        return when (obj) {
            null -> ""
            is String -> obj
            is BigDecimal -> obj.toPlainString()
            is Number -> obj.toString()
            is Boolean -> {
                if (obj) {
                    "1"
                } else {
                    "0"
                }
            }
            is JSONObject -> obj.toString()
            is JSONArray -> obj.toString()
            else -> {
                val util = realJsonUtil
                if (util != null) {
                    return util.toJsonString(obj)
                } else {
                    obj.toString()
                }
            }
        }
    }

    override fun <T> fromJson(json: String?, type: Type): T? {
        val result: Any? = when (type) {
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
                if (json == null) {
                    null
                } else {
                    try {
                        JSONArray(json)
                    } catch (e: Exception) {
                        null
                    }
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