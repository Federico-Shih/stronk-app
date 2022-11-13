package com.example.stronk.network.dtos

import com.example.stronk.state.models.User
import com.google.gson.annotations.SerializedName

data class TokenData(
    @SerializedName("token") val token: String
)

data class UserData(
    @SerializedName("id") val id: Int,
    @SerializedName("username") val username: String,
    @SerializedName("firstName") val firstName: String,
    @SerializedName("lastName") val lastName: String,
    @SerializedName("gender") val gender: String,
    @SerializedName("birthdate") val birthdate: Long,
    @SerializedName("email") val email: String,
    @SerializedName("phone") val phone: String,
    @SerializedName("avatarUrl") val avatarUrl: String
) {
    fun asModel(): User {
        return User(
            id = id,
            username = username,
            birthdate = birthdate,
            avatarUrl = avatarUrl,
            email = email
        )
    }
}