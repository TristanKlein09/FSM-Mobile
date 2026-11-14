package com.fsm.mobile.activity

import com.fsm.mobile.R
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.fsm.mobile.activity.navigator.ActivityTarget
import com.fsm.mobile.models.Pair
import com.fsm.mobile.network.HttpClient
import com.fsm.mobile.permission.PermissionService
import com.fsm.mobile.permission.PermissionType
import com.fsm.mobile.service.AndroidActivityService
import com.fsm.mobile.service.AndroidPermissionService
import kotlinx.coroutines.launch

class LoginActivity: AppCompatActivity() {
    //Components
    private lateinit var tvUsername: TextView
    private lateinit var tvPassword: TextView
    private lateinit var tvPrompt: TextView
    private lateinit var etUsername: EditText
    private lateinit var etPassword: EditText
    private lateinit var buttonLogin: Button

    //Permissions
    private lateinit var androidPermissionService: AndroidPermissionService
    private var hasLocalNetPermission: Boolean = false
    private var waitingForLogin: Boolean = false //Track if a login was attempted before a permission prompt


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

        //Ask for necessary permissions
        androidPermissionService = AndroidPermissionService(this)
        this.hasLocalNetPermission = androidPermissionService.requestPermission(PermissionType.ACCESS_LOCAL_NETWORK)
        Log.i("Self", "Has ACCESS_LOCAL_NETWORK permission: ${this.hasLocalNetPermission}")



        //Listeners
        buttonLogin.setOnClickListener {
            Log.i("Self", "Login button pressed")

            //Coroutine
//            lifecycleScope.launch {
//                if (hasLocalNetPermission) {
//                    login(etUsername.text.toString(), etPassword.text.toString())
//                } else {
//                    //Reprompt for permission again
//                    hasLocalNetPermission = androidPermissionService.requestPermission(PermissionType.ACCESS_LOCAL_NETWORK)
//
//                    if (hasLocalNetPermission) {
//                        login(etUsername.text.toString(), etPassword.text.toString())
//                    }
//                }
//
//            }

            if (hasLocalNetPermission) { //Has permission
                lifecycleScope.launch{
                    login(etUsername.text.toString(), etPassword.text.toString())
                }
            } else { //No permission, so reprompt the user
                //Once the user responds we know to log the user in
                waitingForLogin = true
                androidPermissionService.requestPermission(PermissionType.ACCESS_LOCAL_NETWORK)
            }

        }
    }

    //TODO: Explore the possibility of using custom exceptions and then try catch?
    private suspend fun login(username: String, password: String) {
        val canLogin = HttpClient.login(this, this, username, password)

        when(canLogin) {
            //Login is successful
            Pair(true, "Success") -> {
                val androidActivityService = AndroidActivityService(this)
                androidActivityService.navigateTo(ActivityTarget.HOME)
                Log.i("Self", "Login successful")
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

    //This function is called automatically by android after the permission dialogue closes
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        //TODO: Check if this can be replaces with ContextCompat.checkSelfPermission
        if (grantResults.isNotEmpty() && grantResults[0] == android.content.pm.PackageManager.PERMISSION_GRANTED) {
            this.hasLocalNetPermission = true

            //Log in the user if the button was pressed right before the permission prompt
            if (waitingForLogin) {
                waitingForLogin = false
                lifecycleScope.launch {
                    login(etUsername.text.toString(), etPassword.text.toString())
                }
            } else {
                hasLocalNetPermission = false
                waitingForLogin = false
            }
        }
    }

    private fun setPrompt(text: String) {
        tvPrompt.text = text;
    }

}