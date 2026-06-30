package com.gigapi.storage.json

import com.gigapi.effects.DisposableEffect
import com.gigapi.effects.LaunchedEffect
import com.gigapi.general.Context

class AppConfig<P>(configName: String, default: P): LaunchedEffect, DisposableEffect {

    private val jsonPreference = JsonPreference(configName, default)
    private var value = default

    fun getConfig(): P = value

    override fun launch(context: Context) {
        value = jsonPreference.getPreference()
    }

    override fun dispose() {
        jsonPreference.setPreference(value)
    }
}