package com.example.stronk.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.stronk.StronkApplication
import com.example.stronk.network.repositories.UserRepository
import com.example.stronk.state.MainState
import com.example.stronk.state.User
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainViewModel(private val userRepository: UserRepository) : ViewModel() {
    var uiState by mutableStateOf(MainState())
    private var userJob: Job? = null
    private var logoutJob: Job? = null

    fun logout() {
        logoutJob?.cancel()
        logoutJob = viewModelScope.launch {
            runCatching {
                userRepository.logout()
            }
        }
    }

    fun clearUiState() = run { uiState = MainState() }

    suspend fun forceFetchUser(): Boolean {
        return try {
            val user = userRepository.getCurrentUser(true)
            uiState = uiState.copy(currentUser = user)
            true
        } catch (err: java.lang.Exception) {
            false
        }
    }

    fun fetchCurrentUser() {
        userJob?.cancel()
        userJob = viewModelScope.launch {
            forceFetchUser()
        }
    }
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as StronkApplication)
                MainViewModel(application.userRepository)
            }
        }
    }
}
