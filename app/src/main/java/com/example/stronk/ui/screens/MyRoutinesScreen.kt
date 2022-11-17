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
    myRoutinesViewModel: MyRoutinesViewModel = viewModel(factory = MyRoutinesViewModel.Factory)
) {
    val state = myRoutinesViewModel.uiState

    Refreshable(refreshFunction = { myRoutinesViewModel.forceFetchRoutines() }) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            LoadDependingContent(loadState = state.loadState) {
                // RoutineLayoutButton(layoutSelected = state.viewPreference, changeLayout ={preference-> myRoutinesViewModel.changeViewPreference(preference)}  )
                if(state.viewPreference==PreferencesManager.ViewPreference.LIST) {
                    RoutineButtonList(
                        state.myRoutines,
                        stringResource(R.string.MyRoutines),
                        { myRoutinesViewModel.moreMyRoutines() },
                        onNavigateToViewRoutine,
                        state.isLastPageMyRoutines,
                        stringResource(id = R.string.no_my_routines)
                    )
                    Divider()
                    RoutineButtonList(
                        state.favouriteRoutines,
                        stringResource(R.string.FavRoutines),
                        { myRoutinesViewModel.moreFavouriteRoutines() },
                        onNavigateToViewRoutine,
                        state.isLastPageFav,
                        stringResource(id = R.string.no_fav_routines)
                    )
                }else{
                    RoutineButtonGrid(
                        routineList = state.myRoutines,
                        title = stringResource(R.string.MyRoutines),
                        onNavigateToViewRoutine = onNavigateToViewRoutine,
                        onGetMoreRoutines = { myRoutinesViewModel.moreMyRoutines() },
                        isLastPage = state.isLastPageMyRoutines,
                        noRoutinesMessage = stringResource(id = R.string.no_my_routines)
                    )
                    Divider()
                    RoutineButtonGrid(
                        routineList = state.favouriteRoutines,
                        title = stringResource(R.string.FavRoutines),
                        onNavigateToViewRoutine = onNavigateToViewRoutine,
                        onGetMoreRoutines = { myRoutinesViewModel.moreFavouriteRoutines() },
                        isLastPage = state.isLastPageFav,
                        noRoutinesMessage = stringResource(id = R.string.no_fav_routines)
                    )
                }
            }
        }
    }
}




