package com.fsm.mobile.activity

import androidx.appcompat.app.AppCompatActivity
import com.fsm.mobile.R
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import com.fsm.mobile.network.HttpClient
import androidx.core.content.edit

class LoginSettingsActivity: AppCompatActivity() {
    private lateinit var tvTitle: TextView
    private lateinit var tvIP: TextView
    private lateinit var tvPort: TextView
    private lateinit var etIP: EditText
    private lateinit var etPort: EditText
    private lateinit var buttonSave: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login_settings)

        //Initialising components
        this.tvTitle = findViewById(R.id.tvTitle)
        this.tvIP = findViewById(R.id.tvIP)
        this.tvPort = findViewById(R.id.tvPort)
        this.etIP = findViewById(R.id.etIP)
        this.etPort = findViewById(R.id.etPort)
        this.buttonSave = findViewById(R.id.buttonSave)

        val prefs = getSharedPreferences("com.fsm.mobile", MODE_PRIVATE)
        this.etIP.setText(prefs.getString("server-ip", ""))
        this.etPort.setText(prefs.getString("server-port", ""))

        //Get the value of the Edit texts and set them to the SharedPreferences
        this.buttonSave.setOnClickListener {
            Log.i("Self", "Login Settings Button Pressed")

            val prefs = getSharedPreferences("com.fsm.mobile", MODE_PRIVATE)
            prefs.edit {
                putString("server-ip", etIP.text.toString())
                    .putString("server-port", etPort.text.toString())
            }

            HttpClient.updateServerURL(this)

            Toast.makeText(this, "Settings applied", Toast.LENGTH_SHORT).show()
        }

    }
}