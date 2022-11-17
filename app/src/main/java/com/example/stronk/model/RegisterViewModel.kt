package com.example.stronk.model

import android.provider.ContactsContract.Data
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.stronk.StronkApplication
import com.example.stronk.network.ApiErrorCode
import com.example.stronk.network.DataSourceException
import com.example.stronk.network.dtos.RegisterDTO
import com.example.stronk.network.repositories.UserRepository
import com.example.stronk.state.InputError
import com.example.stronk.state.InputState
import com.example.stronk.state.RegisterState
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class RegisterViewModel(private val userRepository: UserRepository) : ViewModel() {
    var uiState by mutableStateOf(RegisterState())

    var registerJob: Job? = null
    var verifyJob: Job? = null

    fun clearUsername() {
        uiState = uiState.copy(usernameInputState = InputState())
    }
    fun clearFirstname() {
        uiState = uiState.copy(firstnameInputState = InputState())
    }
    fun clearPassword() {
        uiState = uiState.copy(passwordInputState = InputState())
    }
    fun clearLastname() {
        uiState = uiState.copy(lastnameInputState = InputState())
    }
    fun clearEmail() {
        uiState = uiState.copy(emailInputState = InputState())
    }

    fun verifyCode(email: String, code: String, username: String, password: String, onSuccess: () -> Unit) {
        verifyJob?.cancel()
        verifyJob = viewModelScope.launch {
            runCatching {
                userRepository.verifyEmail(email, code)
            }.onSuccess {
                runCatching {
                    userRepository.login(username, password)
                }.onSuccess {
                    onSuccess()
                }
            }.onFailure {
                if (it is DataSourceException) {
                    uiState = uiState.copy(apiState = ApiState(ApiStatus.FAILURE, code = it.code))
                }
            }
        }
    }

    fun register(
        username: String,
        email: String,
        firstname: String,
        lastname: String,
        password: String,
        confirmPassword: String,
        onRegisterSuccess: (email: String) -> Unit
    ) {
        if (username != "" && email != "" && firstname != "" && lastname != "" && password != "" && password == confirmPassword) {
            registerJob?.cancel()
            uiState = uiState.copy(apiState = ApiState(ApiStatus.LOADING))
            registerJob = viewModelScope.launch {
                runCatching {
                    userRepository.register(
                        RegisterDTO(
                            username = username,
                            password = password,
                            email = email,
                            firstName = firstname,
                            lastName = lastname
                        )
                    )
                }.onSuccess {
                    uiState = uiState.copy(username = username, password = password, apiState = ApiState(ApiStatus.SUCCESS))
                    onRegisterSuccess(email)
                }.onFailure {
                    if (it is DataSourceException) {
                        uiState = uiState.copy(apiState = ApiState(ApiStatus.FAILURE, code = it.code))
                        when (it.code) {
                            ApiErrorCode.DATA_CONSTRAINT.code -> {
                                val state = InputState(
                                    hasError = true,
                                    errorCode = ApiErrorCode.DATA_CONSTRAINT.code
                                )
                                uiState = uiState.copy(
                                    usernameInputState = state,
                                    emailInputState = state.copy()
                                )
                            }
                        }
                    }
                }
            }
        }
        if (username == "") {
            uiState = uiState.copy(
                usernameInputState = InputState(
                    hasError = true,
                    errorCode = InputError.EMPTY_ERROR.code
                )
            )
        }
        if (email == "") {
            uiState = uiState.copy(
                emailInputState = InputState(
                    hasError = true,
                    errorCode = InputError.EMPTY_ERROR.code
                )
            )
        }
        if (password == "") {
            uiState = uiState.copy(
                passwordInputState = InputState(
                    hasError = true,
                    errorCode = InputError.EMPTY_ERROR.code
                )
            )
        }
        if (password != confirmPassword) {
            uiState = uiState.copy(
                passwordInputState = InputState(
                    hasError = true,
                    errorCode = InputError.PASSNOTMATCH.code
                )
            )
        }
        if (firstname == "") {
            uiState = uiState.copy(
                firstnameInputState = InputState(
                    hasError = true,
                    errorCode = InputError.EMPTY_ERROR.code
                )
            )
        }
        if (lastname == "") {
            uiState = uiState.copy(
                lastnameInputState = InputState(
                    hasError = true,
                    errorCode = InputError.EMPTY_ERROR.code
                )
            )
        }

    }

    fun dismissMessage() {
        uiState = uiState.copy(apiState = ApiState(message = "", status = null))
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as StronkApplication)
                RegisterViewModel(application.userRepository)
            }
        }
    }
}