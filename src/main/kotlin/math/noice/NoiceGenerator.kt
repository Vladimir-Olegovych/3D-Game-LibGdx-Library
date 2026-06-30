package com.gigapi.math.noice

import com.gigapi.math.noice.models.NoiseSettings

interface NoiceGenerator {
    fun noice(x: Int, y: Int, noiceSettings: NoiseSettings): Float
    fun noice(x: Float, y: Float, noiceSettings: NoiseSettings): Float
    fun noice(x: Int, y: Int, z: Int, noiceSettings: NoiseSettings): Float
    fun noice(x: Float, y: Float, z: Float, noiceSettings: NoiseSettings): Float
}