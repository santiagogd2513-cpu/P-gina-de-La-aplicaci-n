package com.lunamistica.app

import java.util.prefs.Preferences

actual object KeyValueStore {
    private val prefs: Preferences = Preferences.userRoot().node("lunamistica")

    actual fun getString(key: String): String? = prefs.get(key, null)

    actual fun putString(key: String, value: String) {
        prefs.put(key, value)
        prefs.flush()
    }

    actual fun remove(key: String) {
        prefs.remove(key)
        prefs.flush()
    }
}
