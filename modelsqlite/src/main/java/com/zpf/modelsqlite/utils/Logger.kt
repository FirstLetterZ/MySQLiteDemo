package com.zpf.modelsqlite.utils

import java.util.logging.Level
import java.util.logging.Logger

object Logger {
    var realLogger: Logger? = null

    internal fun i(msg: String?) {
        if (msg.isNullOrEmpty()) {
            return
        }
        logout(Level.INFO, msg)
    }

    internal fun w(msg: String?) {
        if (msg.isNullOrEmpty()) {
            return
        }
        logout(Level.WARNING, msg)
    }

    private fun logout(level: Level, msg: String) {
        realLogger?.log(level, msg)
    }

}