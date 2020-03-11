package com.zpf.modelsqlite.utils

import android.util.Log
import com.zpf.modelsqlite.constant.SQLiteConfig


object Logger {

    internal fun d(msg: String?) {
        if (SQLiteConfig.DEBUG && !msg.isNullOrEmpty()) {
            Log.d(SQLiteConfig.LOG_TAG, msg)
        }
    }

    internal fun i(msg: String?) {
        if (SQLiteConfig.DEBUG && !msg.isNullOrEmpty()) {
            Log.i(SQLiteConfig.LOG_TAG, msg)
        }
    }

    internal fun w(msg: String?) {
        if (SQLiteConfig.DEBUG && !msg.isNullOrEmpty()) {
            Log.w(SQLiteConfig.LOG_TAG, msg)
        }
    }

    internal fun e(msg: String?) {
        if (SQLiteConfig.DEBUG && !msg.isNullOrEmpty()) {
            Log.e(SQLiteConfig.LOG_TAG, msg)
        }
    }
}