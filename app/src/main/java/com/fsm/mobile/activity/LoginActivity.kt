package com.fsm.mobile.activity

import android.annotation.SuppressLint
import android.graphics.Paint
import com.fsm.mobile.R
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.lifecycleScope
import com.fsm.mobile.activity.navigator.ActivityTarget
import com.fsm.mobile.models.Pair
import com.fsm.mobile.network.HttpClient
import com.fsm.mobile.permission.PermissionType
import com.fsm.mobile.service.AndroidActivityService
import com.fsm.mobile.service.AndroidPermissionService
import kotlinx.coroutines.launch

/**
 * The activity for the login screen, allows the user to enter their username and password and login.
 */
class LoginActivity: AppCompatActivity() {
    //Components
    private lateinit var tvUsername: TextView
    private lateinit var tvPassword: TextView
    private lateinit var tvPrompt: TextView
    private lateinit var etUsername: EditText
    private lateinit var etPassword: EditText
    private lateinit var buttonLogin: Button
    private lateinit var tvSettings: TextView

    //Permissions
    private lateinit var androidPermissionService: AndroidPermissionService
    private var hasLocalNetPermission: Boolean = false
    private var waitingForLogin: Boolean = false //Track if a login was attempted before a permission prompt


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        //Force light mode
        AppCompatDelegate.setDefaultNightMode(
            AppCompatDelegate.MODE_NIGHT_NO
        )

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)



        //Initialising the components
        this.tvUsername = findViewById(R.id.tvPassword)
        this.tvPassword = findViewById(R.id.tvUsername)
        this.tvPrompt = findViewById(R.id.tvPrompt)
        this.etUsername = findViewById(R.id.etUsername)
        this.etPassword = findViewById(R.id.etPassword)
        this.buttonLogin = findViewById(R.id.buttonLogin)
        this.tvSettings = findViewById(R.id.tvSettings)
        tvSettings.paintFlags = Paint.UNDERLINE_TEXT_FLAG //underlining the text

        HttpClient.init(this)

        //Ask for necessary permissions
        androidPermissionService = AndroidPermissionService(this)

        //Listeners
        buttonLogin.setOnClickListener {
            Log.i("Self", "Login button pressed")
            Log.i("Self", "Has ACCESS_LOCAL_NETWORK permission: ${this.hasLocalNetPermission}")

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

        tvSettings.setOnClickListener {
            Log.i("Self", "Login Settings button pressed")

            AlertDialog.Builder(this)
                .setTitle("Caution!")
                .setMessage("Editing these settings could cause the app to behave in unexpected ways. " +
                        "Are you sure you want to proceed?")
                .setPositiveButton("Proceed") {dialogue, _ -> //Load the login settings
                    Log.i("Self", "Login Settings Alert Proceed pressed")
                    dialogue.dismiss()
                    val androidActivityService = AndroidActivityService(this)
                    androidActivityService.navigateTo(ActivityTarget.LOGIN_SETTINGS)
                }
                .setNegativeButton("Cancel") {dialogue, _ ->
                    Log.i("Self", "Login Settings Alert Cancel pressed")
                    dialogue.dismiss()
                }
                .show()




        }
    }

    //TODO: Explore the possibility of using custom exceptions and then try catch?
    /**
     * Logs the user in and changes the prompt label's text to give visual feedback to the user if
     * the process fails.
     * @param username username to authenticate
     * @param password password to authenticate
     */
    private suspend fun login(username: String, password: String) {
        val canLogin = HttpClient.login(username, password)

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
    /**
     * Function is called automatically by android after the permission prompt closes.
     * Checks if permission was granted to use the local network and if so automatically logs in the
     * user.
     */
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

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

    /**
     * Changes the text of the prompt label
     * @param text Text to change the prompt label to
     */
    private fun setPrompt(text: String) {
        tvPrompt.text = text
    }
}