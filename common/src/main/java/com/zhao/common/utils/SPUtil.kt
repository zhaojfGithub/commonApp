@file:Suppress("UNCHECKED_CAST")

package com.zhao.common.utils

import android.content.Context

/**
 * @author zjf
 * @date 2023/9/15
 * @effect SharedPreferences 工具类
 */
object SPUtil {

    private const val SP_APP = "sp_aiyouwei"

    @JvmOverloads
    fun <T> getSpValue(
        filename: String = SP_APP,
        key: String,
        defaultVal: T,
        context: Context
    ): T {
        val sp = context.getSharedPreferences(filename, Context.MODE_PRIVATE)
        return when (defaultVal) {
            is Boolean -> sp.getBoolean(key, defaultVal) as T
            is String -> sp.getString(key, defaultVal) as T
            is Int -> sp.getInt(key, defaultVal) as T
            is Long -> sp.getLong(key, defaultVal) as T
            is Float -> sp.getFloat(key, defaultVal) as T
            is Set<*> -> sp.getStringSet(key, defaultVal as Set<String>) as T
            else -> throw IllegalArgumentException("Unrecognized default value $defaultVal")
        }
    }

    @JvmOverloads
    fun <T> putSpValue(
        filename: String = SP_APP,
        key: String,
        value: T,
        context: Context
    ) {
        val editor = context.getSharedPreferences(filename, Context.MODE_PRIVATE).edit()
        when (value) {
            is Boolean -> editor.putBoolean(key, value)
            is String -> editor.putString(key, value)
            is Int -> editor.putInt(key, value)
            is Long -> editor.putLong(key, value)
            is Float -> editor.putFloat(key, value)
            is Set<*> -> editor.putStringSet(key, value as Set<String>)
            else -> throw UnsupportedOperationException("Unrecognized value $value")
        }
        editor.apply()
    }

    @JvmOverloads
    fun removeSpValue(
        filename: String = SP_APP,
        key: String,
        context: Context
    ) {
        context.getSharedPreferences(filename, Context.MODE_PRIVATE)
            .edit()
            .remove(key)
            .apply()
    }

    @JvmOverloads
    fun clearSpValue(filename: String = SP_APP, context: Context) {
        context.getSharedPreferences(filename, Context.MODE_PRIVATE)
            .edit()
            .clear()
            .apply()
    }
}