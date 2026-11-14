package com.fsm.mobile.activity.navigator

/**
 * Interface class for global management of Activities.
 */
interface ActivityService {
    /**
     * Changes the current activity to the specified target activity
     * @param target the activity to switch to
     */
    fun navigateTo(target: ActivityTarget)
}