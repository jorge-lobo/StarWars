package com.example.starwarsjunior.data.personage

class DataCache {
    private val dataMap = mutableMapOf<String, Any>()

    fun put(key: String, data: Any) {
        dataMap[key] = data
    }

    fun get(key: String): Any? {
        return dataMap[key]
    }
}