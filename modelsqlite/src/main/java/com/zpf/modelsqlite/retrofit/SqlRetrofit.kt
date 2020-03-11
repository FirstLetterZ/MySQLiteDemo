package com.zpf.modelsqlite.retrofit

import com.zpf.modelsqlite.utils.SqlUtil
import com.zpf.modelsqlite.utils.Utils
import java.lang.reflect.Method
import java.lang.reflect.Proxy
import java.util.concurrent.ConcurrentHashMap

class SqlRetrofit {
    private val handlerCache = ConcurrentHashMap<Method, SqlMethodHandler>()

    fun <T> create(service: Class<T>): T? {
        if (!service.isInterface) {
            throw java.lang.IllegalArgumentException("API declarations must be interfaces.")
        }
        return Proxy.newProxyInstance(service.classLoader, arrayOf<Class<*>?>(service)
        ) { _, method, args ->
            getSqlMethodHandler(method)?.invoke(SqlUtil.get(), args)
        } as? T
    }

    private fun getSqlMethodHandler(method: Method): SqlMethodHandler? {
        var result = handlerCache[method]
        if (result != null) {
            return result
        }
        synchronized(handlerCache) {
            result = handlerCache[method]
            if (result == null) {
                val returnType = method.genericReturnType
                if (Utils.hasUnresolvableType(returnType)) {
                    throw IllegalArgumentException(
                            "Method return type must not include a type variable or wildcard: " + returnType.toString() + " for method " + method.name)
                }
                val sqlFactory = SqlExecutor.Builder(method).build()
                if (sqlFactory != null) {
                    result = SqlMethodHandler(sqlFactory, returnType)
                    handlerCache[method] = result!!
                }
            }
        }
        return result
    }

}