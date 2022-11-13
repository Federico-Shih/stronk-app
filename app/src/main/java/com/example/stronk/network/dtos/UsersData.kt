package com.example.stronk.network.dtos

import com.example.stronk.state.User
import com.google.gson.annotations.SerializedName
import java.util.*

data class TokenData(
    @SerializedName("token") val token: String
)

data class UserData(
    @SerializedName("id") val id: Int,
    @SerializedName("username") val username: String,
    @SerializedName("firstName") val firstName: String,
    @SerializedName("lastName") val lastName: String,
    @SerializedName("gender") val gender: String,
    @SerializedName("birthdate") val birthdate: Date,
    @SerializedName("email") val email: String,
    @SerializedName("phone") val phone: String,
    @SerializedName("avatarUrl") val avatarUrl: String
) {
    fun asModel(): User {
        return User(id,username,gender,avatarUrl,email,birthdate)
    }
}