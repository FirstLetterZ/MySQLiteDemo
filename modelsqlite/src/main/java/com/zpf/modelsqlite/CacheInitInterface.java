package com.zpf.modelsqlite;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * 获取创建CacheDao所需要的参数
 * Created by ZPF on 2018/4/25.
 */

public interface CacheInitInterface {

    @NonNull
    Context getContext();

    /**
     * 数据库文件位置,如果返回null或""，则使用系统默认位置"data/< 项目文件夹 >/databases/"，
     * 否则需要注意提前判断是否需要读写权限
     */
    @Nullable
    String getDataBaseFolderPath();

    /**
     * 将数据转成json格式的字符串，具体实现方式由使用者决定
     */
    String toJson(Object object);

    /**
     * 将json格式转化成对应的对象，具体实现方式由使用者决定
     */
    Object fromJson(String json, Class<?> cls);
}
