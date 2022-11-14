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
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.stronk.StronkApplication
import com.example.stronk.network.repositories.RoutineRepository

class ExecuteViewModel(private val routineRepository: RoutineRepository) : ViewModel() {
    var uiState by mutableStateOf(ExecuteRoutineState())

    val cycleListPrueba = listOf(
        CycleInfo(
            "Sugerida por copilot",
            listOf(
                ExInfo(
                    "Pushups",
                    10,
                    3,
                    "https://images.ecestaticos.com/WAot9QyeV2vzRuE1gVu55WLdv7Y=/0x0:0x0/1200x900/filters:fill(white):format(jpg)/f.elconfidencial.com%2Foriginal%2Fb3c%2Fc7c%2Fff6%2Fb3cc7cff6cc1ee44df172f15afa3e4f9.jpg",
                    "Este es un ejercicio que se realiza ejercitando aaaaaaaaaaaaaaaaaaaaaaaaaaaaa aaaaaaaaaaa aaaaaaaaaa aaa           aaaaaaaaaaaaa aaaaaaaaaaaaaaa"                ),
                ExInfo(
                    "Squats",
                    10,
                    null,
                    "https://images.ecestaticos.com/WAot9QyeV2vzRuE1gVu55WLdv7Y=/0x0:0x0/1200x900/filters:fill(white):format(jpg)/f.elconfidencial.com%2Foriginal%2Fb3c%2Fc7c%2Fff6%2Fb3cc7cff6cc1ee44df172f15afa3e4f9.jpg",
                    "Este es un ejercicio que se realiza ejercitando las piernas"
                ),
                ExInfo(
                    "Pullups",
                    null,
                    10,
                    "https://images.ecestaticos.com/WAot9QyeV2vzRuE1gVu55WLdv7Y=/0x0:0x0/1200x900/filters:fill(white):format(jpg)/f.elconfidencial.com%2Foriginal%2Fb3c%2Fc7c%2Fff6%2Fb3cc7cff6cc1ee44df172f15afa3e4f9.jpg",
                    "Este es un ejercicio que se realiza ejercitando las piernas y los brazons"
                ),
                ExInfo(
                    "Planks",
                    null,
                    10,
                    "https://images.ecestaticos.com/WAot9QyeV2vzRuE1gVu55WLdv7Y=/0x0:0x0/1200x900/filters:fill(white):format(jpg)/f.elconfidencial.com%2Foriginal%2Fb3c%2Fc7c%2Fff6%2Fb3cc7cff6cc1ee44df172f15afa3e4f9.jpg",
                    "Este es un ejercicio que se realiza ejercitando el pecho y los brazos"
                ),
            ),
            2
        ),
        CycleInfo(
            "AAAAAA",
            listOf(
                ExInfo(
                    "Pushups",
                    10,
                    null,
                    "https://images.ecestaticos.com/WAot9QyeV2vzRuE1gVu55WLdv7Y=/0x0:0x0/1200x900/filters:fill(white):format(jpg)/f.elconfidencial.com%2Foriginal%2Fb3c%2Fc7c%2Fff6%2Fb3cc7cff6cc1ee44df172f15afa3e4f9.jpg",
                    "Este es un ejercicio que se realiza ejercitando el pecho y los brazos"
                ),
                ExInfo(
                    "Squats",
                    10,
                    50,
                    "https://images.ecestaticos.com/WAot9QyeV2vzRuE1gVu55WLdv7Y=/0x0:0x0/1200x900/filters:fill(white):format(jpg)/f.elconfidencial.com%2Foriginal%2Fb3c%2Fc7c%2Fff6%2Fb3cc7cff6cc1ee44df172f15afa3e4f9.jpg",
                    "Este es un ejercicio que se realiza ejercitando el pecho y los brazos"
                ),
                ExInfo(
                    "Pullups",
                    null,
                    10,
                    "https://images.ecestaticos.com/WAot9QyeV2vzRuE1gVu55WLdv7Y=/0x0:0x0/1200x900/filters:fill(white):format(jpg)/f.elconfidencial.com%2Foriginal%2Fb3c%2Fc7c%2Fff6%2Fb3cc7cff6cc1ee44df172f15afa3e4f9.jpg",
                    "Este es un ejercicio que se realiza ejercitando el pecho y los brazos"
                ),
                ExInfo(
                    "Planks",
                    null,
                    10,
                    "https://images.ecestaticos.com/WAot9QyeV2vzRuE1gVu55WLdv7Y=/0x0:0x0/1200x900/filters:fill(white):format(jpg)/f.elconfidencial.com%2Foriginal%2Fb3c%2Fc7c%2Fff6%2Fb3cc7cff6cc1ee44df172f15afa3e4f9.jpg",
                    "Este es un ejercicio que se realiza ejercitando el pecho y los brazos"
                ),
            ),
            1
        ),
        CycleInfo(
            "Sugerida por copilot",
            listOf(
                ExInfo(
                    "Pushups",
                    10,
                    3,
                    "https://images.ecestaticos.com/WAot9QyeV2vzRuE1gVu55WLdv7Y=/0x0:0x0/1200x900/filters:fill(white):format(jpg)/f.elconfidencial.com%2Foriginal%2Fb3c%2Fc7c%2Fff6%2Fb3cc7cff6cc1ee44df172f15afa3e4f9.jpg",
                    "Este es un ejercicio que se realiza ejercitando el pecho y los brazos"
                ),
                ExInfo(
                    "Squats",
                    10,
                    null,
                    "https://images.ecestaticos.com/WAot9QyeV2vzRuE1gVu55WLdv7Y=/0x0:0x0/1200x900/filters:fill(white):format(jpg)/f.elconfidencial.com%2Foriginal%2Fb3c%2Fc7c%2Fff6%2Fb3cc7cff6cc1ee44df172f15afa3e4f9.jpg",
                    "Este es un ejercicio que se realiza ejercitando el pecho y los brazos"
                ),
                ExInfo(
                    "Pullups",
                    null,
                    10,
                    "https://images.ecestaticos.com/WAot9QyeV2vzRuE1gVu55WLdv7Y=/0x0:0x0/1200x900/filters:fill(white):format(jpg)/f.elconfidencial.com%2Foriginal%2Fb3c%2Fc7c%2Fff6%2Fb3cc7cff6cc1ee44df172f15afa3e4f9.jpg",
                    "Este es un ejercicio que se realiza ejercitando el pecho y los brazos"
                ),
                ExInfo(
                    "Planks",
                    null,
                    10,
                    "https://images.ecestaticos.com/WAot9QyeV2vzRuE1gVu55WLdv7Y=/0x0:0x0/1200x900/filters:fill(white):format(jpg)/f.elconfidencial.com%2Foriginal%2Fb3c%2Fc7c%2Fff6%2Fb3cc7cff6cc1ee44df172f15afa3e4f9.jpg",
                    "Este es un ejercicio que se realiza ejercitando el pecho y los brazos"
                ),
            ),
            2
        )
    )

