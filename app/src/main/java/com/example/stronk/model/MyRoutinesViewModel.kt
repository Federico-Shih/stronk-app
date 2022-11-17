package com.example.stronk.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.stronk.StronkApplication
import com.example.stronk.network.PreferencesManager
import com.example.stronk.network.repositories.RoutineRepository
import com.example.stronk.state.MyRoutinesState
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MyRoutinesViewModel(
    private val routineRepository: RoutineRepository,
    private val preferencesManager: PreferencesManager
) : MainNavViewModel() {
    var uiState by mutableStateOf(MyRoutinesState())
        private set

    override val viewPreference: PreferencesManager.ViewPreference
        get() = uiState.viewPreference

    private var fetchJob: Job? = null
    private val favoritePageSize = 3
    private val myRoutinesPageSize = 3

    init {
        getViewPreference()
        fetchFirstRoutines()
    }

    private fun getViewPreference() {
        uiState = uiState.copy(viewPreference = preferencesManager.fetchViewPreferenceMyRoutines())
    }

    override fun changeViewPreference(viewPreference: PreferencesManager.ViewPreference) {
        preferencesManager.saveViewPreferenceMyRoutines(viewPreference)
        uiState = uiState.copy(viewPreference = viewPreference)
    }

    fun moreFavouriteRoutines() {
        if (uiState.isLastPageFav) {
            return
        }
        val routineList = uiState.favouriteRoutines
        fetchJob?.cancel()
        fetchJob = viewModelScope.launch {
            runCatching {
                routineRepository.getFavouriteRoutines(
                    page = uiState.favouriteRoutinesPage,
                    size = favoritePageSize
                )
            }.onSuccess { routines ->
                val auxRoutines =
                    (if (routineList.size % favoritePageSize != 0) routineList.slice(0 until (routineList.size - routineList.size % favoritePageSize)) else routineList) + routines.content.map { it.asModel() }
                uiState = uiState.copy(
                    favouriteRoutines = auxRoutines,
                    favouriteRoutinesPage = auxRoutines.size / favoritePageSize,
                    isLastPageFav = routines.isLastPage,
                    loadState = ApiState(ApiStatus.SUCCESS, "")
                )
            }.onFailure { e ->
                uiState = uiState.copy(
                    loadState = ApiState(
                        ApiStatus.FAILURE,
                        "Falló el fetch de myRoutines ${e.message}"
                    )
                )
                return@launch
            }
        }
    }

    fun moreMyRoutines() {
        if (uiState.isLastPageMyRoutines) {
            return
        }
        val routineList = uiState.myRoutines
        fetchJob?.cancel()
        fetchJob = viewModelScope.launch {

            runCatching {
                routineRepository.getMyRoutines(
                    page = uiState.myRoutinesPage,
                    size = myRoutinesPageSize
                )
            }.onSuccess { routines ->
                val auxRoutines =
                    (if (routineList.size % myRoutinesPageSize != 0) routineList.slice(0 until (routineList.size - routineList.size % myRoutinesPageSize)) else routineList) + routines.content.map { it.asModel() }
                uiState = uiState.copy(
                    myRoutines = auxRoutines,
                    myRoutinesPage = auxRoutines.size / myRoutinesPageSize,
                    isLastPageMyRoutines = routines.isLastPage,
                    loadState = ApiState(ApiStatus.SUCCESS, "")
                )
            }.onFailure { e ->
                uiState = uiState.copy(
                    loadState = ApiState(
                        ApiStatus.FAILURE,
                        "Falló el fetch de myRoutines ${e.message}"
                    )
                )
                return@launch
            }
        }
    }

    suspend fun forceFetchRoutines() {
        runCatching {
            routineRepository.getMyRoutines(page = 0, size = myRoutinesPageSize)
        }.onSuccess { routines ->
            uiState = uiState.copy(
                myRoutines = routines.content.map { it.asModel() },
                myRoutinesPage = 1,
                isLastPageMyRoutines = routines.isLastPage
            )
        }.onFailure { e ->
            uiState = uiState.copy(
                loadState = ApiState(
                    ApiStatus.FAILURE,
                    "Falló el fetch de myRoutines ${e.message}"
                )
            )
            return
        }
        runCatching {
            routineRepository.getFavouriteRoutines(page = 0, size = favoritePageSize)
        }.onSuccess { routines ->
            uiState = uiState.copy(
                favouriteRoutines = routines.content.map { it.asModel() },
                favouriteRoutinesPage = 1,
                isLastPageFav = routines.isLastPage,
                loadState = ApiState(ApiStatus.SUCCESS)
            )
        }.onFailure { e ->
            uiState = uiState.copy(
                loadState = ApiState(
                    ApiStatus.FAILURE,
                    "Falló el fetch de Favorites ${e.message}"
                )
            )
        }
    }

    fun fetchFirstRoutines() {
        if (uiState.myRoutines.isNotEmpty() || uiState.favouriteRoutines.isNotEmpty()) {
            return
        }
        fetchJob?.cancel()
        fetchJob = viewModelScope.launch {
            forceFetchRoutines()
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as StronkApplication)
                MyRoutinesViewModel(application.routineRepository, application.preferencesManager)
            }
        }
    }
}