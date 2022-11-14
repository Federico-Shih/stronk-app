package com.example.stronk.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.stronk.StronkApplication
import com.example.stronk.network.DataSourceException
import com.example.stronk.network.dtos.Paginated
import com.example.stronk.network.dtos.RoutineData
import com.example.stronk.network.repositories.RoutineRepository
import com.example.stronk.state.Category
import com.example.stronk.state.ExploreState
import com.example.stronk.state.Routine
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class ExploreViewModel(private val routineRepository: RoutineRepository) : ViewModel() {
    var uiState by mutableStateOf(ExploreState())
        private set

    private var routinesJob: Job? = null

    init {
        getInitialRoutines()
    }

    fun getInitialRoutines() {

        val isLastOne: MutableList<Boolean> = mutableListOf()
        val allRoutines: MutableList<List<RoutineData>> = mutableListOf()
        routinesJob = viewModelScope.launch {
            runCatching {
                getCategories()
                uiState.categories.forEach {
                    runCatching {
                        routineRepository.getRoutines(size = 2, category = it.second)
                    }.onSuccess { result ->
                        allRoutines.add(result.content)
                        isLastOne.add(result.isLastPage)
                    }.onFailure { throw it }
                }
            }.onSuccess {
                uiState = uiState.copy(
                    routineByCategory = allRoutines,
                    isLastOne = isLastOne,
                    loadState = ApiState(ApiStatus.SUCCESS)
                )
            }.onFailure {
                uiState = uiState.copy(
                    loadState = ApiState(
                        ApiStatus.FAILURE,
                        "Falló el fetch ${it.message}"
                    )
                )
            }
        }
    }

    fun getMoreRoutines(routineIndex: Int) {

    }

    private suspend fun getCategories() {
        var categories: List<Category>? = listOf()
        runCatching {
            categories = routineRepository.getCategories()
        }.onSuccess {
            val listCatIds: MutableList<Pair<String, Int>> = mutableListOf()
            categories?.forEach {
                listCatIds.add(Pair(it.name, it.id))
            }
            val array = IntArray(listCatIds.size) { 0 }
            uiState = uiState.copy(categories = listCatIds, pages = array.toList())
        }.onFailure { throw it }

    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as StronkApplication)
                ExploreViewModel(application.routineRepository)
            }
        }
    }
}