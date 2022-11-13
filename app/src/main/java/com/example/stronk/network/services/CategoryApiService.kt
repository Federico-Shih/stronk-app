package com.example.stronk.network.services

import com.example.stronk.network.dtos.CategoryData
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface CategoryApiService {
    @GET("categories")
    fun getCategories(
        @Query("search") search: String,
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Query("orderBy") orderBy: String,
        @Query("direction") direction: String,
    ): Response<CategoryData>
}