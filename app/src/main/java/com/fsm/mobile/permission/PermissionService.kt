package com.fsm.mobile.permission

import android.content.Context
import okhttp3.Callback

interface PermissionService {
    fun requestPermission(permission: PermissionType): Boolean
}