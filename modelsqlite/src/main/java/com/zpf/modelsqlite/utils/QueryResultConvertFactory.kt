package com.zpf.modelsqlite.utils

import java.lang.reflect.Array
import java.lang.reflect.Type
import java.util.*

object QueryResultConvertFactory {

    val setConvert = object : ResultConvert<Any> {
        override fun convert(resultList: List<Any>, itemType: Type): Set<Any> {
            val set = LinkedHashSet<Any>()
            resultList.map {
                set.add(it)
            }
            return set
        }
    }


    val queueConvert = object : ResultConvert<Any> {
        override fun convert(resultList: List<Any>, itemType: Type): Queue<Any> {
            val queue = ArrayDeque<Any>()
            resultList.map {
                queue.add(it)
            }
            return queue
        }
    }


    val listConvert = object : ResultConvert<Any> {
        override fun convert(resultList: List<Any>, itemType: Type): Set<Any> {
            val set = LinkedHashSet<Any>()
            resultList.map {
                set.add(it)
            }
            return set
        }
    }


    val arrayConvert = object : ResultConvert<Any> {
        override fun convert(resultList: List<Any>, itemType: Type): kotlin.Array<Any> {
            val array = Array.newInstance(itemType.javaClass, resultList.size)
            resultList.forEachIndexed { i, item ->
                Array.set(array, i, item)
            }
            return array as kotlin.Array<Any>
        }
    }


    val singleConvert = object : ResultConvert<Any> {
        override fun convert(resultList: List<Any>, itemType: Type): Any? {
            return resultList.getOrNull(0)
        }
    }
}

