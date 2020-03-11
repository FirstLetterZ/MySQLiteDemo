package com.zpf.modelsqlite.utils

import java.lang.reflect.Type

interface ResultConvert<T> {
    fun convert(resultList: List<T>, itemType: Type): Any?
}