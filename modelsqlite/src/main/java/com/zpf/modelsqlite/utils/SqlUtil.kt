package com.zpf.modelsqlite.utils

import android.app.Application
import android.content.pm.ApplicationInfo
import com.zpf.modelsqlite.*
import com.zpf.modelsqlite.constant.ColumnEnum
import com.zpf.modelsqlite.constant.SQLiteConfig
import java.lang.reflect.*
import java.lang.reflect.Array
import java.util.*
import kotlin.collections.HashMap

object SqlUtil {
    private val creatorMap = HashMap<Type, ObjCreator<*>>()

    internal val defJsonUtil: ISqlJsonUtil by lazy {
        SqlJsonUtilImpl()
    }

    private val defSqlDao: CacheDao by lazy {
        CacheDao(SQLiteConfig.DB_PATH)
    }

    fun initConfig(app: Application, tag: String? = null, dbPath: String? = null) {
        SQLiteConfig.DEBUG = ((app.applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE) != 0)
        if (!tag.isNullOrEmpty()) {
            SQLiteConfig.LOG_TAG = tag
        }
        SQLiteConfig.appContext = app
        SQLiteConfig.DB_PATH = dbPath
    }

    fun get(): CacheDao {
        return defSqlDao
    }

    fun setJsonUtil(util: ISqlJsonUtil?) {
        (defJsonUtil as? SqlJsonUtilImpl)?.setJsonUtil(util)
    }

    fun addObjCreator(type: Type, creator: ObjCreator<*>) {
        creatorMap[type] = creator
    }

    fun newInstance(type: Type): Any? {
        val typeCreator = creatorMap[type]
        if (typeCreator != null) {
            return typeCreator.create(type)
        }
        val rawTypeClass = Utils.getRawTypeClass(type)
        var result: Any? = null
        if (rawTypeClass != null) {
            result = try {
                rawTypeClass.newInstance()
            } catch (e: Exception) {
                Logger.e(e.toString())
            }
            if (result == null) {
                try {
                    val constructor = rawTypeClass.getDeclaredConstructor()
                    val args: kotlin.Array<Any>? = null
                    constructor.newInstance(args)
                } catch (e: Exception) {
                    Logger.e(e.toString())
                }
            }
        }
        if (result != null) {
            return result
        }
        return result
    }


}