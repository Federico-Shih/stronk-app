package com.example.stronk.state

import com.example.stronk.model.ApiState
import com.example.stronk.model.ApiStatus
import com.example.stronk.network.dtos.RoutineData

data class ExploreState(
    // quizás deba ser Routine y no RoutineData
    val routineByCategory: List<List<Routine>> = listOf(),
    val categories: List<Pair<String, Int>> = listOf(),
    val pages: List<Int> = listOf(),
    val isLastOne: List<Boolean> = listOf(),
    val loadState: ApiState = ApiState(ApiStatus.LOADING, ""),
    val search: String = "",
)
