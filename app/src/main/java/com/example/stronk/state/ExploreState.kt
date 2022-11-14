package com.example.stronk.state

import com.example.stronk.model.ApiState
import com.example.stronk.model.ApiStatus

data class ExploreState(
    val routineByCategory: List<List<Routine>> = listOf(),
    val categories: List<Pair<String, Int>> = listOf(),
    val pages: List<Int> = listOf(),
    val isLastOne: List<Boolean> = listOf(),
    val loadState: ApiState = ApiState(ApiStatus.LOADING, ""),
    val search: String = "",
)
