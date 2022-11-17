package com.example.stronk.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Filter
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material.icons.outlined.FilterAlt
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.stronk.MainScreens
import com.example.stronk.R
import com.example.stronk.misc.QrCodeGenerator
import com.example.stronk.model.ExploreViewModel
import com.example.stronk.state.ExploreState
import com.example.stronk.state.foundSomething
import com.example.stronk.state.searching
import com.example.stronk.ui.components.*

@Composable
fun ExploreScreen(
    onNavigateToViewRoutine: (routineId: Int) -> Unit,
    exploreViewModel: ExploreViewModel = viewModel(factory = ExploreViewModel.Factory)
) {
    val state = exploreViewModel.uiState

    LoadDependingContent(loadState = state.loadState) {
        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)) {
                SearchBar(
                    label = stringResource(id = R.string.search_for_routines),
                    onValueChanged = { s -> exploreViewModel.searchRoutines(s) })

                IconButton(
                    onClick = { exploreViewModel.showFilterMenu() }, modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .size(70.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.FilterAlt,
                        contentDescription = stringResource(id = R.string.filter)
                    )
                }
            }
            if(state.showFilters) {
                Dialog(onDismissRequest = { exploreViewModel.hideFilterMenu() }) {
                    Card(backgroundColor = MaterialTheme.colors.background) {
                        Column(modifier = Modifier
                            .padding(16.dp)
                            .wrapContentHeight()) {
                            Column(modifier = Modifier
                                .height(500.dp)
                                .verticalScroll(rememberScrollState())) {
                                Text(
                                    text = "${stringResource(id = R.string.OrderBy)}:",
                                    style = MaterialTheme.typography.h6,
                                    modifier = Modifier.padding(bottom = 10.dp)
                                )
                                OrderBy(
                                    title = stringResource(id = R.string.criteria),
                                    optionsList = listOf(
                                        Pair(stringResource(id = R.string.default_)) { exploreViewModel.setOrderAndReload() },
                                        Pair(stringResource(id = R.string.name)) { exploreViewModel.setOrderAndReload("name") },
                                        Pair(stringResource(id = R.string.score)) { exploreViewModel.setOrderAndReload("score") },
                                        Pair(stringResource(id = R.string.difficulty)) { exploreViewModel.setOrderAndReload("difficulty") },
                                        Pair(stringResource(id = R.string.date)) { exploreViewModel.setOrderAndReload("date") }))
                                OrderBy(
                                    title = stringResource(id = R.string.direction),
                                    optionsList= listOf(Pair("asc") { exploreViewModel.setAscOrDescAndReload("asc")},
                                        Pair("desc") { exploreViewModel.setAscOrDescAndReload("desc")}),
                                )
                                Divider(modifier = Modifier.padding(vertical = 10.dp))
                                Text(
                                    text = "${stringResource(id = R.string.filter)}:",
                                    style = MaterialTheme.typography.h6,
                                    modifier = Modifier.padding(bottom = 10.dp)
                                )
                                OrderBy(
                                    title = stringResource(id = R.string.difficulty),
                                    optionsList = listOf(
                                        Pair(stringResource(id = R.string.no_filter)) { exploreViewModel.setDifficultyAndReload(null) },
                                        Pair(stringResource(id = R.string.beginner)) { exploreViewModel.setDifficultyAndReload("beginner") },
                                        Pair(stringResource(id = R.string.intermediate)) { exploreViewModel.setDifficultyAndReload("intermediate") },
                                        Pair(stringResource(id = R.string.advanced)) { exploreViewModel.setDifficultyAndReload("advanced") },))
                                OrderBy(
                                    title = stringResource(id = R.string.score),
                                    optionsList = listOf(
                                        Pair(stringResource(id = R.string.no_filter)) { exploreViewModel.setScoreAndReload(null) },
                                        Pair(stringResource(id = R.string.score_0)){ exploreViewModel.setScoreAndReload(0) },
                                        Pair(stringResource(id = R.string.score_1)){ exploreViewModel.setScoreAndReload(1) },
                                        Pair(stringResource(id = R.string.score_2)){ exploreViewModel.setScoreAndReload(2) },
                                        Pair(stringResource(id = R.string.score_3)){ exploreViewModel.setScoreAndReload(3) },
                                        Pair(stringResource(id = R.string.score_4)){ exploreViewModel.setScoreAndReload(4) },
                                        Pair(stringResource(id = R.string.score_5)){ exploreViewModel.setScoreAndReload(5) }))
                            }
                            Row(
                                modifier = Modifier
                                    .padding(top = 10.dp)
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.End
                            ) {
                                Button(onClick = {
                                    exploreViewModel.hideFilterMenu()
                                }) {
                                    Text(text = stringResource(id = R.string.apply).uppercase())
                                }
                            }
                        }
                    }
                }
            }
            if (state.searching) {
                if (state.foundSomething) {
                    RoutineButtonGroup(
                        routineList = state.searchedRoutines,
                        title = stringResource(id = R.string.searching),
                        onNavigateToViewRoutine = onNavigateToViewRoutine,
                        onGetMoreRoutines = {},
                        showButton = false
                    )
                } else {
                    NoRoutinesMessage(msg = stringResource(id = R.string.nothing_found))
                }
            } else {
                state.categories.forEach() { category ->
                    if (category.routines.isNotEmpty()) {
                        RoutineButtonGroup(
                            routineList = category.routines,
                            title = category.name,
                            onNavigateToViewRoutine = onNavigateToViewRoutine,
                            onGetMoreRoutines = { exploreViewModel.getMoreRoutines(category.id) },
                            showButton = !category.isLastPage
                        )
                    }
                }
                if(state.categories.isEmpty()) {
                    NoRoutinesMessage(msg = stringResource(id = R.string.nothing_found))
                }
            }
        }
    }

}

@Preview
@Composable
fun PreviewExplore() {
    Box {
        val navController = rememberNavController()
        NavHost(
            navController = navController,
            startDestination = MainScreens.EXPLORE.name
        ) {
            composable(route = MainScreens.EXPLORE.name) {
                ExploreScreen(onNavigateToViewRoutine = { routineId ->
                    navController.navigate("${MainScreens.VIEW_ROUTINE.name}/$routineId")
                })
            }
        }
    }
}