package com.gigapi.storage.json

import com.badlogic.gdx.Gdx
import com.fasterxml.jackson.databind.ObjectMapper

class JsonPreference<P>(
    private val preferenceName: String,
    private val defaultPreference: P
) {
    private val objectMapper = ObjectMapper()

    private fun handle() = Gdx.files.local("$preferenceName.json")

    fun getPreference(): P {
        val handle = handle()
        if (!handle.exists() || handle.length() == 0L) {
            setPreference(defaultPreference)
            return defaultPreference
        }
        return handle.read().use { input ->
            objectMapper.readValue(input, defaultPreference!!::class.java)
        }
    }

    fun setPreference(preference: P) {
        val handle = handle()
        handle.parent().mkdirs()
        handle.write(false).use { output ->
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(output, preference)
        }
    }
}
