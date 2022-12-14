package com.example.stronk.network

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken
import retrofit2.Response
import java.io.IOException

data class NetworkError(
    @SerializedName("code") val code: Int,
    @SerializedName("description") val description: String,
    @SerializedName("details") var details: List<String>? = null
)

class DataSourceException(
    val code: Int,
    message: String,
    val details: List<String>?
) : Exception(message) {
}

abstract class RemoteDataSource {
    suspend fun <T : Any> handleApiResponse(
        execute: suspend () -> Response<T>
    ) : T {
        try {
            val response = execute()
            val body = response.body()
            if (response.isSuccessful && body != null) {
                return body
            }
            response.errorBody()?.let {
                val gson = Gson()
                val error = gson.fromJson<NetworkError>(it.string(), object: TypeToken<NetworkError?>() {}.type)
                throw DataSourceException(error.code, error.description, error.details)
            }
            throw DataSourceException(ApiErrorCode.UNEXPECTED_ERROR.code, "Missing Error", null)
        } catch (e: DataSourceException) {
          throw e
        } catch (e: IOException) {
            println(e)
            throw DataSourceException(ApiErrorCode.CONNECTION_ERROR.code, "Connection Error", getDetailsFromException(e))
        } catch (e: java.lang.Exception) {
            throw DataSourceException(ApiErrorCode.UNEXPECTED_ERROR.code, e.message ?: "Unexpected Error", getDetailsFromException(e))
        }
    }

    private fun getDetailsFromException(e: Exception) : List<String> {
        return if (e.message != null) listOf(e.message!!) else emptyList()
    }
}