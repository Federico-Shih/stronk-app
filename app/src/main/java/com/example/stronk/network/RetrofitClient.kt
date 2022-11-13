package com.example.stronk.network

import android.content.Context
import com.example.stronk.BuildConfig
import com.example.stronk.network.datasource.CategoryApiService
import com.example.stronk.network.datasource.FavouriteApiService
import com.example.stronk.network.datasource.RoutineApiService
import com.example.stronk.network.datasource.UsersApiService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitClient(context: Context) {
    private val retrofit: Retrofit

    init {
        val httpLoggingInterceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

        val okHttpClient = OkHttpClient
            .Builder()
            .addInterceptor(httpLoggingInterceptor)
            .addInterceptor(AuthInterceptor(context))
            .build()

        retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
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