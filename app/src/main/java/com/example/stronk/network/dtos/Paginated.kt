package com.example.stronk.network.dtos

import com.google.gson.annotations.SerializedName

data class Paginated<T> (
    @SerializedName("totalCount") val totalCount: Int,
    @SerializedName("orderBy") val orderBy: String,
    @SerializedName("direction") val direction: String,
    @SerializedName("content") val content: List<T>,
    @SerializedName("size") val size: Int,
    @SerializedName("page") val page: Int,
    @SerializedName("isLastPage") val isLastPage: Boolean
)