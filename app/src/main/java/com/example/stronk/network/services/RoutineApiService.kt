package com.example.stronk.network.services

import com.example.stronk.network.dtos.CycleData
import com.example.stronk.network.dtos.ExerciseImageData
import com.example.stronk.network.dtos.Paginated
import com.example.stronk.network.dtos.RoutineData
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface RoutineApiService {
    @GET("routines")
    suspend fun getRoutines(
        @Query("categoryId") category: Int?,
        @Query("userId") userId: Int?,
        @Query("difficulty") difficulty: Int?,
        @Query("score") score: Int?,
        @Query("search") search: String?,
        @Query("page") page: Int?,
        @Query("size") size: Int?,
        @Query("orderBy") orderBy: String?,
        @Query("direction") direction: String?,
        ): Response<List<Paginated<RoutineData>>>

    @GET("routines/{id}")
    suspend fun getRoutine(@Path("id") id: Int): Response<RoutineData>

    @GET("users/current/routines")
    suspend fun getMyRoutines(
        @Query("difficulty") difficulty: Int?,
        @Query("search") search: String?,
        @Query("page") page: Int?,
        @Query("size") size: Int?,
        @Query("orderBy") orderBy: String ?,
        @Query("direction") direction: String ?,
    ): Response<Paginated<RoutineData>>

    @GET("routines/{routineId}/cycles")
    suspend fun getCycles(
        @Path("routineId") routineId: Int,
        @Query("page") page: Int?,
        @Query("size") size: Int?,
        @Query("orderBy") orderBy: String?,
        @Query("direction") direction: String?,
    ): Response<Paginated<CycleData>>

    @GET("exercises/{exerciseId}/images")
    suspend fun getExercisesImages(
        @Path("exerciseId") exerciseId: Int,
        @Query("page") page: Int?,
        @Query("size") size: Int?,
        @Query("orderBy") orderBy: String?,
        @Query("direction") direction: String?,
    ): Response<Paginated<ExerciseImageData>>
}