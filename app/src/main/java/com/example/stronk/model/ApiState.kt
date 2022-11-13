package com.example.stronk.model

enum class ApiStatus {
    LOADING, SUCCESS, FAILURE
}

class ApiState(
    var status: ApiStatus? = null,
    var message: String = ""
)
