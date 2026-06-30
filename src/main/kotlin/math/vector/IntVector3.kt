package com.gigapi.math.vector

import com.badlogic.gdx.math.Vector3
import kotlin.math.roundToInt
import kotlin.math.sqrt

data class IntVector3(var x: Int = 0, var y: Int = 0, var z: Int = 0) {
    companion object {
        fun dst(v1: IntVector3, v2: IntVector3): Float {
            val a = v2.x - v1.x
            val b = v2.y - v1.y
            val c = v2.z - v1.z
            return sqrt((a * a + b * b + c * c).toDouble()).toFloat()
        }

        fun roundToInt(vector3: Vector3): IntVector3 {
            return IntVector3(
                vector3.x.roundToInt(),
                vector3.y.roundToInt(),
                vector3.z.roundToInt(),
            )
        }
    }
}