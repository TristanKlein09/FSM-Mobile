package com.fsm.mobile.service

import android.content.Context
import android.content.Intent
import com.fsm.mobile.activity.HomeActivity
import com.fsm.mobile.activity.LoginActivity
import com.fsm.mobile.activity.navigator.ActivityService
import com.fsm.mobile.activity.navigator.ActivityTarget

//Since we are implementing an interface, we cant use AppCompatActivity(), so the context is passed as a parameter to the constructor
/**
 * Implementation of [ActivityService]. Responsible for global management of activities
 */
class AndroidActivityService(private val context: Context): ActivityService {

    /**
     * Changes the current activity to the specified target activity
     * @param target the activity to switch to
     */
    override fun navigateTo(target: ActivityTarget) {
        //Equivalent to switch-case in Java
        val intent = when (target) {
            ActivityTarget.LOGIN -> Intent(context, LoginActivity::class.java)
            ActivityTarget.HOME -> Intent(context, HomeActivity::class.java)
        }

        context.startActivity(intent)
    }
}