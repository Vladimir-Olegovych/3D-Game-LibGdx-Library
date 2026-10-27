package com.gigapi.coruntines

import kotlinx.coroutines.*

abstract class DeltaUpdater(
    private val deltaTime: Float,
    dispatcher: CoroutineDispatcher
) {

    protected val lifecycleScope = CoroutineScope(dispatcher + SupervisorJob())

    @Volatile private var step = (deltaTime * 1_000_000_000L).toLong()
    @Volatile private var lastTime = System.nanoTime()
    @Volatile private var timeSinceLastUpdate = 0L
    @Volatile private var isRunning = false

    private var job: Job? = null

    fun getStatus() = isRunning

    abstract fun create()
    abstract fun update(deltaTime: Float)
    abstract fun dispose()

    fun stop(blocking: Boolean = true){
        isRunning = false
        if (blocking) {
            runBlocking { job?.join() }
        } else {
            job?.cancel()
        }
        job = null
        lifecycleScope.cancel()
    }

    fun start(blockingInitialize: Boolean = true) {
        if (isRunning) return
        if (blockingInitialize) {
            runBlocking { lifecycleScope.async { create() }.await() }
        }
        job = lifecycleScope.launch {
            if (!blockingInitialize) create()
            isRunning = true
            lastTime = System.nanoTime()
            while (isRunning) update()
            dispose()
            isRunning = true
        }
    }


    private fun update() {
        lastTime = System.nanoTime().also { timeSinceLastUpdate += it - lastTime }
        if (timeSinceLastUpdate < step) {
            val remain = step - timeSinceLastUpdate
            if (remain >= 1_000_000L) Thread.sleep(remain / 1_000_000L)
        } else {
            do {
                update(deltaTime)
                timeSinceLastUpdate -= step
            } while (timeSinceLastUpdate >= step)
        }
    }
}