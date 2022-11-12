package com.example.stronk.state

data class MyRoutinesState(
    val myRoutines: List<Routine> = listOf(),
    val favouriteRoutines: List<Routine> = listOf(),
    val myRoutinesPage: Int = 0,
    val favouriteRoutinesPage: Int = 0,
)
