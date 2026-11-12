package com.fsm.mobile.network

import com.fsm.mobile.models.Credentials
import com.fsm.mobile.models.Pair

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import okio.IOException
import java.util.concurrent.TimeUnit

object HttpClient {
    private var client = OkHttpClient()
    private val callTimeout = 15//Maximum time to wait for the entire client-server communication process (seconds)
    private lateinit var SERVER_URL: String

    fun init(context: Context) {
        val prefs = context.getSharedPreferences("com.fsm.mobile", MODE_PRIVATE)
        val ip = prefs.getString("server-ip", null) //p1 is the default value
        val port = prefs.getString("server-port", null)
        this.SERVER_URL = "http://$ip:$port"
        Log.i("Self", "Server URL: ${this.SERVER_URL}")

        //Specifying the properties of the client
        client = OkHttpClient.Builder()
            //maximum time to wait for the entire client-server communication process
            .callTimeout(callTimeout.toLong(), TimeUnit.SECONDS)
            .build()
    }

    fun sendRequest(request: Request, callback: Callback) {
        client.newCall(request).enqueue(callback)
    }


    //TODO: Split this up into more classes and functions when it is fully working (might already be okay)
    //TODO: Write unit tests for this
    //Marked as suspend so it can be called from coroutines (background threads) without blocking
    /**
     * Sends a login request to the server. If the response is 202, it loads the home activity view.
     * @param username
     * @param password
     * @return A Pair<Boolean, String>, where the Boolean states if the login was successful, and the
     * String states the nature of the problem (if any)
     */
    suspend fun login(username: String, password: String): Pair<Boolean, String> = withContext(
        Dispatchers.IO){
        var success = Pair<Boolean, String>(false, "") //Initialising the return variable
        //Serialising username and password to json
        val credentials = Credentials(username, password)
        val jsonCredentials = Json.encodeToString(credentials)
        //Constructing the request body
        val bodyType = "application/json".toMediaType()
        val body = jsonCredentials.toRequestBody(bodyType)
        //Constructing the request
        val request = Request.Builder()
            .url("${SERVER_URL}/models/login")
            .post(body)
            .header("Accept-encoding", "text/plain")
            .header("Content-Type", "application/json")
            .build()


        try {
            val response = client.newCall(request).execute()
            when (response.code) {
                202 -> Pair(true, "Success")
                401 -> Pair(false, "Incorrect credentials")
                else -> Pair(false, "Unexpected server error occurred")
            }
        } catch (e: IOException) {
            Pair(false, "No response from server")
        }

    }

}