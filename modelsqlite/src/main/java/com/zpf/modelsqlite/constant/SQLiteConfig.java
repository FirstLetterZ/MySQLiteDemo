package com.zpf.modelsqlite.constant;

import android.app.Application;
import android.content.pm.ApplicationInfo;

public class SQLiteConfig {
    private SQLiteConfig() {
    }
    public static final int DB_CACHE_VERSION = 1;
    public static final String DB_USER_CACHE = "userCache.db";
    public static final String TB_CACHE = "userCache";
    public static final String PRIMARY_KEY = "_id"; //主键
    public static final String FIRST_KEY_WORD = "keyword"; //表名
    public static final String OTHER = "other"; //预留列
    public static final String TIME_CREATE = "create_time"; //数据插入时间--long类型
    public static final String TIME_UPDATE = "update_time"; //数据更新时间--long类型
    public static final String COLUMN_NAME = "column";

    public static final int TYPE_SHORT = 1000;
    public static final int TYPE_DOUBLE = 2000;
    public static final int TYPE_LONG = 3000;
    public static final int TYPE_FLOAT = 4000;
    public static final int TYPE_BOOLEAN = 5000;
    public static final int TYPE_INT = 6000;
    public static final int TYPE_STRING = 7000;

    public static final int DEF_NUMBER = 0;
    public static final boolean DEF_BOOLEAN = false;
    public static final String DEF_STRING = "";

    public static final String COMMA = " , ";
    public static final String ORDER = " ORDER BY ";
    public static final String GROUP = " GROUP BY ";
    public static final String HAVING = " HAVING ";
    public static final String ASC = " ASC";
    public static final String DESC = " DESC";
    public static final String LIMIT = " LIMIT ";
    public static final String AND = " AND ";
    public static final String OR = " OR ";

    public static final String WHERE = " WHERE " + FIRST_KEY_WORD + " = ";
    public static final String VALUES = ")VALUES(";
    public static final String SQL_SELECT = "SELECT * FROM " + TB_CACHE + WHERE;
    public static final String SQL_UPDATE = "UPDATE " + TB_CACHE + " SET ";
    public static final String SQL_INSERT = "INSERT INTO " + TB_CACHE + " (";
    public static final String SQL_DELETE = "DELETE FROM " + TB_CACHE + WHERE;

    private static Application appContext;
    public static String DB_PATH = null;

    public static void initConfig(Application app, String dbPath) {
        if (app != null) {
            appContext = app;
        }
        DB_PATH = dbPath;
    }

    public static Application getAppContext() {
        return appContext;
    }

}
