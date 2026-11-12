package com.fsm.mobile.activity

import com.fsm.mobile.R
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.fsm.mobile.activity.navigator.ActivityTarget
import com.fsm.mobile.models.Pair
import com.fsm.mobile.network.HttpClient
import com.fsm.mobile.service.ActivityService
import kotlinx.coroutines.launch

class LoginActivity: AppCompatActivity() {
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

        //Use if you want to change value in shared preferences
        val prefs = getSharedPreferences("com.fsm.mobile", MODE_PRIVATE)
        prefs.edit().putString("server-ip", "192.168.0.181").putString("server-port", "9999").apply()

        HttpClient.init(this)

        //Listeners
        buttonLogin.setOnClickListener {
            Log.i("Self", "Login button pressed")

            //Coroutine
            lifecycleScope.launch {
                login(etUsername.text.toString(), etPassword.text.toString())
            }

        }
    }

    //TODO: Explore the possibility of using custom exceptions and then try catch?
    private suspend fun login(username: String, password: String) {
        val canLogin = HttpClient.login(username, password)

        when(canLogin) {
            //Login is successful
            Pair(true, "Success") -> {
                val activityService = ActivityService(this)
                activityService.navigateTo(ActivityTarget.HOME)
                Log.i("Self", "asdasd")
            }
            Pair(false, "Incorrect credentials") -> {
                setPrompt("Incorrect credentials, try again")
            }
            Pair(false, "Unexpected server error occurred") -> {
                setPrompt("Unexpected server error occurred")
            }
            Pair(false, "No response from server") -> {
                setPrompt("No response from server")
            }
            else -> {
                setPrompt("Unexpected error occurred")
            }
        }

    }

    fun setPrompt(text: String) {
        tvPrompt.text = text;
    }

}