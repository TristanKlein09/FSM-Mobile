package com.fsm.mobile.permission

/**
 * Interface class for global management of application permissions
 */
interface PermissionService {
    /**
     * Checks if the app has been granted a certain permission
     * @param permission [PermissionType] which indicates the type of permission
     * @return [Boolean] which states whether the permission is granted or not.
     */
    fun requestPermission(permission: PermissionType): Boolean
}