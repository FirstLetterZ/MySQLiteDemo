package com.zpf.modelsqlite.retrofit

import com.zpf.modelsqlite.*
import com.zpf.modelsqlite.anno.additional.*
import com.zpf.modelsqlite.anno.operation.*
import com.zpf.modelsqlite.constant.ColumnEnum
import com.zpf.modelsqlite.utils.QueryResultConvertFactory
import com.zpf.modelsqlite.utils.ResultConvert
import java.lang.reflect.GenericArrayType
import java.lang.reflect.Method
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

interface SqlExecutor {
    fun execute(sqlDao: ISqlDao, args: Array<Any?>?): Any?
    fun methodType(): Type

    class Builder(method: Method) {
        val returnType = method.genericReturnType
        var itemType: Type = returnType
        var tableName: String = ""
        var originalString: String? = null
        var sqlMethodType: Annotation? = null
        var transaction: Boolean = false
        var whereArgs: List<SqlColumnInfo>? = null
        var replaceMap: Map<String, Int>? = null
        var valueMap: Map<ColumnEnum, Int>? = null
        var bodyIndex = -1

        var orderInfo: SqlOrderInfo? = null
        var groupInfo: SqlGroupInfo? = null
        var limitInfo: SqlLimitInfo? = null
        var pageInfoIndex = -1
        var queryResultConvert: ResultConvert<Any>? = null

        init {
            method.annotations.map {
                when (it) {
                    is ORIGINAL -> {
                        sqlMethodType = checkSqlMethodType(it, sqlMethodType)
                        if (it.sqlString.isEmpty()) {
                            throw IllegalArgumentException("The value of @ORIGINAL can not be empty")
                        }
                        originalString = it.sqlString
                    }
                    is QUERY -> {
                        sqlMethodType = checkSqlMethodType(it, sqlMethodType)
                        sqlMethodType = it
                        transaction = it.transaction
                        tableName = it.table
                        if (limitInfo == null) {
                            limitInfo = SqlLimitInfo()
                        }
                        limitInfo?.offset = it.startIndex
                        limitInfo?.pageSize = it.endIndex - it.startIndex
                        limitInfo?.pageNumber = 1
                    }
                    is DELETE -> {
                        sqlMethodType = checkSqlMethodType(it, sqlMethodType)
                        sqlMethodType = it
                        transaction = it.transaction
                        tableName = it.table
                    }
                    is SAVE -> {
                        sqlMethodType = checkSqlMethodType(it, sqlMethodType)
                        sqlMethodType = it
                        transaction = it.transaction
                        tableName = it.table
                    }
                    is UPDATE -> {
                        sqlMethodType = checkSqlMethodType(it, sqlMethodType)
                        sqlMethodType = it
                        transaction = it.transaction
                        tableName = it.table
                    }
                    is ORDER -> {
                        if (orderInfo == null) {
                            orderInfo = SqlOrderInfo()
                        }
                        it.columns.map { item ->
                            orderInfo?.columnArray?.add(item)
                        }
                        orderInfo?.asc = it.asc
                    }
                    is GROUP -> {
                        if (groupInfo == null) {
                            groupInfo = SqlGroupInfo()
                        }
                        it.columns.map { item ->
                            groupInfo?.columnArray?.add(item)
                        }
                        groupInfo?.having
                    }
                    else -> {
                        throw IllegalArgumentException("Unknown Annotation Type :$it")
                    }
                }
            }

            val parameterTypes = method.genericParameterTypes
            val parameterAnnotationsArray = method.parameterAnnotations
            parameterAnnotationsArray.forEachIndexed { i, item ->
                parseParameters(i, parameterTypes[i], item)
            }

            when (sqlMethodType) {
                is ORIGINAL -> {
                    if (returnType.javaClass != Unit::class.java) {
                        throw IllegalArgumentException("Does not support the returnType:$returnType")
                    }
                }
                is QUERY -> {
                    if (returnType is GenericArrayType) {
                        itemType = returnType.genericComponentType
                        queryResultConvert = QueryResultConvertFactory.arrayConvert
                    } else if (returnType is Class<*> && returnType.isArray) {
                        itemType = returnType.componentType!!
                        queryResultConvert = QueryResultConvertFactory.arrayConvert
                    } else if (returnType is ParameterizedType) {
                        val rawType = returnType.rawType
                        itemType = returnType.actualTypeArguments.getOrNull(0) ?: Any::class.java
                        if (rawType is Class<*>) {
                            if (Set::class.java.isAssignableFrom(rawType)) {
                                queryResultConvert = QueryResultConvertFactory.setConvert
                            } else if (List::class.java.isAssignableFrom(rawType)) {
                                queryResultConvert = QueryResultConvertFactory.listConvert
                            } else if (Queue::class.java.isAssignableFrom(rawType)) {
                                queryResultConvert = QueryResultConvertFactory.queueConvert
                            }
                        }
                    } else {
                        queryResultConvert = QueryResultConvertFactory.singleConvert
                    }
                    if (queryResultConvert == null) {
                        //TODO 暂不支持
                        throw IllegalArgumentException("Does not support the returnType:$returnType")
                    }
                }
                is DELETE -> {
                    if (returnType !is Number && returnType.javaClass != Boolean::class.java) {
                        throw IllegalArgumentException("Does not support the returnType:$returnType")
                    }
                }
                is SAVE -> {
                    if (returnType !is Number && returnType.javaClass != Boolean::class.java) {
                        throw IllegalArgumentException("Does not support the returnType:$returnType")
                    }
                }
                is UPDATE -> {
                    if (returnType !is Number && returnType.javaClass != Boolean::class.java) {
                        throw IllegalArgumentException("Does not support the returnType:$returnType")
                    }
                }
            }
        }

