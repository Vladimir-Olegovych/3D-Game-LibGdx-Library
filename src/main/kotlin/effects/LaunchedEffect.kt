package com.gigapi.effects

import com.gigapi.general.GContext

interface LaunchedEffect {
    fun launch(gContext: GContext)
}