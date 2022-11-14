package com.example.stronk.state

import com.example.stronk.model.ApiState

data class MainState(
    var apiState: ApiState = ApiState(),
    val currentUser: User? = null,
)