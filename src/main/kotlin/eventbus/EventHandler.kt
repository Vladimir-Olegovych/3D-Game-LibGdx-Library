package com.gigapi.eventbus

import com.gigapi.eventbus.annotation.BusEvent
import com.gigapi.eventbus.annotation.EventType
import java.lang.reflect.Method
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.CopyOnWriteArrayList
import kotlin.reflect.KClass

open class EventHandler {

    protected val handlers = CopyOnWriteArrayList<Any>()
    protected val handlerCache = mutableMapOf<Class<*>, List<MethodInfo>>()

    protected data class MethodInfo(
        val method: Method,
        val eventType: KClass<*>?,
        val parameterType: Class<*>
    )

    fun registerHandler(obj: Any) {
        handlers.add(obj)
        cacheHandlerMethods(obj)
    }

    fun removeHandler(obj: Any) {
        handlers.remove(obj)
        handlerCache.remove(obj::class.java)
    }

    open fun clear() {
        handlers.clear()
        handlerCache.clear()
    }

    protected fun applyEvent(event: Any, customType: KClass<*>?) {
        for (handler in handlers) {
            val methods = handlerCache[handler::class.java] ?: continue
            for (methodInfo in methods) {
                when {
                    methodInfo.eventType != null -> {
                        if (methodInfo.eventType == customType) {
                            methodInfo.method.invoke(handler, event)
                        }
                    }
                    methodInfo.parameterType == event::class.java -> {
                        methodInfo.method.invoke(handler, event)
                    }
                }
            }
        }
    }

    protected fun cacheHandlerMethods(handler: Any) {
        val methods = handler::class.java.methods
        val methodInfos = methods.mapNotNull { method ->
            if (!method.isAnnotationPresent(BusEvent::class.java)) return@mapNotNull null

            val typeAnnotation = method.getAnnotation(EventType::class.java)
            val params = method.parameterTypes

            if (params.size != 1) return@mapNotNull null

            MethodInfo(
                method = method,
                eventType = typeAnnotation?.customType,
                parameterType = params[0]
            )
        }
        handlerCache[handler::class.java] = methodInfos
    }
}