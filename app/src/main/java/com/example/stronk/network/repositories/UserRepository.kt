package com.example.stronk.network.repositories

import com.example.stronk.network.datasources.UserDataSource
import com.example.stronk.network.dtos.UserData
import com.example.stronk.state.User
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class UserRepository(private val remoteDataSource: UserDataSource) {
    private val mutex = Mutex()
    private var currentUser: User? = null

    suspend fun login(username: String, password: String) {
        remoteDataSource.login(username, password)
    }

    suspend fun logout() {
        remoteDataSource.logout()
    }

    suspend fun getCurrentUser(refresh: Boolean) : User? {
        if (refresh || currentUser == null) {
            val result = remoteDataSource.getCurrentUser()
            mutex.withLock {
                this.currentUser = result
            }
        }
        return mutex.withLock { this.currentUser }
    }
}