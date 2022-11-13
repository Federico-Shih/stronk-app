package com.example.stronk.network

import android.content.Context
import com.example.stronk.BuildConfig
import com.example.stronk.network.services.CategoryApiService
import com.example.stronk.network.services.FavouriteApiService
import com.example.stronk.network.services.RoutineApiService
import com.example.stronk.network.services.UsersApiService
import com.example.stronk.network.dtos.ApiDateTypeAdapter
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.URI
import java.net.URL
import java.util.*

class RetrofitClient(context: Context) {
    private var retrofit: Retrofit

    init {
        val httpLoggingInterceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

        val okHttpClient = OkHttpClient
            .Builder()
            .addInterceptor(httpLoggingInterceptor)
            .addInterceptor(AuthInterceptor(context))
            .build()

        val gson = GsonBuilder().registerTypeAdapter(Date::class.java, ApiDateTypeAdapter()).create()

        retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(okHttpClient)
            .build()
    }

    fun getRoutineApiService() : RoutineApiService {
        return retrofit.create(RoutineApiService::class.java)
    }
    fun getFavouriteApiService() : FavouriteApiService {
        return retrofit.create(FavouriteApiService::class.java)
    }
    fun getUsersApiService() : UsersApiService {
        return retrofit.create(UsersApiService::class.java)
    }
    fun getCategoryApiService() : CategoryApiService {
        return retrofit.create(CategoryApiService::class.java)
    }
}