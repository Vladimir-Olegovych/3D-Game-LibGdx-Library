package com.gigapi.general

import com.badlogic.gdx.utils.Disposable
import com.gigapi.effects.DisposableEffect
import com.gigapi.effects.LaunchedEffect
import java.util.concurrent.ConcurrentHashMap
import kotlin.collections.component2
import kotlin.collections.set

class Context: DisposableEffect {

    val objectMap = ConcurrentHashMap<IdentityObjectKey, IdentityObjectValue>()

    fun removeContext(context: Context) {
        context.objectMap.forEach { (key, value) ->
            this@Context.objectMap.remove(key, value)
        }
    }

    fun addContext(context: Context) {
        context.objectMap.forEach { (key, value) ->
            this@Context.objectMap[key] = IdentityObjectValue(
                anObject = value.anObject,
                launched = value.launched,
                fromOtherContext = true
            )
        }
    }

    inline fun <reified T>getObject(): T {
        val key = IdentityObjectKey(
            objectClass = T::class.java
        )
        val mapObj = objectMap[key]!!
        val obj = mapObj.anObject as T
        if(obj is LaunchedEffect && !mapObj.launched && !mapObj.fromOtherContext) {
            mapObj.launched = true
            obj.launch(this)
        }
        return obj
    }

    inline fun <reified T>getObject(customKey: String): T {
        val key = IdentityObjectKey(
            objectClass = T::class.java,
            customKey = customKey
        )
        val mapObj = objectMap[key]!!
        val obj = mapObj.anObject as T
        if(obj is LaunchedEffect && !mapObj.launched && !mapObj.fromOtherContext) {
            mapObj.launched = true
            obj.launch(this)
        }
        return obj
    }

    inline fun <reified T>setObject(anObject: T) {
        val key = IdentityObjectKey(objectClass = T::class.java)
        objectMap[key] = IdentityObjectValue(anObject as Any)
    }

    inline fun <reified T>setObject(customKey: String, anObject: T) {
        val key = IdentityObjectKey(objectClass = T::class.java, customKey = customKey)
        objectMap[key] = IdentityObjectValue(anObject as Any)
    }


    override fun dispose() {
        val iterator = objectMap.iterator()
        while (iterator.hasNext()) {
            val entry = iterator.next()
            val value = entry.value
            if(!value.fromOtherContext && value.anObject is Disposable) value.anObject.dispose()
            iterator.remove()
        }
    }

    fun launch() {
        val iterator = objectMap.iterator()
        while (iterator.hasNext()) {
            val entry = iterator.next()
            val value = entry.value
            if(value.fromOtherContext) continue
            if(value.anObject is LaunchedEffect && !value.launched) {
                value.anObject.launch(this)
                value.launched = true
            }
        }
    }

    data class IdentityObjectKey(
        val objectClass: Class<*>,
        val customKey: String? = null
    )

    data class IdentityObjectValue(
        val anObject: Any,
        var launched: Boolean = false,
        var fromOtherContext: Boolean = false
    )
}