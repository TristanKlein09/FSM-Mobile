package com.fsm.mobile

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {
    private lateinit var tvUsername: TextView
    private lateinit var tvPassword: TextView
    private lateinit var tvPrompt: TextView
    private lateinit var etUsername: EditText
    private lateinit var etPassword: EditText
    private lateinit var buttonLogin: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)

        //Initialising the components
        tvUsername = findViewById(R.id.tvPassword)
        tvPassword = findViewById(R.id.tvUsername)
        tvPrompt = findViewById(R.id.tvPrompt)
        etUsername = findViewById(R.id.etUsername)
        etPassword = findViewById(R.id.etPassword)
        buttonLogin = findViewById(R.id.buttonLogin)

    }

}