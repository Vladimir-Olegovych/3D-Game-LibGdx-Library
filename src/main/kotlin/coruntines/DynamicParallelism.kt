package com.gigapi.coruntines

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class DynamicParallelism(private var maxConcurrency: Int) {
    private val mutex = Mutex()
    private val tokens = Channel<Unit>(capacity = maxConcurrency)

    init {
        repeat(maxConcurrency) { tokens.trySend(Unit) }
    }

    suspend fun setMaxConcurrency(newMax: Int) {
        mutex.withLock {
            val delta = newMax - maxConcurrency
            when {
                delta > 0 -> repeat(delta) { tokens.send(Unit) }
                delta < 0 -> repeat(-delta) { tokens.receive() }
            }
            maxConcurrency = newMax
        }
    }

    suspend fun <T> withPermit(block: suspend () -> T): T {
        tokens.receive()
        return try {
            block()
        } finally {
            tokens.send(Unit)
        }
    }
}