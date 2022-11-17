package com.example.stronk.state

import androidx.compose.runtime.mutableStateOf
import com.example.stronk.model.ApiState

enum class InputError(val code: Int) {
    EMPTY_ERROR(100), DEFAULT(-1), PASSNOTMATCH(102),
}

data class InputState(
    val hasError: Boolean = false,
    val errorCode: Int = InputError.DEFAULT.code,
)

data class RegisterState(
    val emailInputState: InputState = InputState(),
    val usernameInputState: InputState = InputState(),
    val firstnameInputState: InputState = InputState(),
    val lastnameInputState: InputState = InputState(),
    val passwordInputState: InputState = InputState(),
    val apiState: ApiState = ApiState(),
    val username: String = "",
    val password: String = "",
)