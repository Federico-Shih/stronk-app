package com.example.stronk.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.stronk.model.ExploreViewModel
import com.example.stronk.network.PreferencesManager
import com.example.stronk.state.CategoryInfo
import com.example.stronk.ui.components.LoadDependingContent
import com.example.stronk.ui.components.RoutineButtonGroup

@Composable
fun OneCategoryScreen(onNavigateToViewRoutine: (categortId: Int) -> Unit, exploreViewModel: ExploreViewModel, category: CategoryInfo) {
    val state = exploreViewModel.uiState
    LoadDependingContent(loadState = state.loadState) {
        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
            RoutineButtonGroup(
                routineList = category.routines,
                title = category.name,
                onNavigateToViewRoutine = onNavigateToViewRoutine,
                onGetMoreRoutines = { exploreViewModel.getMoreRoutines(category.id) },
                isLastPage = category.isLastPage,
                wantsList = (state.viewPreference== PreferencesManager.ViewPreference.LIST)
            )
        }
    }
}