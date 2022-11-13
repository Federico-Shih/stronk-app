package com.example.stronk.network.dtos

import com.google.gson.annotations.SerializedName


data class UserDTO(
    @SerializedName("id") val id: Int,
    @SerializedName("username") val username: String,
    @SerializedName("gender") val gender: String,
    @SerializedName("avatarUrl") val avatarUrl: String,
    @SerializedName("date") val date: Long
)

data class RoutineDTO(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("description") val description: String,
    @SerializedName("creationDate") val creationDate: Long,
    @SerializedName("rating") val rating: Int,
    @SerializedName("difficulty") val difficulty: String,
    @SerializedName("user") val user: UserDTO,
    @SerializedName("category") val category: String
)

data class PaginatedRoutinesDTO(
    @SerializedName("totalCount") val totalCount: Int,
    @SerializedName("orderBy") val orderBy: String,
    @SerializedName("direction") val direction: String,
    @SerializedName("content") val content: List<RoutineDTO>,
    @SerializedName("size") val size: Int,
    @SerializedName("page") val page: Int,
    @SerializedName("isLastPage") val isLastPage: Boolean
)