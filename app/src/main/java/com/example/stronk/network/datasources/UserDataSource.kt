package com.example.stronk.network.datasources

import com.example.stronk.network.RemoteDataSource
import com.example.stronk.network.PreferencesManager
import com.example.stronk.network.dtos.*
import com.example.stronk.network.services.UsersApiService
import com.example.stronk.state.User

class UserDataSource(
    private val preferencesManager: PreferencesManager,
    private val userApiService: UsersApiService) : RemoteDataSource() {
    suspend fun login(username: String, password: String) {
        val response = handleApiResponse {
            userApiService.login(LoginDTO(username, password))
        }
        preferencesManager.saveAuthToken(response.token)
    }

    suspend fun logout() {
        preferencesManager.removeAuthToken()
        handleApiResponse {
            userApiService.logout()
        }
    }

    suspend fun getCurrentUser() : User {
        return handleApiResponse {
            userApiService.current()
        }.asModel()
    }

    suspend fun register(registerDTO: RegisterDTO) : User {
        return handleApiResponse {
            userApiService.register(registerDTO)
        }.asModel()
    }

    suspend fun verifyEmail(email: String, code: String) {
        handleApiResponse {
            userApiService.verifyEmail(VerifyEmailDTO(email, code))
        }
    }

    suspend fun resendVerification(email: String) {
        handleApiResponse {
            userApiService.resendVerification(ResendEmailDTO(email))
        }
    }
}