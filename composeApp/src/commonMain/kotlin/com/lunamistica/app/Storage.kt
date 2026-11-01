package com.lunamistica.app

/** Almacenamiento clave-valor persistente en el dispositivo. */
expect object KeyValueStore {
    fun getString(key: String): String?
    fun putString(key: String, value: String)
    fun remove(key: String)
}
