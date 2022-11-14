package com.example.stronk.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.stronk.StronkApplication
import com.example.stronk.network.DataSourceException
import com.example.stronk.network.SessionManager
import com.example.stronk.network.services.UsersApiService
import com.example.stronk.network.dtos.LoginDTO
import com.example.stronk.network.repositories.UserRepository
import com.example.stronk.state.LoginState
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class LoginViewModel(
    sessionManager: SessionManager,
    private val userRepository: UserRepository,
    ) : ViewModel() {

    private var loginJob: Job? = null

    var uiState by mutableStateOf(LoginState(isAuthenticated = sessionManager.fetchAuthToken() != null))
        private set

    fun login(username: String, password: String) {
        loginJob?.cancel()

        loginJob = viewModelScope.launch {
            uiState = uiState.copy(apiState = ApiState(ApiStatus.LOADING))
            runCatching {
             userRepository.login(username, password)
            }.onSuccess {
                uiState = uiState.copy(apiState = ApiState(ApiStatus.SUCCESS), isAuthenticated = true)
            }.onFailure {
                error ->
                if (error is DataSourceException)
                {
                    when (error.code) {
                        4 -> {
                            uiState = uiState.copy(isWrongPasswordOrUser = true)
                        }
                    }
                    uiState = uiState.copy(apiState = ApiState(ApiStatus.FAILURE, error.message ?: "Unknown error"))
                }
            }
        }
    }

    fun dismissMessage() {
        uiState = uiState.copy(apiState = ApiState(message = "", status = null))
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as StronkApplication)
                LoginViewModel(application.sessionManager, application.userRepository)
            }
        }
    }
}