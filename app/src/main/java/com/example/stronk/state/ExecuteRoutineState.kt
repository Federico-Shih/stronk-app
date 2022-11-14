package com.example.stronk.state

data class ExecuteRoutineState(
    val executingRoutine: Routine? = null,
    val cycles: List<CycleInfo> = listOf(),
    val cycleRepetition: Int = 0,
    val cycleIndex: Int = 0,
    val exerciseIndex: Int = 0,
    val page: Int = 0
)

val ExecuteRoutineState.hasPrevious: Boolean get() = !(cycleIndex == 0 && exerciseIndex == 0 && cycleRepetition == 0)
val ExecuteRoutineState.hasNext: Boolean get() = !(cycleIndex == cycles.size - 1 && cycleRepetition == cycles[cycleIndex].cycleReps - 1 && exerciseIndex == cycles[cycleIndex].exList.size - 1)

val ExecuteRoutineState.previousCycle: CycleInfo? get() = if (cycleIndex > 0) cycles[cycleIndex - 1] else null
val ExecuteRoutineState.currentCycle: CycleInfo get() = cycles[cycleIndex]
val ExecuteRoutineState.nextCycle: CycleInfo? get() = if (cycleIndex < (cycles.size - 1)) cycles[cycleIndex + 1] else null
val ExecuteRoutineState.nextExercise: ExInfo? get() = if (exerciseIndex < (cycles[cycleIndex].exList.size - 1)) cycles[cycleIndex].exList[exerciseIndex + 1] else if (cycleRepetition < cycles[cycleIndex].cycleReps - 1) cycles[cycleIndex].exList[0] else if (cycleIndex < (cycles.size - 1)) cycles[cycleIndex + 1].exList[0] else null