package com.zubeyirtaruz.quotable.util

import android.content.Context

class CustomSharedPreferences {

    fun setTimeSharedPreference(context: Context, key: String?, value: Long) {
        val sharedPref = context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)
        val edit = sharedPref.edit()
        edit.putLong(key, value)
        edit.apply()
    }

    fun getTimeSharedPreference(context: Context, key: String?, defaultValue: Long)
        = context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)
            .getLong(key, defaultValue)

    fun setKeySharedPreference(context: Context, key: String?, value: String) {
        val sharedPref = context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)
        val edit = sharedPref.edit()
        edit.putString(key, value)
        edit.apply()
    }

    fun getKeySharedPreference(context: Context, key: String?, defaultValue: String)
            = context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)
        .getString(key, defaultValue)

    fun removeKeySharedPreference(context: Context, key: String?) {
        val sharedPref = context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)
        val edit = sharedPref.edit()
        edit.remove(key)
        edit.apply()
    }



}