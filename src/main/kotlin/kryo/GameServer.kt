package com.gigapi.kryo

import com.esotericsoftware.kryo.Kryo
import com.esotericsoftware.kryonet.Listener
import com.esotericsoftware.kryonet.Server

class GameServer(private val bufferSize: Int) {

    private var server: Server? = null

    fun prepare() {
        server = Server(bufferSize, bufferSize)
    }

    fun addListener(listener: Listener) {
        server?.addListener(listener)
    }

    fun removeListener(listener: Listener) {
        server?.removeListener(listener)
    }

    fun start(port: Int, custom: (Kryo) -> Unit){
        val server = server ?: return
        val kryo = server.kryo
        custom.invoke(kryo)

        //server.bind(InetSocketAddress("0.0.0.0", port), InetSocketAddress("0.0.0.0", port))
        server.bind(port, port)
        server.start()

        this.server = server
    }


    fun dispose() {
        server?.dispose()
        server = null
    }
}