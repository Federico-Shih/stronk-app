package com.example.stronk.network.services

import com.example.stronk.network.dtos.LoginDTO
import com.example.stronk.network.dtos.TokenData
import com.example.stronk.network.dtos.UserData
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface UsersApiService {
    @POST("users/login")
    suspend fun login(@Body loginDTO: LoginDTO): Response<TokenData>

    @GET("users/current")
    suspend fun current(): Response<UserData>

    @POST("users/logout")
    suspend fun logout(): Response<Unit>
}