        fun build(): SqlExecutor? {
            return when (sqlMethodType) {
                is ORIGINAL -> {
                    SqlOriginalExecutor(this)
                }
                is QUERY -> {
                    SqlQueryExecutor(this, itemType, queryResultConvert!!)
                }
                is DELETE -> {
                    SqlDeleteExecutor(this)
                }
                is SAVE -> {
                    SqlSaveFactory(this)
                }
                is UPDATE -> {
                    SqlUpdateFactory(this)
                }
                else -> {
                    null
                }
            }
        }

        private fun parseParameters(index: Int, pType: Type, pAnno: Array<Annotation>) {
            var type: Annotation? = null
            pAnno.map {
                when (it) {
                    is DataBody -> {
                        if (sqlMethodType is SAVE || sqlMethodType is UPDATE) {
                            type = checkSqlMethodType(it, type)
                            bodyIndex = index
                        }
                    }
                    is Havinng -> {
                        if (sqlMethodType is QUERY && groupInfo != null) {
                            type = checkSqlMethodType(it, type)
                            groupInfo?.having?.let { group ->
                                val columnInfo = SqlColumnInfo(it.column, index)
                                columnInfo.relation = it.relation
                                columnInfo.funcName = it.sqlFunc
                                columnInfo.useAndConnector = it.useAndConnector
                                columnInfo.ignoreOnNull = it.ignoreOnNull
                                group.add(columnInfo)
                            }
                        }
                    }
                    is Page -> {
                        if (sqlMethodType is QUERY) {
                            type = checkSqlMethodType(it, type)
                            if (pType is Number) {
                                if (it.offset >= 0 && it.pageSize > 0) {
                                    if (limitInfo == null) {
                                        limitInfo = SqlLimitInfo()
                                    }
                                    limitInfo?.offset = it.offset
                                    limitInfo?.pageSize = it.pageSize
                                    pageInfoIndex = index
                                } else {
                                    throw IllegalArgumentException("Because offset = ${it.offset} & pageSize = ${it.pageSize} @Page")
                                }
                            } else {
                                throw IllegalArgumentException("The type of Parameter with @Page must be Number")
                            }
                        }
                    }
                    is Replace -> {
                        if (sqlMethodType is ORIGINAL) {
                            type = checkSqlMethodType(it, type)
                            if (it.holder.isNotEmpty()) {
                                if (replaceMap == null) {
                                    replaceMap = HashMap()
                                }
                                (replaceMap as? HashMap)?.put(it.holder, index)
                            }
                        }
                    }
                    is Value -> {
                        type = checkSqlMethodType(it, type)
                        if (sqlMethodType is SAVE || sqlMethodType is UPDATE) {
                            type = checkSqlMethodType(it, type)
                            if (valueMap == null) {
                                valueMap = EnumMap(ColumnEnum::class.java)
                            }
                            (valueMap as? EnumMap)?.put(it.column, index)
                        }
                    }
                    is Where -> {
                        type = checkSqlMethodType(it, type)
                        if (sqlMethodType !is ORIGINAL) {
                            type = checkSqlMethodType(it, type)
                            if (whereArgs == null) {
                                whereArgs = ArrayList()
                            }
                            val columnInfo = SqlColumnInfo(it.column, index)
                            columnInfo.relation = it.relation
                            columnInfo.funcName = it.sqlFunc
                            columnInfo.useAndConnector = it.useAndConnector
                            columnInfo.ignoreOnNull = it.ignoreOnNull
                            (whereArgs as? ArrayList)?.add(columnInfo)
                        }
                    }
                }
            }

        }

        private fun checkSqlMethodType(target: Annotation, current: Annotation?): Annotation {
            if (current == null) {
                return target
            } else {
                val msg = String.format("Only one SQL method is allowed. Found: %s and %s.",
                        target.javaClass.name, current.javaClass.name)
                throw RuntimeException(msg)
            }
        }
    }
}