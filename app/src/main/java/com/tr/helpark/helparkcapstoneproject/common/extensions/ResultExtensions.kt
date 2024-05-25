package com.tr.helpark.helparkcapstoneproject.common.extensions

import android.util.Log
import tr.com.helpark.core.BuildConfig
import tr.com.helpark.core.util.logE

/**
 * Wraps a result object and logs its exception if any failure happens.
 *
 * @param throwIfDebug will throw the exception if true and the
 * app is in debug mode.
 */
fun <T> Result<T>.logException(throwIfDebug: Boolean = false): Result<T> {
    return onFailure {
        val stackTrace = Log.getStackTraceString(it)
        logE("Exception while performing runCatching block.\n$stackTrace")
        if (throwIfDebug && BuildConfig.DEBUG) {
            // Exit the task and make sure the program quits.
            Thread {
                throw it
            }.start()
        }
    }
}