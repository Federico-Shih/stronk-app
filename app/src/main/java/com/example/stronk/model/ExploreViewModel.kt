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
import com.example.stronk.network.PreferencesManager
import com.example.stronk.network.repositories.RoutineRepository
import com.example.stronk.state.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

abstract class MainNavViewModel : ViewModel() {
    abstract val viewPreference: PreferencesManager.ViewPreference
    abstract fun changeViewPreference(viewPreference: PreferencesManager.ViewPreference)
}


class ExploreViewModel(private val routineRepository: RoutineRepository,
private val preferencesManager: PreferencesManager) : MainNavViewModel() {
    var uiState by mutableStateOf(ExploreState())
        private set

    override val viewPreference: PreferencesManager.ViewPreference
        get() = uiState.viewPreference

    private var routinesJob: Job? = null

    init {
        getViewPreference()
        getRoutines()
    }

    fun getRoutines() {
        routinesJob = viewModelScope.launch {
            runCatching {
                getCategories()
                uiState.categories.forEach {
                    runCatching {
                        routineRepository.getRoutines(size = 2, category = it.id, orderBy = uiState.order,
                            direction = uiState.ascOrDesc,
                            difficulty = uiState.difficultyFilter,
                            score = uiState.scoreFilter )
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

    fun setDifficultyAndReload(value: String?)
    {
        uiState = uiState.copy(difficultyFilter = value)
        reload()
    }

    fun setScoreAndReload(value: Int?)
    {
        uiState = uiState.copy(scoreFilter = value)
        reload()
    }

    fun showFilterMenu()
    {
        uiState = uiState.copy(showFilters = true)
    }

    fun hideFilterMenu()
    {
        uiState = uiState.copy(showFilters = false)
    }

    private fun reload()
    {
        if(uiState.searching)
        {
            searchRoutines(uiState.searchString)
        }
        else{
            getRoutines()
        }
    }

    fun setOrderAndReload(order: String = "id")
    {
        uiState = uiState.copy(order = order)
        reload()
    }

    fun setAscOrDescAndReload(ascOrDesc : String)
    {
        uiState = uiState.copy(ascOrDesc = ascOrDesc)
        reload()
    }

    fun searchRoutines(search: String) {
        if(search.isNotEmpty()) {
            routinesJob = viewModelScope.launch {
                runCatching {
                    routineRepository.getRoutines(size = 10, page = 0, search = search,
                        orderBy = uiState.order, direction = uiState.ascOrDesc,
                        difficulty = uiState.difficultyFilter,
                        score = uiState.scoreFilter )
                }.onSuccess { result ->
                    uiState = uiState.copy(searchedRoutines = result.content.map { it.asModel() }, searchString = search)
                }
            }
        } else{
            uiState = uiState.copy(searchedRoutines = listOf(), searchString = "")
        }
    }

    fun getMoreRoutines(categoryId: Int) {
        val category = uiState.categories.find { c -> c.id == categoryId } ?: uiState.categories[0]
        routinesJob = viewModelScope.launch {
            runCatching {
                routineRepository.getRoutines(size = 2, category = category.id, page = category.pages,
                    orderBy = uiState.order, direction = uiState.ascOrDesc,
                    difficulty = uiState.difficultyFilter,
                    score = uiState.scoreFilter )
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

    fun getViewPreference(){
        uiState = uiState.copy( viewPreference = preferencesManager.fetchViewPreferenceExplore() )
    }

    override fun changeViewPreference(viewPreference: PreferencesManager.ViewPreference){
        preferencesManager.saveViewPreferenceExplore(viewPreference)
        uiState = uiState.copy( viewPreference = viewPreference )
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as StronkApplication)
                ExploreViewModel(application.routineRepository, application.preferencesManager)
            }
        }
    }
}