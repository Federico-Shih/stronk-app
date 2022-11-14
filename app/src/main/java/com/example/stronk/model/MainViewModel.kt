package com.example.stronk.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.stronk.StronkApplication
import com.example.stronk.network.repositories.UserRepository

class MainViewModel(private val userRepository: UserRepository) : ViewModel() {
    suspend fun logout() {
        userRepository.logout()
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
