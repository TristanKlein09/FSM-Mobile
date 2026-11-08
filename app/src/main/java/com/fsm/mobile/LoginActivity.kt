package com.fsm.mobile

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.fsm.mobile.network.HttpClient

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

        //Listeners
        buttonLogin.setOnClickListener {
            HttpClient.login(etUsername.text.toString(), etPassword.text.toString())
        }

    }

    fun setPromptError(text: String) {
        tvPrompt.text = text

        val errorColour = ContextCompat.getColor(this, R.color.error)
        tvPrompt.setTextColor(errorColour)
    }

    fun switchToHomeActivity() {
        val intent = Intent(this@LoginActivity, HomeActivity::class.java)
        startActivity(intent)
    }

}