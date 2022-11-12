package com.example.stronk.state

data class ViewRoutineState (
    val routine: Routine = Routine(0,"", "", 0, 0, "", User(0, "", "", "", 0), ""),
    val cycles: List<CycleInfo> = listOf(),
    val faved: Boolean = false,
    val showRatingDialog: Boolean = false,
)