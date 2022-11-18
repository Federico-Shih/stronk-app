package com.example.stronk.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TabRowDefaults.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.stronk.R
import com.example.stronk.model.MyRoutinesViewModel
import com.example.stronk.network.PreferencesManager
import com.example.stronk.ui.components.*

@Composable
fun MyRoutinesScreen(
    onNavigateToViewRoutine: (routineId: Int) -> Unit,
    myRoutinesViewModel: MyRoutinesViewModel = viewModel(factory = MyRoutinesViewModel.Factory),
    onNavigateToViewMore: (type: String) -> Unit,
) {
    val state = myRoutinesViewModel.uiState

    Refreshable(refreshFunction = { myRoutinesViewModel.forceFetchRoutines() }) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            LoadDependingContent(loadState = state.loadState) {
                RoutineButtonGroup(
                    routineList = if (state.myRoutines.size > myRoutinesViewModel.myRoutinesPageSize)
                        state.myRoutines.subList(
                            0,
                            myRoutinesViewModel.myRoutinesPageSize
                        ) else state.myRoutines,
                    title = stringResource(R.string.MyRoutines),
                    onNavigateToViewRoutine = onNavigateToViewRoutine,
                    onGetMoreRoutines = { onNavigateToViewMore("myroutines") },
                    isLastPage = state.myRoutines.size < myRoutinesViewModel.myRoutinesPageSize,
                    noRoutinesMessage = stringResource(id = R.string.no_my_routines),
                    wantsList = state.viewPreference == PreferencesManager.ViewPreference.LIST
                )
                Divider(modifier = Modifier.padding(top = 5.dp))
                RoutineButtonGroup(
                    routineList = if (state.favouriteRoutines.size > myRoutinesViewModel.favoritePageSize)
                        state.favouriteRoutines.subList(
                            0,
                            myRoutinesViewModel.favoritePageSize
                        ) else state.favouriteRoutines,
                    title = stringResource(R.string.FavRoutines),
                    onNavigateToViewRoutine = onNavigateToViewRoutine,
                    onGetMoreRoutines = { onNavigateToViewMore("favourites") },
                    isLastPage = state.favouriteRoutines.size < myRoutinesViewModel.favoritePageSize,
                    noRoutinesMessage = stringResource(id = R.string.no_fav_routines),
                    wantsList = state.viewPreference == PreferencesManager.ViewPreference.LIST
                )
            }
        }
    }
}




