package com.example.stronk

import android.app.Application
import com.example.stronk.network.RemoteDataSource
import com.example.stronk.network.RetrofitClient
import com.example.stronk.network.SessionManager
import com.example.stronk.network.datasources.UserDataSource
import com.example.stronk.network.repositories.UserRepository

class StronkApplication : Application() {
    lateinit var sessionManager: SessionManager
    lateinit var userRemoteDataSource: UserDataSource
    lateinit var userRepository: UserRepository

    override fun onCreate() {
        super.onCreate()
        val retrofitClient = RetrofitClient(this)
        sessionManager = SessionManager(this)
        userRemoteDataSource = UserDataSource(sessionManager, retrofitClient.getUsersApiService())
        userRepository = UserRepository(remoteDataSource = userRemoteDataSource)
        // Agregar los otros repositories
    }
}