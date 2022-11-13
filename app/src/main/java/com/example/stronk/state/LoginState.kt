package com.example.stronk.state

import com.example.stronk.model.ApiState

data class LoginState(
    var apiState: ApiState = ApiState(),
    val isAuthenticated: Boolean = false,
    val currentUser: User? = null,
)

val LoginState.canGetCurrentUser: Boolean get() = isAuthenticated