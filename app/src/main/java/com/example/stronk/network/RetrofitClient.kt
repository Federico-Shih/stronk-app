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

class RetrofitClient(val context: Context) {
    @Volatile
    private var retrofit: Retrofit? = null

    private fun getInstance(): Retrofit =
        retrofit ?: synchronized(this) {
            retrofit ?: buildRetrofit(context = context).also { retrofit = it }
        }

    private fun buildRetrofit(context: Context): Retrofit {
        val httpLoggingInterceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

        val okHttpClient = OkHttpClient
            .Builder()
            .addInterceptor(AuthInterceptor(context))
            .addInterceptor(httpLoggingInterceptor)
            .build()

        val gson = GsonBuilder().registerTypeAdapter(Date::class.java, ApiDateTypeAdapter()).create()

        return Retrofit.Builder()
            .baseUrl(BuildConfig.API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(okHttpClient)
            .build()
    }

    fun getRoutineApiService() : RoutineApiService {
        return getInstance().create(RoutineApiService::class.java)
    }
    fun getFavouriteApiService() : FavouriteApiService {
        return getInstance().create(FavouriteApiService::class.java)
    }
    fun getUsersApiService() : UsersApiService {
        return getInstance().create(UsersApiService::class.java)
    }
    fun getCategoryApiService() : CategoryApiService {
        return getInstance().create(CategoryApiService::class.java)
    }
}