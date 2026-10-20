package com.fsm.mobile

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.fsm.mobile.network.HttpClient
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

class MainActivity : AppCompatActivity() {
    private lateinit var tvStatusLabel: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // Initialising the components
        tvStatusLabel = findViewById(R.id.tvStatusLabel)
        HttpClient.initialise(this)

        tvStatusLabel.text = "Disconnected"

        val url = HttpClient.SERVER_URL
        val request = Request.Builder()
            .url("$url/checkConnection")
            .get().build()

        HttpClient.sendRequest(request, object : Callback {
            override fun onResponse(call: Call, response: Response) {
                runOnUiThread {
                    if (response.code == 200) {
                        tvStatusLabel.text = "Connected"
                    } else {
                        tvStatusLabel.text = "Response received - error occurred"
                    }
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    println("Error: $e")
                    tvStatusLabel.text = "Connection failed"
                }
            }
        })
    }
}
