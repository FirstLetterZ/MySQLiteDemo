package com.zpf.modelsqlite;

/**
 * 用于创建数据接收对象
 * Created by ZPF on 2018/4/25.
 */
public interface ClassObjectCreator<T> {
     T create();
}
