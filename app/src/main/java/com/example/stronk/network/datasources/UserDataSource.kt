package com.example.stronk.network.datasources

import com.example.stronk.network.RemoteDataSource
import com.example.stronk.network.SessionManager
import com.example.stronk.network.dtos.LoginDTO
import com.example.stronk.network.dtos.UserData
import com.example.stronk.network.services.UsersApiService
import com.example.stronk.state.User

class UserDataSource(
    private val sessionManager: SessionManager,
    private val userApiService: UsersApiService) : RemoteDataSource() {
    suspend fun login(username: String, password: String) {
        val response = handleApiResponse {
            userApiService.login(LoginDTO(username, password))
        }
        sessionManager.saveAuthToken(response.token)
    }

    suspend fun logout() {
        handleApiResponse {
            userApiService.logout()
        }
        sessionManager.removeAuthToken()
    }

    suspend fun getCurrentUser() : User {
        return handleApiResponse {
            userApiService.current()
        }.asModel()
    }
}