package com.example.stronk.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.stronk.R
import com.example.stronk.RoutineButton
import com.example.stronk.model.MyRoutinesViewModel
import com.example.stronk.state.Routine
import com.example.stronk.ui.components.LoadDependingContent
import com.example.stronk.ui.components.Refreshable
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
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
                RoutinesList(
                    state.myRoutines,
                    stringResource(R.string.MyRoutines),
                    { myRoutinesViewModel.moreMyRoutines() },
                    onNavigateToViewRoutine,
                    state.isLastPageMyRoutines
                )
                RoutinesList(
                    state.favouriteRoutines,
                    stringResource(R.string.FavRoutines),
                    { myRoutinesViewModel.moreFavouriteRoutines() },
                    onNavigateToViewRoutine,
                    state.isLastPageFav
                )
            }
        }
    }
}

@Composable
fun RoutinesList(
    routines: List<Routine> = listOf(),
    title: String = "Rutinas",
    onShowMore: () -> Unit = {},
    onNavigateToViewRoutine: (routineId: Int) -> Unit = {},
    isLastPage: Boolean = false
) {
    Column(
        modifier = Modifier
            .padding(10.dp)
            .wrapContentHeight()
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 5.dp), horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.h5,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(bottom = 4.dp)
                    .alignByBaseline()
            )
            if (!isLastPage) {
                Button(onClick = { onShowMore() }, modifier = Modifier.alignByBaseline()) {
                    Text(
                        text = stringResource(R.string.ShowMore).uppercase(),
                        style = MaterialTheme.typography.body1,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            routines.forEach { routine ->
                RoutineButton(
                    routine.id,
                    R.drawable.abdos,
                    routine.name,
                    onNavigateToViewRoutine = onNavigateToViewRoutine,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(125.dp)
                        .padding(bottom = 3.dp)
                )
            }
        }

    }

}



