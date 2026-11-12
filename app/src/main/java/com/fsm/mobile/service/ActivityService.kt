package com.fsm.mobile.service

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat.startActivity
import com.fsm.mobile.activity.HomeActivity
import com.fsm.mobile.activity.LoginActivity
import com.fsm.mobile.activity.navigator.ActivityNavigator
import com.fsm.mobile.activity.navigator.ActivityTarget

//Since we are implementing an interface, we cant use AppCompatActivity(), so the context is passed
//as a parameter to the constructor
class ActivityService(private val context: Context) : ActivityNavigator {

    override fun navigateTo(target: ActivityTarget) {
        //Equivalent to switch-case in Java
        val intent = when (target) {
            ActivityTarget.LOGIN -> Intent(context, LoginActivity::class.java)
            ActivityTarget.HOME -> Intent(context, HomeActivity::class.java)
        }

        context.startActivity(intent)
    }
}