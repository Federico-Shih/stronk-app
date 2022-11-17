package com.example.stronk.network.services

import com.example.stronk.network.dtos.*
import com.example.stronk.state.User
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

    @POST("users")
    suspend fun register(@Body registerDTO: RegisterDTO): Response<UserData>

    @POST("users/verify_email")
    suspend fun verifyEmail(@Body verifyEmail: VerifyEmailDTO): Response<Unit>

    @POST("users/resend_verification")
    suspend fun resendVerification(@Body resendEmailDTO: ResendEmailDTO): Response<Unit>
}