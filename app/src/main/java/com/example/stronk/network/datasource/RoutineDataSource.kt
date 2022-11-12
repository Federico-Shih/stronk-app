package com.example.stronk.network.datasource

import com.example.stronk.network.dtos.PaginatedRoutines
import com.example.stronk.network.dtos.Routine
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface RoutineApiService {
    @GET("/routines")
    fun getRoutines(
        @Query("categoryId") category: Int,
        @Query("userId") userId: Int,
        @Query("difficulty") difficulty: Int,
        @Query("score") score: Int,
        @Query("search") search: String,
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Query("orderBy") orderBy: String,
        @Query("direction") direction: String,
        ): List<PaginatedRoutines>

    @GET("/routines/{id}")
    fun getRoutine(@Path("id") id: Int): Routine
}