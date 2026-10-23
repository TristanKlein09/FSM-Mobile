package com.fsm.mobile.network

import android.content.Context
import com.fsm.mobile.Util
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.MediaType.Companion.toMediaType
import java.io.IOException

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

    //TODO: Test the functions
    //TODO: Split up into a createLoginRequest function as well - same like the scheduler
    fun login(username: String, password: String) {
        val rawBody = """
            {
                "username": "$username",
                "password": "$password"
            }
        """.trimIndent()

        //If doesn't work might need "appliction/json; charset=utf-8"
        val bodyType = "application/json".toMediaType()
        val body = rawBody.toRequestBody(bodyType)

        val request = Request.Builder()
            .url("$SERVER_URL/models/login")
            .post(body)
            .build()

        this.sendRequest(request, object : Callback {
            override fun onResponse(call: Call, response: Response) {
                //Check the nature of the response
                //401 unauthorised, 202 accepted

                if (response.code == 202) { //Login successful
                    //TODO: Load in the Main Calendar View Activity

                } else if (response.code == 401) { //Login unsuccessful
                    //TODO: Give feedback to the user to reprompt the credentials

                } else { //Error has occured due to unexpected server response
                    //TODO: Give feedback to the user to try again and that an error has occurred

                }

            }

            //No response has been sent, error has occured
            override fun onFailure(call: Call, e: IOException) {
                println("No response sent")
                //TODO: This is the place where we can call functions to change UI to show user that a server error has occurred
            }
        })
    }
}
