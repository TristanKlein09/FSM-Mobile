package com.fsm.mobile.service

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import com.fsm.mobile.permission.PermissionService
import com.fsm.mobile.permission.PermissionType

/**
 * Implementation of [PermissionService]. Responsible for the global management of application permissions
 */
class AndroidPermissionService(private val context: Context): PermissionService{

    /**
     * Checks if the app has been granted a certain permission
     * @param permission [PermissionType] which indicates the type of permission
     * @return [Boolean] which states whether the permission is granted or not.
     */
    override fun requestPermission(permission: PermissionType): Boolean {
        val activity = context as Activity

        when (permission) {
            PermissionType.ACCESS_LOCAL_NETWORK -> return requestACCESS_LOCAL_NETWORK()
        }

    }

    /**
     * Checks whether the ACCESS_LOCAL_NETWORK permission has been granted. If not it will open the prompt to ask the
     * user for that permission
     * @return whether the permission has been granted
     */
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