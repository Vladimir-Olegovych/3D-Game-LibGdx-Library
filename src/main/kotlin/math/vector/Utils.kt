package com.gigapi.math.vector

import com.badlogic.gdx.math.Vector3

fun IntVector3.roundToFloat(): Vector3 {
    return Vector3(
        this.x.toFloat(),
        this.y.toFloat(),
        this.z.toFloat(),
    )
}