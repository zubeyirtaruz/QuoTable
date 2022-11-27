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

}