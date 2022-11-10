package com.example.stronk.model

import androidx.lifecycle.ViewModel
import com.example.stronk.state.ExecuteRoutineState
import com.example.stronk.state.hasNext
import com.example.stronk.state.hasPrevious
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class ExecuteViewModel : ViewModel() {
    private val _executeState = MutableStateFlow(ExecuteRoutineState())
    val uiState: StateFlow<ExecuteRoutineState> = _executeState

    fun executeRoutine(id: Int) {
        // Cargar la rutina en el state.
    }

    fun next() {
        val current = uiState.value
        if (current.hasNext) {
            if (current.exerciseIndex < current.cycles[current.cycleIndex].exList.size - 1) {
                _executeState.update { it.copy(exerciseIndex = current.exerciseIndex + 1) }
            } else if (current.cycleRepetition < current.cycles[current.cycleIndex].cycleReps - 1) {
                _executeState.update {
                    it.copy(
                        exerciseIndex = 0,
                        cycleRepetition = current.cycleRepetition + 1
                    )
                }
            } else {
                _executeState.update {
                    it.copy(
                        exerciseIndex = 0,
                        cycleRepetition = 0,
                        cycleIndex = current.cycleIndex + 1
                    )
                }
            }
        }
    }

    fun previous() {
        val current = uiState.value
        if (current.hasPrevious) {
            if (current.exerciseIndex > 0) {
                _executeState.update { it.copy(exerciseIndex = current.exerciseIndex - 1) }
            } else if (current.cycleRepetition > 0) {
                _executeState.update {
                    it.copy(
                        exerciseIndex = current.cycles[current.cycleIndex].exList.size - 1,
                        cycleRepetition = current.cycleRepetition - 1
                    )
                }
            } else {
                _executeState.update {
                    it.copy(
                        exerciseIndex = current.cycles[current.cycleIndex - 1].exList.size - 1,
                        cycleRepetition = current.cycles[current.cycleIndex - 1].cycleReps - 1,
                        cycleIndex = current.cycleIndex - 1
                    )
                }
            }
        }
    }
}