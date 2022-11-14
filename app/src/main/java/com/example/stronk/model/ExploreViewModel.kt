package com.example.stronk.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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


    fun getInitialRoutines()
    {
        getCategories()
        val isLastOne: MutableList<Boolean> = mutableListOf()
        val allRoutines: MutableList<List<RoutineData>> = mutableListOf()
        uiState.categories.forEach {
            routinesJob = viewModelScope.launch {
                runCatching {
                    val paginated = routineRepository.getRoutines(size = 2, category = it.second)
                    allRoutines.add(paginated.content)
                    isLastOne.add(paginated.isLastPage)
                }.onSuccess {
                    // wtf pongo acá?
                }.onFailure {
                    println("Oops se rompió algo no sé qué rutina: $it")
                    //TODO hacerlo con el String Resource
                    uiState = uiState.copy(loadState = ApiState(ApiStatus.FAILURE, "Falló el fetch ${it.message}"))
                }
            }
        }
        uiState = uiState.copy(routineByCategory = allRoutines, isLastOne = isLastOne, loadState = ApiState(ApiStatus.SUCCESS))
    }

    fun getMoreRoutines(routineIndex: Int)
    {

    }

    fun getCategories()
    {
        var categories: List<Category>? = listOf()
        routinesJob = viewModelScope.launch {
            runCatching {
                categories = routineRepository.getCategories()
            }.onSuccess {
                val listCatIds: MutableList<Pair<String, Int>> = mutableListOf()
                categories?.forEach{
                    listCatIds.add(Pair(it.name, it.id))
                }
                val array = IntArray(listCatIds.size) {0}
                uiState = uiState.copy(categories = listCatIds, pages = array.toList())
            }.onFailure {
                println("Oops se rompió algo no sé qué de categorías")
            }
        }
    }
}