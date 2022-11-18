package com.example.stronk.state

import com.example.stronk.model.ApiState
import com.example.stronk.model.ApiStatus
import com.example.stronk.network.PreferencesManager

data class CategoryInfo(
    val id: Int,
    val name: String,
    var pages: Int,
    val routines: MutableList<Routine>,
    var isLastPage: Boolean
)

data class ExploreState(
    val categories: List<CategoryInfo> = listOf(),
    val searchedRoutines: List<Routine> = listOf(),
    val loadState: ApiState = ApiState(ApiStatus.LOADING, ""),
    val order: String = "id",
    val ascOrDesc: String = "asc",
    val searchString: String = "",
    val showFilters: Boolean = false,
    val difficultyFilter: String? = null,
    val scoreFilter: Int? = null,
    val viewPreference: PreferencesManager.ViewPreference = PreferencesManager.ViewPreference.GRID,
    val categoryViewMore: Int? = null
)

val listForFilterIndex: List<String> = listOf("id", "name", "score", "difficulty", "date")
val listForDifficulty: List<String> = listOf("beginner", "intermediate", "advanced")

val ExploreState.searching: Boolean get() = searchString.isNotEmpty()
val ExploreState.foundSomething: Boolean get() = searchedRoutines.isNotEmpty()
val ExploreState.scoreFilteringIndex: Int get() =  if(scoreFilter==null) 0 else scoreFilter+1
val ExploreState.directionIndex: Int get() = if(ascOrDesc=="asc") 0 else 1
val ExploreState.filterIndex: Int get() = listForFilterIndex.indexOf(order)
val ExploreState.difficultyIndex: Int get() = if(difficultyFilter==null){0} else {listForDifficulty.indexOf(difficultyFilter)+1}