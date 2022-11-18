package com.example.stronk.state

import com.example.stronk.model.ApiState
import com.example.stronk.model.ApiStatus

data class ExecuteRoutineState(
    val loadState: ApiState = ApiState(ApiStatus.LOADING),
    val executingRoutine: Routine? = null,
    val cycles: List<CycleInfo> = listOf(),
    val cycleRepetition: Int = 0,
    val cycleIndex: Int = 0,
    val exerciseIndex: Int = 0,
    val page: Int = 0,
    val finished: Boolean = false,
    val tts: Boolean = false
)

val ExecuteRoutineState.emptyRoutine: Boolean get() = cycles.isEmpty() || cycles.none { it.exList.isNotEmpty() }

val ExecuteRoutineState.hasPrevious: Boolean get() = !(exerciseIndex == 0 && cycleRepetition == 0 && previousNonEmptyCycle == null)
val ExecuteRoutineState.hasNext: Boolean get() = !(cycleRepetition == cycles[cycleIndex].cycleReps - 1 && exerciseIndex == cycles[cycleIndex].exList.size - 1 && nextNonEmptyCycle == null)

val ExecuteRoutineState.previousNonEmptyCycle: CycleInfo? get() = if (cycleIndex > 0) cycles.subList(0, cycleIndex).lastOrNull{it.exList.isNotEmpty()} else null
val ExecuteRoutineState.currentCycle: CycleInfo get() = cycles[cycleIndex]
val ExecuteRoutineState.nextNonEmptyCycle: CycleInfo? get() = if (cycleIndex < (cycles.size - 1)) cycles.subList(cycleIndex+1, cycles.size).firstOrNull{it.exList.isNotEmpty()} else null
val ExecuteRoutineState.nextExercise: ExInfo? get() = if (exerciseIndex < (currentCycle.exList.size - 1)) currentCycle.exList[exerciseIndex+1] else if (cycleRepetition < (currentCycle.cycleReps - 1)) currentCycle.exList[0] else nextNonEmptyCycle?.exList?.get(0)
