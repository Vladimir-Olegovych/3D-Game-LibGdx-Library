package com.gigapi.math.noice.domain

import com.badlogic.gdx.math.Vector2
import com.gigapi.math.noice.NoiceGenerator
import com.gigapi.math.noice.models.NoiseSettings
import com.gigapi.math.vector.IntVector2
import kotlin.math.roundToInt

class DomainWarping2D(
    val noiceGenerator: NoiceGenerator,
    val noiseDomainX: NoiseSettings,
    val noiseDomainY: NoiseSettings,
    val amplitudeX: Int = 20,
    val amplitudeY: Int = 20
) {

    fun generateDomainNoise(x: Int, z: Int, defaultNoiseSettings: NoiseSettings): Float {
        val domainOffset = generateDomainOffset(x, z)
        return noiceGenerator.noice(
            x + domainOffset.x,
            z + domainOffset.y,
            defaultNoiseSettings
        )
    }

    fun generateDomainOffset(x: Int, z: Int): Vector2 {
        val noiseX = noiceGenerator.noice(x.toFloat(), z.toFloat(), noiseDomainX) * amplitudeX
        val noiseY = noiceGenerator.noice(x.toFloat(), z.toFloat(), noiseDomainY) * amplitudeY
        return Vector2(noiseX, noiseY)
    }

    fun generateDomainOffsetInt(x: Int, z: Int): IntVector2 {
        val offset = generateDomainOffset(x, z)
        return IntVector2(offset.x.roundToInt(), offset.y.roundToInt())
    }
}