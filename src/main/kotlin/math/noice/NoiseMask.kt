package com.gigapi.math.noice

import com.gigapi.math.noice.models.NoiseSettings

interface SpawnMask {
    fun weight(gen: NoiceGenerator, x: Int, z: Int, salt: Int): Float

    companion object {
        /**
         * Пятна по шуму: ниже [min] шанс 0, выше [max] полный, между ними плавный край.
         * [signed] = true, если твой noice() возвращает -1..1 (иначе ждём 0..1).
         */
        fun noise(settings: NoiseSettings, min: Float = 0.5f, max: Float = 0.6f, signed: Boolean = false): SpawnMask =
            NoiseMask(settings, min, max, signed)
    }
}

private class NoiseMask(
    private val settings: NoiseSettings,
    private val min: Float,
    private val max: Float,
    private val signed: Boolean
) : SpawnMask {
    override fun weight(gen: NoiceGenerator, x: Int, z: Int, salt: Int): Float {
        // у каждой структуры свой сдвиг по шуму, чтобы пятна разных структур не совпадали
        val v = gen.noice(x + (salt and 0x3FFF), z + ((salt ushr 14) and 0x3FFF), settings)
        val n = if (signed) v * 0.5f + 0.5f else v
        if (max <= min) return if (n >= min) 1f else 0f
        val t = ((n - min) / (max - min)).coerceIn(0f, 1f)
        return t * t * (3f - 2f * t)
    }
}