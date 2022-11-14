package com.example.stronk.state

import com.example.stronk.model.ApiState
import com.example.stronk.model.ApiStatus

data class MyRoutinesState(
    val myRoutines: List<Routine> = listOf(),
    val favouriteRoutines: List<Routine> = listOf(),
    val myRoutinesPage: Int = 0,
    val favouriteRoutinesPage: Int = 0,
    val isLastPageMyRoutines: Boolean = false,
    val isLastPageFav:Boolean=false,
    val loadState: ApiState = ApiState(ApiStatus.LOADING, ""),
)
