package com.example.stronk.network.datasource

import com.example.stronk.network.dtos.Paginated
import com.example.stronk.network.dtos.RoutineData
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface FavouriteApiService {
    @GET("/favourites")
    fun getFavourites(@Query("page") page: Int, @Query("size") size: Int): Response<Paginated<RoutineData>>

    @GET("/favourites/{routineId}")
    fun postFavouriteRoutine(@Path("routineId") routineId: Int): Response<Any>

    @GET("/favourites/{routineId")
    fun removeFavouriteRoutine(@Path("routineId") routineId: Int): Response<Any>
}