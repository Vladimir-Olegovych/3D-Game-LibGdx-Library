package com.gigapi.eventbus

import java.util.concurrent.ConcurrentLinkedQueue
import kotlin.reflect.KClass

class EventBus: EventHandler() {

    private val needProcessedEvents = ConcurrentLinkedQueue<Pair<Any, KClass<*>?>>()

    fun getEventsSwap(): ConcurrentLinkedQueue<Pair<Any, KClass<*>?>> {
        val events = ConcurrentLinkedQueue(needProcessedEvents)
        needProcessedEvents.clear()
        return events
    }

    fun process() {
        while (true) {
            val pair = needProcessedEvents.poll() ?: break
            applyEvent(pair.first, pair.second)
        }
    }

    fun processSwap(swap: ConcurrentLinkedQueue<Pair<Any, KClass<*>?>>) {
        while (true) {
            val pair = swap.poll() ?: break
            applyEvent(pair.first, pair.second)
        }
    }

    fun processSwap(swap: ConcurrentLinkedQueue<Pair<Any, KClass<*>?>>, type: Class<*>) {
        val iterator = swap.iterator()
        while (iterator.hasNext()) {
            val pair = iterator.next()
            if (pair.second?.java == type || pair.first::class.java == type) {
                applyEvent(pair.first, pair.second)
                iterator.remove()
            }
        }
    }

    fun sendEventNow(event: Any, customType: KClass<*>? = null) {
        applyEvent(event, customType)
    }

    fun sendEvent(event: Any, customType: KClass<*>? = null) {
        needProcessedEvents.add(event to customType)
    }

    override fun clear() {
        super.clear()
        needProcessedEvents.clear()
    }
}