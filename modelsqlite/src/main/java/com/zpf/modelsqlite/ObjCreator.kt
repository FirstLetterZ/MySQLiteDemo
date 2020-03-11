package com.zpf.modelsqlite

import java.lang.reflect.Type

/**
 * 用于创建数据接收对象
 * Created by ZPF on 2018/4/25.
 */
interface ObjCreator<T> {
    fun create(t: Type): T
}