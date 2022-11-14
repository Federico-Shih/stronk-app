package com.example.stronk.network.dtos

import com.google.gson.annotations.SerializedName

data class Paginated<T> (
    @SerializedName("totalCount") val totalCount: Int,
    @SerializedName("orderBy") val orderBy: String? = null,
    @SerializedName("direction") val direction: String? = null,
    @SerializedName("content") val content: List<T> = arrayListOf(),
    @SerializedName("size") val size: Int,
    @SerializedName("page") val page: Int,
    @SerializedName("isLastPage") val isLastPage: Boolean
)