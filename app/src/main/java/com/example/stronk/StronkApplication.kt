package com.example.stronk

import android.app.Application
import com.example.stronk.network.RetrofitClient
import com.example.stronk.network.PreferencesManager
import com.example.stronk.network.datasources.RoutineDataSource
import com.example.stronk.network.datasources.UserDataSource
import com.example.stronk.network.repositories.RoutineRepository
import com.example.stronk.network.repositories.UserRepository

class StronkApplication : Application() {
    lateinit var preferencesManager: PreferencesManager
    lateinit var userRemoteDataSource: UserDataSource
    lateinit var userRepository: UserRepository
    lateinit var routineRemoteDataSource:RoutineDataSource
    lateinit var routineRepository: RoutineRepository

    override fun onCreate() {
        super.onCreate()
        val retrofitClient = RetrofitClient(this)
        preferencesManager = PreferencesManager(this)
        userRemoteDataSource = UserDataSource(preferencesManager, retrofitClient.getUsersApiService())
        userRepository = UserRepository(remoteDataSource = userRemoteDataSource)
        routineRemoteDataSource = RoutineDataSource(retrofitClient.getRoutineApiService(), retrofitClient.getFavouriteApiService(), retrofitClient.getCategoryApiService())
        routineRepository = RoutineRepository(remoteDataSource = routineRemoteDataSource)
    }
}