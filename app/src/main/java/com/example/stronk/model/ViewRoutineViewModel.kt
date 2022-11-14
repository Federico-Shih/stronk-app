package com.example.stronk.model

import android.content.Context
import android.content.Intent
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.stronk.StronkApplication
import com.example.stronk.network.repositories.RoutineRepository
import com.example.stronk.state.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class ViewRoutineViewModel(private val routineRepository: RoutineRepository) : ViewModel() {
    var uiState by mutableStateOf(ViewRoutineState())

    val routinePrueba = Routine(
        id = 1,
        name = "Abdominales en 15 minutos!",
        description = "Este es un entrenamiento de abdominales que te ayudará a fortalecer tu abdomen y a quemar grasa abdominal.",
        creationDate = Date(34572452467),
        rating = 4,
        user = UserRoutine(1, "Juan", "https://picsum.photos/100", Date(34572452467)),
        category = Category(1,"FullBody",null),
        difficulty = "Advanced"
    )

    val cycleListPrueba = listOf(
        CycleInfo(
            "Sugerida por copilot",
            listOf(
                ExInfo(
                    "Pushups",
                    10,
                    3,
                    "https://images.ecestaticos.com/WAot9QyeV2vzRuE1gVu55WLdv7Y=/0x0:0x0/1200x900/filters:fill(white):format(jpg)/f.elconfidencial.com%2Foriginal%2Fb3c%2Fc7c%2Fff6%2Fb3cc7cff6cc1ee44df172f15afa3e4f9.jpg",
                    "Este es un ejercicio que se realiza ejercitando aaaaaaaaaaaaaaaaaaaaaaaaaaaaa aaaaaaaaaaa aaaaaaaaaa aaa           aaaaaaaaaaaaa aaaaaaaaaaaaaaa"
                ),
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

    fun initialize() {
        uiState = uiState.copy(
            routine = Routine(0, "", "", Date(0), 0, "", UserRoutine(0, "", "", Date(0)), Category(0,"",null)),
            loadState = ApiState(ApiStatus.LOADING, ""),
            cycles = listOf(),
            faved = false,
            showRatingDialog = false,
        )
    }

    fun fetchRoutine(routineId: Int) {
        viewModelScope.launch {
            uiState = uiState.copy(cycles = cycleListPrueba, routine = routinePrueba, faved = true)

            runCatching {
                runCatching {
                    routineRepository.getRoutine(routineId)
                }.onSuccess {
                    routineData ->
                    runCatching {
                        routineRepository.getRoutineCycles(routineData.id)
                    }.onSuccess {
                        cycles ->
                        uiState = uiState.copy(routine = routineData.asModel(), cycles = cycles)
                    }
                }
            }.onSuccess {
                uiState = uiState.copy(loadState = ApiState(ApiStatus.SUCCESS, "OK"))
            }.onFailure {
                uiState = uiState.copy(loadState = ApiState(ApiStatus.FAILURE, it.message ?: "Unknown error"))
            }
        }
    }

    fun favRoutine() {
        //TODO: fav/unfav routine in database
    }

    fun rateRoutine(rating: Int) {
        //TODO: rate routine in database
    }

    fun shareRoutine(context: Context) {
        val intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, "https://www.stronk.com/routines/${uiState.routine.id}")
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(intent, null)
        context.startActivity(shareIntent)
    }

    fun showRatingDialog() {
        uiState = uiState.copy(showRatingDialog = true)
    }

    fun hideRatingDialog() {
        uiState = uiState.copy(showRatingDialog = false)
    }
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as StronkApplication)
                ViewRoutineViewModel(application.routineRepository)
            }
        }
    }
}