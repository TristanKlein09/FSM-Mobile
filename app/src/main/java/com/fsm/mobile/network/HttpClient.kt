package com.fsm.mobile.network

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.util.Log
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import java.util.concurrent.TimeUnit

object HttpClient {
    private val client = OkHttpClient()
    private val callTimeout = 15//Maximum time to wait for the entire client-server communication process (seconds)
    private lateinit var SERVER_URL: String


    //Need a util class for getSharedPreferences to work in the init block as it doesnt have access to context

    fun init(context: Context) {
        val prefs = context.getSharedPreferences("com.fsm.mobile", MODE_PRIVATE)
        val ip = prefs.getString("server-ip", null) //p1 is the default value
        val port = prefs.getString("server-port", null)
        this.SERVER_URL = "http://$ip:$port"
        Log.i("Self", "Server URL: ${this.SERVER_URL}")

        //Specifying the properties of the client
        client.newBuilder()
            .callTimeout(callTimeout.toLong(), TimeUnit.SECONDS) //maximum time to wait for the entire client-server communication process
            .build()
    }

    fun sendRequest(request: Request, callback: Callback) {
        client.newCall(request).enqueue(callback)
    }

    //TODO: Write login function
    fun login(username: String, password: String) {

    }

}