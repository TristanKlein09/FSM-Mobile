package com.fsm.mobile.service

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.result.ActivityResultLauncher
import androidx.core.content.ContextCompat
import com.fsm.mobile.permission.PermissionService
import com.fsm.mobile.permission.PermissionType
import kotlinx.coroutines.suspendCancellableCoroutine
import java.util.jar.Manifest

class AndroidPermissionService(private val context: Context): PermissionService{

//    //IDE gives error if not specified that it cant be null
//    private var callback: ((Boolean) -> Unit)? = null

    override fun requestPermission(permission: PermissionType): Boolean {
        val activity = context as Activity

        when (permission) {
            PermissionType.ACCESS_LOCAL_NETWORK -> return requestACCESS_LOCAL_NETWORK()
        }

    }

    private fun requestACCESS_LOCAL_NETWORK(): Boolean {
        //If permission is not already granted, then open up the window to ask for it
        if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_LOCAL_NETWORK) != PackageManager.PERMISSION_GRANTED) {
            val activity = context as Activity
            activity.requestPermissions(arrayOf(android.Manifest.permission.ACCESS_LOCAL_NETWORK), 0)

            //Check again if they gave permission
            if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_LOCAL_NETWORK) == PackageManager.PERMISSION_GRANTED) {
                return true
            } else {
                return false
            }

        } else {
            return true
        }
    }
}