    fun executeRoutine(id: Int) {
        // Cargar la rutina en el state.
        uiState = uiState.copy(cycles = cycleListPrueba)
    }

    fun setPage(page: Int) {
        uiState = uiState.copy(page = page)
    }

    fun next() {
        if (uiState.hasNext) {
            uiState = if (uiState.exerciseIndex < uiState.cycles[uiState.cycleIndex].exList.size - 1) {
                uiState.copy(exerciseIndex = uiState.exerciseIndex + 1)
            } else if (uiState.cycleRepetition < uiState.cycles[uiState.cycleIndex].cycleReps - 1) {
                uiState.copy(exerciseIndex = 0, cycleRepetition = uiState.cycleRepetition + 1)
            } else {
                uiState.copy(exerciseIndex = 0, cycleRepetition = 0, cycleIndex = uiState.cycleIndex + 1)
            }
        }
    }

    fun previous() {
        if (uiState.hasPrevious) {
            uiState = if (uiState.exerciseIndex > 0) {
                uiState.copy(exerciseIndex = uiState.exerciseIndex - 1)
            } else if (uiState.cycleRepetition > 0) {
                uiState.copy(exerciseIndex = uiState.cycles[uiState.cycleIndex].exList.size - 1, cycleRepetition = uiState.cycleRepetition - 1)
            } else {
                uiState.copy(exerciseIndex = uiState.cycles[uiState.cycleIndex - 1].exList.size - 1, cycleRepetition = uiState.cycles[uiState.cycleIndex - 1].cycleReps - 1, cycleIndex = uiState.cycleIndex - 1)
            }
        }
    }

    fun finish() {
        uiState = uiState.copy(finished = true)
    }
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as StronkApplication)
                ExecuteViewModel(application.routineRepository)
            }
        }
    }
}