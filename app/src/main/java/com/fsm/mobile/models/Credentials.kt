package com.fsm.mobile.models

import kotlinx.serialization.Serializable

/**
 * Data class used to store user login credentials. Is serialisable so that it can be converted
 * to json to send in requests.
 */
@Serializable
data class Credentials(
    val username: String,
    val password: String
)