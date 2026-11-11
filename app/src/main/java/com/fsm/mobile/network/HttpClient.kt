package com.fsm.mobile.network

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.util.Log
import com.fsm.mobile.models.Credentials
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
    private val client = OkHttpClient()
    private val callTimeout = 15//Maximum time to wait for the entire client-server communication process (seconds)
    private lateinit var SERVER_URL: String

    fun init(context: Context) {
        val prefs = context.getSharedPreferences("com.fsm.mobile", MODE_PRIVATE)
        val ip = prefs.getString("server-ip", null) //p1 is the default value
        val port = prefs.getString("server-port", null)
        this.SERVER_URL = "http://$ip:$port"
        Log.i("Self", "Server URL: ${this.SERVER_URL}")

        //Specifying the properties of the client
        client.newBuilder()
            //maximum time to wait for the entire client-server communication process
            .callTimeout(callTimeout.toLong(), TimeUnit.SECONDS)
            .build()
    }

    fun sendRequest(request: Request, callback: Callback) {
        client.newCall(request).enqueue(callback)
    }

    //TODO: Split this up into more classes and functions when it is fully working
    //TODO: Write login function
    fun login(username: String, password: String) {
        //Serialising username and password to json
        val credentials = Credentials(username, password)
        val jsonCredentials = Json.encodeToString(credentials)
        //Constructing the request body
        val bodyType = "application/json".toMediaType()
        val body = jsonCredentials.toRequestBody(bodyType)
        //Constructing the request
        val request = Request.Builder()
            .url("${this.SERVER_URL}/models/login")
            .post(body)
            .build()

        this.sendRequest(request, object: Callback {
            //Checks the status code of the response
            override fun onResponse(call: Call, response: Response) {
                if (response.code == 202) { //Login successful
                    //TODO: Load Main View Activity
                    Log.i("Self", "Login successful")
                } else if (response.code == 401) { //Login unsuccessful
                    //TODO: Provide visual feedback to the user to update the credentials
                    Log.i("Self", "Login unsuccessful")
                } else { //Error has occurred due to unexpected server response
                    //TODO: Provide visual feedback to the user to that an error has occurred
                    Log.i("Self", "Unexpected server response during login")
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                //TODO: Provide visual feedback to the user that an error has occurred
                Log.i("Self", "No response sent during login")
            }
        })
    }

}