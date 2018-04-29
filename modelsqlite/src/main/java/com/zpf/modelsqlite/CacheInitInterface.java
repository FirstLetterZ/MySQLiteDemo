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
    Context getContext();//获取上下文

    @Nullable
    String getDataBaseFolderPath();//数据库文件位置,可以为null

    String toJson(Object object);//将数据转成json格式的字符串，具体实现方式由使用者决定

    Object fromJson(String json, Class<?> cls);//将json格式转化成对应的对象，具体实现方式由使用者决定
}
