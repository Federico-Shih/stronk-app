package com.example.stronk.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.TabRowDefaults.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AssignmentLate
import androidx.compose.material.icons.filled.Feedback
import androidx.compose.material.icons.filled.Warning
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.stronk.R
import com.example.stronk.ui.components.RoutineButton
import com.example.stronk.model.MyRoutinesViewModel
import com.example.stronk.state.Routine
import com.example.stronk.ui.components.LoadDependingContent
import com.example.stronk.ui.components.Refreshable

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
                    state.isLastPageMyRoutines,
                    stringResource(id = R.string.no_my_routines)
                )
                Divider()
                RoutinesList(
                    state.favouriteRoutines,
                    stringResource(R.string.FavRoutines),
                    { myRoutinesViewModel.moreFavouriteRoutines() },
                    onNavigateToViewRoutine,
                    state.isLastPageFav,
                    stringResource(id = R.string.no_fav_routines)
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
    isLastPage: Boolean = false,
    noRoutinesMessage: String = stringResource(R.string.no_routines_message)
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
                .padding(bottom = 5.dp, start = 10.dp, end = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.h5,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(bottom = 4.dp)
            )
            if (!isLastPage) {
                Button(onClick = { onShowMore() }, modifier = Modifier) {
                    Text(
                        text = stringResource(R.string.ShowMore).uppercase(),
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
            if (routines.isEmpty()) {
                Row(modifier = Modifier.padding(start = 4.dp)) {
                    Icon(
                        imageVector = Icons.Filled.Feedback,
                        contentDescription = "no routines",
                        modifier = Modifier
                            .size(24.dp)
                            .alignByBaseline()
                    )
                    Text(
                        text = noRoutinesMessage,
                        modifier = Modifier
                            .alignByBaseline()
                            .padding(start = 10.dp)
                    )
                }
            }
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



