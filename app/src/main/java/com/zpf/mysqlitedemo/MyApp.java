package com.zpf.mysqlitedemo;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import com.google.gson.Gson;
import com.zpf.modelsqlite.CacheDao;
import com.zpf.modelsqlite.CacheInitInterface;
import com.zpf.modelsqlite.CacheUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZPF on 2018/1/29.
 */

public class MyApp extends Application {
    private List<Activity> activityList = new ArrayList<>();
    private Gson gson = new Gson();
    private static MyApp myApp;

    @Override
    public void onCreate() {
        super.onCreate();
        myApp = this;
        CacheUtil.init(new CacheInitInterface() {
            @Override
            public Context getContext() {
                return getApplicationContext();
            }

            @Override
            public String getDataBaseFolderPath() {
                return null;
            }

            @Override
            public String toJson(Object object) {
                return gson.toJson(object);
            }

            @Override
            public Object fromJson(String json, Class<?> cls) {
                return gson.fromJson(json,cls);
            }
        });
    }

    public static MyApp instance() {
        return myApp;
    }

    public void addActivity(Activity activity) {
        if (!activityList.contains(activity)) {
            activityList.add(activity);
        }
    }

    public boolean removeActivity(Activity activity) {
        boolean result = activityList.remove(activity);
        if (activityList.size() == 0) {
            CacheUtil.closeDB();
        }
        return result;
    }

}
