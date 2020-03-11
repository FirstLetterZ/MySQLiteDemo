package com.zpf.modelsqlite.retrofit

import com.zpf.modelsqlite.interfaces.ISqlDao
import com.zpf.modelsqlite.anno.operation.ORIGINAL
import com.zpf.modelsqlite.utils.SqlJsonUtilImpl
import java.lang.reflect.Type

class SqlOriginalExecutor(builder: SqlExecutor.Builder) : SqlExecutor {
    val transaction = builder.transaction
    private val replaceMap = builder.replaceMap
    private val sqlString: String = builder.originalString!!

    override fun execute(sqlDao: ISqlDao, args: Array<Any?>?): Boolean {
        val result = sqlString
        var newValue: String
        replaceMap?.map {
            newValue = SqlJsonUtilImpl.get().toJsonString(it.value)
            result.replace("{${it.key}}", newValue)
        }
        return sqlDao.execSqlString(result, transaction)
    }

    override fun methodType(): Type = ORIGINAL::class.java
}