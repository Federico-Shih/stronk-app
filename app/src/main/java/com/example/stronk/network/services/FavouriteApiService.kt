package com.example.stronk.network.services

import com.example.stronk.network.dtos.Paginated
import com.example.stronk.network.dtos.RoutineData
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface FavouriteApiService {
    @GET("favourites")
    suspend fun getFavourites(@Query("page") page: Int?, @Query("size") size: Int?): Response<Paginated<RoutineData>>

    @POST("favourites/{routineId}")
    suspend fun postFavouriteRoutine(@Path("routineId") routineId: Int): Response<Unit>

    @DELETE("favourites/{routineId}")
    suspend fun removeFavouriteRoutine(@Path("routineId") routineId: Int): Response<Unit>
}