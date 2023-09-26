package com.zpf.modelsqlite;

import android.app.Application;
import android.database.sqlite.SQLiteOpenHelper;

import com.zpf.modelsqlite.constant.SQLiteConfig;
import com.zpf.modelsqlite.interfaces.ISqlJsonUtil;
import com.zpf.modelsqlite.interfaces.ObjCreator;
import com.zpf.modelsqlite.retrofit.SqlRetrofit;
import com.zpf.modelsqlite.utils.SqlCreatorImpl;
import com.zpf.modelsqlite.utils.SqlJsonUtilImpl;

import java.util.logging.Logger;

public class SqlUtil {
    private SqlUtil() {
    }

    private static volatile CacheDao defSqlDao;

    public static void initConfig(Application app, String dbPath) {
        SQLiteConfig.initConfig(app, dbPath);
    }

    public static CacheDao getDao() {
        if (defSqlDao == null) {
            synchronized (SqlUtil.class) {
                if (defSqlDao == null) {
                    defSqlDao = new CacheDao(SQLiteConfig.DB_PATH);
                }
            }
        }
        return defSqlDao;
    }

    public static CacheDao creatCacheDao(SQLiteOpenHelper openHelper) {
        return new CacheDao(openHelper);
    }

    public static void setJsonUtil(ISqlJsonUtil util) {
        SqlJsonUtilImpl.INSTANCE.setJsonUtil(util);
    }

    public static void addTypeCreator(ObjCreator<?> creator) {
        SqlCreatorImpl.INSTANCE.addTypeCreator(creator);
    }

    public static void setLogger(Logger logger) {
        com.zpf.modelsqlite.utils.Logger.INSTANCE.setRealLogger(logger);
    }

    public static <T> T create(Class<T> service) {
        return SqlRetrofit.INSTANCE.create(service);
    }
}
