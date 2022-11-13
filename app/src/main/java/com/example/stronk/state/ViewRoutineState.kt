package com.example.stronk.state

import java.util.*

data class ViewRoutineState (
    val loading: Boolean = true,
    val routine: Routine = Routine(0,"", "", Date(0), 0, "", UserRoutine(0, "", "", "", Date(0)), ""),
    val cycles: List<CycleInfo> = listOf(),
    val faved: Boolean = false,
    val showRatingDialog: Boolean = false,
)