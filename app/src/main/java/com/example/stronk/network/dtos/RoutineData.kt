package com.example.stronk.network.dtos

import com.google.gson.annotations.SerializedName

enum class CycleType {
    WARMUP, EXERCISE, COOLDOWN
}

data class RoutineUserData(
    @SerializedName("id") val id: Int,
    @SerializedName("username") val username: String,
    @SerializedName("gender") val gender: String,
    @SerializedName("avatarUrl") val avatarUrl: String,
    @SerializedName("date") val date: Long
)

data class RoutineData(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("description") val description: String,
    @SerializedName("creationDate") val creationDate: Long,
    @SerializedName("rating") val rating: Int,
    @SerializedName("difficulty") val difficulty: String,
    @SerializedName("user") val user: RoutineUserData,
    @SerializedName("category") val category: String
)

data class CycleData(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("detail") val detail: String,
    @SerializedName("type") val type: CycleType,
    @SerializedName("order") val order: Int,
    @SerializedName("repetitions") val repetitions: Int
)

data class ExerciseImageData(
    @SerializedName("id") val id: Int,
    @SerializedName("number") val number: Int,
    @SerializedName("url") val url: String
)