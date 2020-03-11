package com.zpf.modelsqlite.utils

import java.lang.reflect.Type

interface ISqlJsonUtil {
    fun toJsonString(obj: Any?): String
    fun <T> fromJson(json: String?, type: Type):T?
}