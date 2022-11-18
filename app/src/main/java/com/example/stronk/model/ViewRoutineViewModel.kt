package com.example.stronk.model

import android.content.ClipData
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.runtime.*
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.stronk.StronkApplication
import com.example.stronk.misc.QrCodeGenerator
import com.example.stronk.network.ApiErrorCode
import com.example.stronk.network.DataSourceException
import com.example.stronk.network.dtos.RatingDTO
import com.example.stronk.network.repositories.RoutineRepository
import com.example.stronk.state.*
import kotlinx.coroutines.launch
import java.io.File
import java.util.*

class ViewRoutineViewModel(private val routineRepository: RoutineRepository) : ViewModel() {
    var uiState by mutableStateOf(ViewRoutineState())

    fun initialize() {
        uiState = uiState.copy(
            routine = Routine(
                0,
                "",
                "",
                Date(0),
                0,
                "",
                UserRoutine(0, "", "", Date(0)),
                Category(0, "", null)
            ),
            loadState = ApiState(ApiStatus.LOADING, ""),
            cycles = listOf(),
            faved = false,
            showRatingDialog = false,
        )
    }

    suspend fun forceFetchRoutine(routineId: Int) {
        runCatching {
            runCatching {
                routineRepository.getRoutine(routineId)
            }.onSuccess { routineData ->
                runCatching {
                    routineRepository.getRoutineCycles(routineData.id)
                }.onSuccess { cycles ->
                    uiState = uiState.copy(routine = routineData.asModel(), cycles = cycles)
                }.onFailure {
                    throw it
                }
            }.onFailure {
                throw it
            }
            runCatching {
                routineRepository.getAllFavouriteRoutines()
            }.onSuccess { favourites ->
                if (favourites.any { favourite -> favourite.id == routineId }) {
                    uiState = uiState.copy(faved = true)
                }
            }
        }.onSuccess {
            uiState = uiState.copy(loadState = ApiState(ApiStatus.SUCCESS, "OK"))
        }.onFailure { error ->
            uiState = if (error is DataSourceException) {
                uiState.copy(loadState = ApiState(ApiStatus.FAILURE, "", error.code))
            } else {
                uiState.copy(
                    loadState = ApiState(
                        ApiStatus.FAILURE,
                        "",
                        ApiErrorCode.UNEXPECTED_ERROR.code
                    )
                )
            }
        }
    }

    fun fetchRoutine(routineId: Int): Boolean {
        viewModelScope.launch {
            forceFetchRoutine(routineId)
        }
        return true
    }

    fun toggleFavourite() {
        viewModelScope.launch {
            runCatching {
                if (uiState.faved) {
                    routineRepository.unfavouriteRoutine(uiState.routine.id)
                } else {
                    routineRepository.favouriteRoutine(uiState.routine.id)
                }
            }.onSuccess {
                uiState = uiState.copy(faved = !uiState.faved)
            }.onFailure { error ->
                uiState = if (error is DataSourceException) {
                    uiState.copy(loadState = ApiState(ApiStatus.FAILURE, "", error.code))
                } else {
                    uiState.copy(
                        loadState = ApiState(
                            ApiStatus.FAILURE,
                            "",
                            ApiErrorCode.UNEXPECTED_ERROR.code
                        )
                    )
                }
            }
        }
    }

    fun rateRoutine(rating: Int) {
        viewModelScope.launch {
            uiState = uiState.copy(loadState = ApiState(ApiStatus.LOADING))
            runCatching {
                routineRepository.rateRoutine(uiState.routine.id, RatingDTO(rating))
            }.onSuccess {
                uiState = uiState.copy(loadState = ApiState(ApiStatus.SUCCESS))
            }.onFailure { error ->
                uiState = if (error is DataSourceException) {
                    uiState.copy(loadState = ApiState(ApiStatus.FAILURE, "", error.code))
                } else {
                    uiState.copy(
                        loadState = ApiState(
                            ApiStatus.FAILURE,
                            "",
                            ApiErrorCode.UNEXPECTED_ERROR.code
                        )
                    )
                }
            }
        }
    }

    fun shareRoutineLink(context: Context) {
        val intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, "https://www.stronk.com/routines/${uiState.routine.id}")
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(intent, null)
        context.startActivity(shareIntent)
    }

    fun showRoutineQr() {
        uiState = uiState.copy(showQrDialog = true)
    }

    fun hideRoutineQr() {
        uiState = uiState.copy(showQrDialog = false)
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
                val application =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as StronkApplication)
                ViewRoutineViewModel(application.routineRepository)
            }
        }
    }
}