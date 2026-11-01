package com.lunamistica.app

import android.content.Context
import android.content.SharedPreferences

actual object KeyValueStore {
    private lateinit var prefs: SharedPreferences

    /** Se llama desde MainActivity antes de mostrar la interfaz. */
    fun init(context: Context) {
        if (!::prefs.isInitialized) {
            prefs = context.applicationContext.getSharedPreferences("luna_mistica", Context.MODE_PRIVATE)
        }
    }

    actual fun getString(key: String): String? = prefs.getString(key, null)

    actual fun putString(key: String, value: String) {
        prefs.edit().putString(key, value).apply()
    }

    actual fun remove(key: String) {
        prefs.edit().remove(key).apply()
    }
}
