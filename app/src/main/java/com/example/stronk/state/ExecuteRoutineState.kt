package com.example.stronk.state

data class ExecuteRoutineState(
    val executingRoutine: Routine? = null,
    val cycles: List<CycleInfo> = listOf(),
    val cycleRepetition: Int = 0,
    val cycleIndex: Int = 0,
    val exerciseIndex: Int = 0
)

val ExecuteRoutineState.hasPrevious: Boolean get() = !(cycleIndex == 0 && exerciseIndex == 0 && cycleRepetition == 0)
val ExecuteRoutineState.hasNext: Boolean get() = (cycleIndex < (cycles.size - 1) || exerciseIndex < cycles[cycleIndex].exList.size - 1 || cycleRepetition < cycles[cycleIndex].cycleReps - 1)