package com.gigapi.kryo

import com.esotericsoftware.kryo.Kryo
import com.esotericsoftware.kryonet.Client
import com.esotericsoftware.kryonet.Listener
import com.gigapi.effects.DisposableEffect

class GameClient(private val bufferSize: Int): DisposableEffect {

    private var client: Client? = Client(bufferSize, bufferSize)

    fun prepare() {
        client = Client(bufferSize, bufferSize)
    }

    fun addListener(listener: Listener) {
        client?.addListener(listener)
    }

    fun removeListener(listener: Listener) {
        client?.removeListener(listener)
    }

    fun sendTCP(obj: Any) {
        client?.sendTCP(obj)
    }

    fun sendUDP(obj: Any) {
        client?.sendUDP(obj)
    }

    fun start(address: String, port: Int,
              custom: (Kryo) -> Unit,
              onConnectionFailed: () -> Unit
    ){
        val client = client ?: return
        val kryo = client.kryo
        custom.invoke(kryo)
        client.start()

        try {
            client.connect(TIME_OUT, address, port, port)
        } catch (e: Throwable) {
            this.client?.dispose()
            this.client = null
            onConnectionFailed.invoke()
        }
    }

    override fun dispose() {
        client?.dispose()
        client = null
    }

    companion object {
        const val TIME_OUT = 5000
    }
}