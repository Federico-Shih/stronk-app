package com.example.stronk.network.dtos

import com.example.stronk.state.Category
import com.google.gson.annotations.SerializedName

data class CategoryData(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("detail") val detail: String?,
){
    fun asModel():Category{
        return Category(id,name,detail)
    }
}