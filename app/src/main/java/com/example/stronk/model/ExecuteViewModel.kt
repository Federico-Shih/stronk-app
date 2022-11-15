package com.example.stronk.model

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.lifecycle.ViewModel
import com.example.stronk.state.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.stronk.StronkApplication
import com.example.stronk.network.repositories.RoutineRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class ExecuteViewModel(private val routineRepository: RoutineRepository) : ViewModel() {
    var uiState by mutableStateOf(ExecuteRoutineState())
    var fetchJob: Job? = null

    fun executeRoutine(id: Int) : Boolean {
        uiState = uiState.copy(loadState = ApiState(ApiStatus.LOADING))
        fetchJob?.cancel()
        fetchJob = viewModelScope.launch {
            runCatching {
                routineRepository.getRoutine(id)
            }.onSuccess { routine ->
                uiState = uiState.copy(
                    executingRoutine = routine.asModel(),
                )
                runCatching {
                    routineRepository.getRoutineCycles(id)
                }.onSuccess { cycles ->
                    val firstNonEmptyCycle = cycles.indexOfFirst { it.exList.isNotEmpty() }
                    uiState = uiState.copy(cycles = cycles, loadState = ApiState(ApiStatus.SUCCESS), cycleIndex = firstNonEmptyCycle)
                }
            }.onFailure {
                uiState = uiState.copy(
                    loadState = ApiState(
                        ApiStatus.FAILURE,
                        it.message ?: "Unknown Error"
                    )
                )
            }
        }
        return true
    }

    fun setPage(page: Int) {
        uiState = uiState.copy(page = page)
    }

    fun next() {
        if (uiState.hasNext) {
            uiState =
                if (uiState.exerciseIndex < uiState.cycles[uiState.cycleIndex].exList.size - 1) {
                    uiState.copy(exerciseIndex = uiState.exerciseIndex + 1)
                } else if (uiState.cycleRepetition < uiState.cycles[uiState.cycleIndex].cycleReps - 1) {
                    uiState.copy(exerciseIndex = 0, cycleRepetition = uiState.cycleRepetition + 1)
                } else {
                    uiState.copy(
                        exerciseIndex = 0,
                        cycleRepetition = 0,
                        cycleIndex = uiState.cycles.subList(uiState.cycleIndex + 1, uiState.cycles.size).indexOfFirst { it.exList.isNotEmpty() } + uiState.cycleIndex + 1
                    )
                }
        }
    }

    fun previous() {
        if (uiState.hasPrevious) {
            uiState = if (uiState.exerciseIndex > 0) {
                uiState.copy(exerciseIndex = uiState.exerciseIndex - 1)
            } else if (uiState.cycleRepetition > 0) {
                uiState.copy(
                    exerciseIndex = uiState.cycles[uiState.cycleIndex].exList.size - 1,
                    cycleRepetition = uiState.cycleRepetition - 1
                )
            } else {
                val prevCycleIndex = uiState.cycles.subList(0, uiState.cycleIndex).indexOfLast { it.exList.isNotEmpty() }
                uiState.copy(
                    exerciseIndex = uiState.cycles[prevCycleIndex].exList.size - 1,
                    cycleRepetition = uiState.cycles[prevCycleIndex].cycleReps - 1,
                    cycleIndex = prevCycleIndex
                )
            }
        }
    }

    fun finish() {
        uiState = uiState.copy(finished = true)
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as StronkApplication)
                ExecuteViewModel(application.routineRepository)
            }
        }
    }
}