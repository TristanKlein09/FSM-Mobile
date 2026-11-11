package com.fsm.mobile.models

import kotlinx.serialization.Serializable

//@Serializable will allow me to convert the data in this class to a json
@Serializable
data class Credentials(
    val username: String,
    val password: String
)
