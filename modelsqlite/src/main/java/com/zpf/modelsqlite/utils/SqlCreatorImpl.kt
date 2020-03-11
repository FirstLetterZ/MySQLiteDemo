package com.zpf.modelsqlite.utils

import com.zpf.modelsqlite.interfaces.ObjCreator
import java.lang.reflect.Type

class SqlCreatorImpl private constructor() {
    companion object {
        private val impl = SqlCreatorImpl()
        fun get(): SqlCreatorImpl {
            return impl
        }
    }

    private val cacheMap = HashMap<Type, ObjCreator<*>>()
    private val creatorSet = HashSet<ObjCreator<*>>()

    fun <T> newInstance(type: Type): T? {
        val typeCreator = cacheMap[type]
        var result: Any? = null
        if (typeCreator != null) {
            result = typeCreator.create(type) as? T
        }
        if (result != null) {
            return result as? T
        } else {
            cacheMap.remove(type)
        }
        val rawTypeClass = Utils.getRawTypeClass(type)
        if (rawTypeClass != null) {
            result = try {
                rawTypeClass.newInstance()
            } catch (e: Exception) {
                Logger.e(e.toString())
            }
            if (result == null) {
                try {
                    val constructor = rawTypeClass.getDeclaredConstructor()
                    val args: Array<Any>? = null
                    constructor.newInstance(args)
                } catch (e: Exception) {
                    Logger.e(e.toString())
                }
            }
        }
        if (result != null) {
            return result as? T
        }
        for (creator in creatorSet) {
            result = creator.create(type)
            if (result != null) {
                cacheMap[type] = creator
                break
            }
        }
        return result as? T
    }

    fun addTypeCreator(creator: ObjCreator<*>) {
        creatorSet.add(creator)
    }
}