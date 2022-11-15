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
import com.example.stronk.network.repositories.RoutineRepository
import com.example.stronk.state.Category
import com.example.stronk.state.CategoryInfo
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
        val allRoutines: MutableList<List<Routine>> = mutableListOf()
        routinesJob = viewModelScope.launch {
            runCatching {
                getCategories()
                uiState.categories.forEach {
                    runCatching {
                        routineRepository.getRoutines(size = 2, category = it.id)
                    }.onSuccess { result ->
                        val newCategoryInfoList = uiState.categories
                        val index = newCategoryInfoList.indexOf(it)
                        newCategoryInfoList[index].routines.addAll(result.content.map { it.asModel() })
                        newCategoryInfoList[index].isLastPage = result.isLastPage
                        newCategoryInfoList[index].pages = 1
                        uiState = uiState.copy(
                            categories = newCategoryInfoList
                        )
                    }.onFailure { throw it }
                }
            }.onSuccess {
                uiState = uiState.copy(
                    loadState = ApiState(ApiStatus.SUCCESS)
                )
            }.onFailure {
                uiState = uiState.copy(
                    loadState = ApiState(
                        ApiStatus.FAILURE,
                        "Falló el fetch de Rutinas ${it.message}"
                    )
                )
            }
        }
    }

    fun searchRoutines(search: String) {
        if(search.isNotEmpty()) {
            routinesJob = viewModelScope.launch {
                runCatching {
                    routineRepository.getRoutines(size = 10, page = 0, search = search)
                }.onSuccess { result ->
                    uiState = uiState.copy(searchedRoutines = result.content.map { it.asModel() }, searching = true)
                }
            }
        } else{
            uiState = uiState.copy(searchedRoutines = listOf(), searching = false)
        }
    }

    fun getMoreRoutines(categoryId: Int) {
        val category = uiState.categories.find { c -> c.id == categoryId } ?: uiState.categories[0]
        routinesJob = viewModelScope.launch {
            runCatching {
                routineRepository.getRoutines(size = 2, category = category.id, page = category.pages)
            }.onSuccess { result ->
                val newCategoryInfoList = uiState.categories
                val index = newCategoryInfoList.indexOf(category)
                newCategoryInfoList[index].routines.addAll(result.content.map { it.asModel() })
                newCategoryInfoList[index].isLastPage = result.isLastPage
                newCategoryInfoList[index].pages += 1
                uiState = uiState.copy(
                    categories = newCategoryInfoList,
                    loadState = ApiState(ApiStatus.SUCCESS, "lol") //esta linea NO está al pedo
                )
            }.onFailure { throw it }

        }
    }

    private suspend fun getCategories() {
        var categories: List<Category>? = listOf()
        runCatching {
            categories = routineRepository.getCategories()
        }.onSuccess {
            val newCategoryInfoList: MutableList<CategoryInfo> = mutableListOf()
            categories?.forEach {
                newCategoryInfoList.add(CategoryInfo(it.id, it.name, 0, mutableListOf(), false))
            }
            uiState = uiState.copy(categories = newCategoryInfoList)
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