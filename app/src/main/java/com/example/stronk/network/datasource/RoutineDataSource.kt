package com.example.stronk.network.datasource

import com.example.stronk.network.dtos.PaginatedRoutinesDTO
import com.example.stronk.network.dtos.RoutineDTO
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query


interface RoutineApiService {
    @GET("/routines")
    suspend fun getRoutines(
        @Query("categoryId") category: Int,
        @Query("userId") userId: Int,
        @Query("difficulty") difficulty: Int,
        @Query("score") score: Int,
        @Query("search") search: String,
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Query("orderBy") orderBy: String,
        @Query("direction") direction: String,
        ): Response<List<PaginatedRoutinesDTO>>

    @GET("/routines/{id}")
    suspend fun getRoutine(@Path("id") id: Int): Response<RoutineDTO>
}