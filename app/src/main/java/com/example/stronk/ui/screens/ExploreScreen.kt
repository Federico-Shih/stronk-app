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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.stronk.MainScreens
import com.example.stronk.R
import com.example.stronk.model.ExploreViewModel
import com.example.stronk.state.ExploreState
import com.example.stronk.state.foundSomething
import com.example.stronk.state.searching
import com.example.stronk.ui.components.LoadDependingContent
import com.example.stronk.ui.components.OrderBy
import com.example.stronk.ui.components.RoutineButtonGroup
import com.example.stronk.ui.components.SearchBar

@Composable
fun ExploreScreen(
    onNavigateToViewRoutine: (routineId: Int) -> Unit,
    exploreViewModel: ExploreViewModel = viewModel(factory = ExploreViewModel.Factory)
) {
    val state = exploreViewModel.uiState

    LoadDependingContent(loadState = state.loadState) {
        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
            Row(modifier = Modifier.fillMaxWidth()) {
                SearchBar(
                    label = stringResource(id = R.string.search_for_routines),
                    onValueChanged = { s -> exploreViewModel.searchRoutines(s) })

                IconButton(
                    onClick = { exploreViewModel.startCleanFilters() }, modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .size(70.dp)
                ) {
                    Icon(
                        imageVector = if (state.filtering) {
                            Icons.Outlined.FilterAlt
                        } else {
                            Icons.Filled.FilterAlt
                        },
                        contentDescription = stringResource(id = R.string.filter)
                    )
                }
            }
            if(state.filtering) {
                Row(modifier = Modifier.fillMaxWidth().align(Alignment.CenterHorizontally))
                {
                    OrderBy(    //hacer lo de stringResource(id = R.string.score)
                        optionsList = listOf(Pair("Beginner") {
                            exploreViewModel.setDifficultyAndReload(
                                1
                            )
                        },
                            Pair("Intermediate") {
                                exploreViewModel.setDifficultyAndReload(
                                    2
                                )
                            },
                            Pair("Advanced") {
                                exploreViewModel.setDifficultyAndReload(
                                    3
                                )
                            },
                    ))
                }
                Row(modifier = Modifier.fillMaxWidth().align(Alignment.CenterHorizontally))
                {
                    OrderBy(    //hacer lo de stringResource(id = R.string.score)
                        optionsList = listOf(Pair("Awful"){ exploreViewModel.setScoreAndReload(0) },
                        Pair("Bad"){ exploreViewModel.setScoreAndReload(1) },
                        Pair("So so"){ exploreViewModel.setScoreAndReload(2) },
                        Pair("Good"){ exploreViewModel.setScoreAndReload(3) },
                        Pair("Great"){ exploreViewModel.setScoreAndReload(4) },
                        Pair("Excellent"){ exploreViewModel.setScoreAndReload(5) }))
                }
            }
            Row(modifier = Modifier.fillMaxWidth().align(Alignment.CenterHorizontally)) {
                OrderBy(
                    optionsList = listOf(Pair(stringResource(id = R.string.name)) {
                        exploreViewModel.setOrderAndReload(
                            "name"
                        )
                        },
                        Pair(stringResource(id = R.string.score)) {
                            exploreViewModel.setOrderAndReload(
                                "score"
                            )
                        },
                        Pair(stringResource(id = R.string.difficulty)) {
                            exploreViewModel.setOrderAndReload(
                                "difficulty"
                            )
                        },
                        Pair(stringResource(id = R.string.date)) {
                            exploreViewModel.setOrderAndReload(
                                "date"
                            )
                        }))
                OrderBy(
                    optionsList= listOf(Pair("asc") { exploreViewModel.setAscOrDescAndReload("asc")},
                    Pair("desc") { exploreViewModel.setAscOrDescAndReload("desc")})
                )
            }
            if (state.searching) {
                if (state.foundSomething) {
                    RoutineButtonGroup(
                        routineList = state.searchedRoutines,
                        title = stringResource(id = R.string.searching),
                        onNavigateToViewRoutine = onNavigateToViewRoutine,
                        onGetMoreRoutines = { /* Para qué, que no aparezca directamente */ },
                        showButton = false
                    )
                } else {
                    Text(
                        stringResource(id = R.string.nothing_found),
                        modifier = Modifier.padding(10.dp)
                    )
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