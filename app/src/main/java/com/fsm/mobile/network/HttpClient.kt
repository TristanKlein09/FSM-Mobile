package com.fsm.mobile.network

import android.content.Context
import com.fsm.mobile.Util
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request

object HttpClient {
    private val client = OkHttpClient()
    lateinit var SERVER_URL: String
    //val SERVER_URL: String = "http://192.168.0.181:9999"

    fun initialise(context: Context) {
        val util = Util(context.applicationContext)
        val serverIP = util.retrieveProperty("server.ip")
        val serverPort = util.retrieveProperty("server.port")
        this.SERVER_URL = "http://$serverIP:$serverPort"
        println(SERVER_URL)
    }

    fun sendRequest(request: Request, callback: Callback) {
        client.newCall(request).enqueue(callback)
    }
}
