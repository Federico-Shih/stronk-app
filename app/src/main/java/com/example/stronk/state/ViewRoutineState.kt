package com.example.stronk.state

import java.util.*
import com.example.stronk.model.ApiState
import com.example.stronk.model.ApiStatus

data class ViewRoutineState (
    val routine: Routine = Routine(0,"", "", Date(0), 0, "", UserRoutine(0, "", "", Date(0)),Category(0,"",null)),
    val loadState: ApiState = ApiState(ApiStatus.LOADING, ""),
    val cycles: List<CycleInfo> = listOf(),
    val faved: Boolean = false,
    val showRatingDialog: Boolean = false,
)