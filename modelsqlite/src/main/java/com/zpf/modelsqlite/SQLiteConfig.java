package com.zpf.modelsqlite;

/**
 * 数据可辅助常量
 * Created by ZPF on 2018/1/24.
 */

public class SQLiteConfig {
    public static final int DB_CACHE_VERSION = 1;

    public static final String DB_USER_CACHE = "userCache.db";
    public static final String TB_CACHE = "userCache";
    public static final String FIRST_KEY_WORD = "keyword";//“表名”
    public static final String OTHER = "other";//预留列
    public static final String COLUMN_NAME = "column";

    public static final int TYPE_SHORT = 1000;
    public static final int TYPE_DOUBLE = 2000;
    public static final int TYPE_LONG = 3000;
    public static final int TYPE_FLOAT = 4000;
    public static final int TYPE_BOOLEAN = 5000;
    public static final int TYPE_INT = 6000;
    public static final int TYPE_STRING = 7000;

    public static final String ORDER = " ORDER BY ";
    public static final String GROUP = " GROUP BY ";
    public static final String HAVING_COUNT = " HAVING  count(";
    public static final String ASC = " ASC";
    public static final String DESC = " DESC";
    public static final String LIMIT = " LIMIT ";
    public static final String OFFSET = " OFFSET ";
    public static final String BETWEEN = " BETWEEN ";
    public static final String AND = " AND ";

    public static final int DEF_NUMBER = 0;
    public static final boolean DEF_BOOLEAN = false;
    public static final String DEF_STRING = "";
}
