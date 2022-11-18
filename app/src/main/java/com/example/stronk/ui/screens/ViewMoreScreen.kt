package com.example.stronk.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.stronk.R
import com.example.stronk.model.ExploreViewModel
import com.example.stronk.model.MyRoutinesViewModel
import com.example.stronk.network.PreferencesManager
import com.example.stronk.state.Category
import com.example.stronk.state.CategoryInfo
import com.example.stronk.state.nameStringResourceId
import com.example.stronk.ui.components.LoadDependingContent
import com.example.stronk.ui.components.RoutineButtonGroup


@Composable
fun ViewMoreScreen(
    onNavigateToViewRoutine: (categortId: Int) -> Unit,
    exploreViewModel: ExploreViewModel? = null,
    myRoutinesViewModel: MyRoutinesViewModel? = null,
    isFavorite: Boolean = false,
) {
    if (exploreViewModel != null) {
        val state = exploreViewModel.uiState
        val category = state.categories[state.categoryViewMore ?: 0]
        LoadDependingContent(loadState = state.loadState) {
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                RoutineButtonGroup(
                    routineList = category.routines,
                    title = stringResource(id = (Category(0,category.name,null).nameStringResourceId)),
                    onNavigateToViewRoutine = onNavigateToViewRoutine,
                    onGetMoreRoutines = { exploreViewModel.getMoreRoutines(category.id) },
                    isLastPage = category.isLastPage,
                    wantsList = (state.viewPreference == PreferencesManager.ViewPreference.LIST),
                    showButtonAtEnd = true,
                )
            }
        }
    } else if (myRoutinesViewModel != null) {
        val state = myRoutinesViewModel.uiState
        LoadDependingContent(loadState = state.loadState) {
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                RoutineButtonGroup(
                    routineList = if (isFavorite) state.favouriteRoutines else state.myRoutines,
                    title = if (isFavorite) stringResource(R.string.FavRoutines) else stringResource(
                        R.string.MyRoutines
                    ),
                    onNavigateToViewRoutine = onNavigateToViewRoutine,
                    onGetMoreRoutines = {
                        if (isFavorite) {
                            myRoutinesViewModel.moreFavouriteRoutines()
                        } else {
                            myRoutinesViewModel.moreMyRoutines()
                        }
                    },
                    isLastPage = if (isFavorite) state.isLastPageFav else state.isLastPageMyRoutines,
                    wantsList = (state.viewPreference == PreferencesManager.ViewPreference.LIST),
                    showButtonAtEnd = true
                )
            }
        }
    }
}