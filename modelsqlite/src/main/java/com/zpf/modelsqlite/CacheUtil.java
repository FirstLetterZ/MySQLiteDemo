package com.zpf.modelsqlite;

import android.support.annotation.NonNull;

/**
 * 提供单例共外部使用
 * Created by ZPF on 2018/4/25.
 */
public class CacheUtil {
    private static volatile CacheDao mDao;
    private static CacheInitInterface mInitInfo;

    //一定要先执行
    public static void init(@NonNull CacheInitInterface info) {
        mInitInfo = info;
    }

    public static CacheDao instance() {
        if (mDao == null) {
            synchronized (CacheUtil.class) {
                if (mDao == null) {
                    mDao = new CacheDao(mInitInfo);
                }
            }
        }
        return mDao;
    }

    public static void closeDB() {
        if (mDao != null) {
            mDao.closeDB();
        } else {
            synchronized (CacheUtil.class) {
                mDao = null;
            }
        }
    }

}
