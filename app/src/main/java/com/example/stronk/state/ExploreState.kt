package com.example.stronk.state

import com.example.stronk.model.ApiState
import com.example.stronk.model.ApiStatus

data class CategoryInfo(
    val id: Int,
    val name: String,
    var pages: Int,
    val routines: MutableList<Routine>,
    var isLastPage: Boolean
)

data class ExploreState(
    val categories: List<CategoryInfo> = listOf(),
    val searchedRoutines: List<Routine> = listOf(),
    val loadState: ApiState = ApiState(ApiStatus.LOADING, ""),
    val order: String = "id",
    val searchString: String = "",
)

val ExploreState.searching: Boolean get() = searchString.isNotEmpty()
val ExploreState.foundSomething: Boolean get() = searchedRoutines.isNotEmpty()
