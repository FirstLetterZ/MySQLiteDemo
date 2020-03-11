package com.zpf.modelsqlite.utils

import com.zpf.modelsqlite.constant.ColumnEnum
import com.zpf.modelsqlite.constant.SQLiteConfig
import java.lang.reflect.*
import java.lang.reflect.Array
import java.util.*

object Utils {


    internal fun getAllFields(cls: Class<*>?): MutableList<Field> {
        var tempClass: Class<*>? = cls
        val fieldList: MutableList<Field> = ArrayList()
        while (tempClass != null) { //当父类为null的时候说明到达了最上层的父类(Object类).
            fieldList.addAll(tempClass.declaredFields)
            tempClass = tempClass.superclass
        }
        return fieldList
    }


    /**
     * 根据获取默认值
     */
    internal fun getDefaultValue(column: ColumnEnum): Any? {
        return when (getColumnType(column.value)) {
            SQLiteConfig.TYPE_STRING -> SQLiteConfig.DEF_STRING
            SQLiteConfig.TYPE_INT -> SQLiteConfig.DEF_NUMBER
            SQLiteConfig.TYPE_BOOLEAN -> SQLiteConfig.DEF_BOOLEAN
            SQLiteConfig.TYPE_FLOAT -> SQLiteConfig.DEF_NUMBER
            SQLiteConfig.TYPE_LONG -> SQLiteConfig.DEF_NUMBER
            SQLiteConfig.TYPE_SHORT -> SQLiteConfig.DEF_NUMBER
            SQLiteConfig.TYPE_DOUBLE -> SQLiteConfig.DEF_NUMBER
            else -> SQLiteConfig.DEF_STRING
        }
    }

    internal fun getColumnType(column: Int): Int {
        return column / 1000 * 1000
    }

    internal fun getRawTypeClass(type: Type): Class<*>? {
        return if (type is Class<*>) {
            type
        } else if (type is ParameterizedType) {
            val rawType = type.rawType
            if (rawType is Class<*>) {
                rawType
            } else {
                null
            }
        } else if (type is GenericArrayType) {
            val componentType: Type? = type.genericComponentType
            if (componentType == null) {
                Array.newInstance(Any::class.java, 0).javaClass
            } else {
                val rawType = getRawTypeClass(componentType)
                if (rawType == null) {
                    null
                } else {
                    Array.newInstance(rawType, 0).javaClass
                }
            }
        } else if (type is WildcardType) {
            getRawTypeClass(type.upperBounds[0])
        } else if (type is TypeVariable<*>) {
            Any::class.java
        } else {
            null
        }
    }

    fun hasUnresolvableType(type: Type?): Boolean {
        if (type is Class<*>) {
            return false
        }
        if (type is ParameterizedType) {
            for (typeArgument in type.actualTypeArguments) {
                if (hasUnresolvableType(typeArgument)) {
                    return true
                }
            }
            return false
        }
        if (type is GenericArrayType) {
            return hasUnresolvableType(type.genericComponentType)
        }
        if (type is TypeVariable<*>) {
            return true
        }
        if (type is WildcardType) {
            return true
        }
        val className = type?.javaClass?.name ?: "null"
        throw IllegalArgumentException("Expected a Class, ParameterizedType, or "
                + "GenericArrayType, but <" + type + "> is of type " + className)
    }

}