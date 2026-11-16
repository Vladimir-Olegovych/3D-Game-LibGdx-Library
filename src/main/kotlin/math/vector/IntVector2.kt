package com.gigapi.math.vector

import com.badlogic.gdx.math.Vector2
import kotlin.math.sqrt
import kotlin.math.roundToInt

data class IntVector2(var x: Int = 0, var y: Int = 0) {

    fun add(intVector2: IntVector2): IntVector2 {
        return IntVector2(
            x = this.x + intVector2.x,
            y = this.y + intVector2.y
        )
    }

    companion object {
        fun dst(v1: IntVector2, v2: IntVector2): Float {
            val a = v2.x - v1.x
            val b = v2.y - v1.y
            return sqrt((a * a + b * b).toDouble()).toFloat()
        }

        fun roundToInt(vector2: Vector2): IntVector2 {
            return IntVector2(
                vector2.x.roundToInt(),
                vector2.y.roundToInt()
            )
        }
    }
}