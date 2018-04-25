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

    public static final String RELATION_EQUALITY = "=?";
    public static final String RELATION_LIKE = " LIKE ?";
    public static final String RELATION_LESS_THAN = "<?";
    public static final String RELATION_MORE_THAN = ">?";
    public static final String RELATION_LESS_OR_EQUAL = "<=?";
    public static final String RELATION_MORE_OR_EQUAL = ">=?";
    public static final String ORDER = " order by ";
    public static final String ASC = " asc";
    public static final String DESC = " desc";

    public static final int DEF_NUMBER = 0;
    public static final boolean DEF_BOOLEAN = false;
    public static final String DEF_STRING = "";
}
