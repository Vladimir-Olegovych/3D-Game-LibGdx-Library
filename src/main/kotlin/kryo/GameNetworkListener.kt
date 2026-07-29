package com.gigapi.kryo

import com.esotericsoftware.kryonet.Connection

interface GameNetworkListener {
    fun onError(e: Throwable) {}
    fun onConnected(connection: Connection){}
    fun onDisconnected(connection: Connection){}
    fun onReceive(connection: Connection, value: Any){}
}