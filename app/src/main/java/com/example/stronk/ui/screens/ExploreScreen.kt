package com.example.stronk.ui.screens

import android.Manifest.permission.RECORD_AUDIO
import android.content.pm.PackageManager
import android.speech.RecognizerIntent
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.stronk.Manifest
import com.example.stronk.R
import com.example.stronk.model.ExploreViewModel
import com.example.stronk.network.PreferencesManager
import com.example.stronk.state.foundSomething
import com.example.stronk.state.searching
import com.example.stronk.state.*
import com.example.stronk.ui.components.*
import kotlinx.coroutines.launch

@Composable
fun ExploreScreen(
    onNavigateToViewRoutine: (routineId: Int) -> Unit,
    exploreViewModel: ExploreViewModel = viewModel(factory = ExploreViewModel.Factory),
    onNavigateToViewMore: () -> Unit,
) {
    
    //

    val state = exploreViewModel.uiState
    val scrollState = rememberScrollState()
    val coroutine = rememberCoroutineScope()
    Refreshable(refreshFunction = { exploreViewModel.refresh() }) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            SearchBar(
                label = stringResource(id = R.string.search_for_routines),
                onValueChanged = { s ->
                    exploreViewModel.searchRoutines(s)
                    coroutine.launch {
                    scrollState.scrollTo(0)
                }
                })
            Button(
                onClick = { exploreViewModel.showFilterMenu() }, modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(start = 8.dp, top = 6.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = if(state.isNotDefault) MaterialTheme.colors.primary else MaterialTheme.colors.background),
                elevation = ButtonDefaults.elevation(0.dp)

            ) {
                Icon(
                    imageVector = Icons.Filled.FilterList,
                    modifier = Modifier.size(24.dp),
                    contentDescription = stringResource(id = R.string.filter),
                    tint = if(state.isNotDefault) MaterialTheme.colors.onPrimary else MaterialTheme.colors.onBackground
                )
            }
        }
        LoadDependingContent(loadState = state.loadState) {
            Column(modifier = Modifier.verticalScroll(scrollState)) {
                if (state.showFilters) {
                    Dialog(onDismissRequest = { exploreViewModel.hideFilterMenu() }) {
                        Card(backgroundColor = MaterialTheme.colors.background) {
                            Column(
                                modifier = Modifier
                                    .padding(horizontal = 16.dp, vertical = 8.dp)
                                    .wrapContentHeight()
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = stringResource(id = R.string.order_and_filters),
                                        style = MaterialTheme.typography.h5
                                    )
                                    IconButton(onClick = {
                                        exploreViewModel.hideFilterMenu()
                                    }) {
                                        Icon(
                                            imageVector = Icons.Filled.Close,
                                            contentDescription = stringResource(id = R.string.close)
                                        )
                                    }
                                }
                                Column(
                                    modifier = Modifier
                                        .height(500.dp)
                                        .verticalScroll(rememberScrollState())
                                ) {
                                    Divider(modifier = Modifier.padding(bottom = 10.dp))
                                    Text(
                                        text = "${stringResource(id = R.string.OrderBy)}:",
                                        style = MaterialTheme.typography.h6,
                                        modifier = Modifier.padding(bottom = 10.dp)
                                    )
                                    OrderBy(
                                        title = stringResource(id = R.string.criteria),
                                        optionsList = listOf(
                                            Pair(stringResource(id = R.string.default_)) { exploreViewModel.setOrderAndReload() },
                                            Pair(stringResource(id = R.string.name)) {
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
                                            }),
                                        selectedIndex = state.orderIndex
                                    )
                                    OrderBy(
                                        title = stringResource(id = R.string.direction),
                                        optionsList = listOf(Pair("asc") {
                                            exploreViewModel.setAscOrDescAndReload(
                                                "asc"
                                            )
                                        },
                                            Pair("desc") { exploreViewModel.setAscOrDescAndReload("desc") }),
                                        selectedIndex = state.directionIndex
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
                                            Pair(stringResource(id = R.string.no_filter)) {
                                                exploreViewModel.setDifficultyAndReload(
                                                    null
                                                )
                                            },
                                            Pair(stringResource(id = R.string.beginner)) {
                                                exploreViewModel.setDifficultyAndReload(
                                                    "beginner"
                                                )
                                            },
                                            Pair(stringResource(id = R.string.intermediate)) {
                                                exploreViewModel.setDifficultyAndReload(
                                                    "intermediate"
                                                )
                                            },
                                            Pair(stringResource(id = R.string.advanced)) {
                                                exploreViewModel.setDifficultyAndReload(
                                                    "advanced"
                                                )
                                            },
                                        ),
                                        selectedIndex = state.difficultyIndex
                                    )
                                    OrderBy(
                                        title = stringResource(id = R.string.score),
                                        optionsList = listOf(
                                            Pair(stringResource(id = R.string.no_filter)) {
                                                exploreViewModel.setScoreAndReload(
                                                    null
                                                )
                                            },
                                            Pair(stringResource(id = R.string.score_0)) {
                                                exploreViewModel.setScoreAndReload(
                                                    0
                                                )
                                            },
                                            Pair(stringResource(id = R.string.score_1)) {
                                                exploreViewModel.setScoreAndReload(
                                                    1
                                                )
                                            },
                                            Pair(stringResource(id = R.string.score_2)) {
                                                exploreViewModel.setScoreAndReload(
                                                    2
                                                )
                                            },
                                            Pair(stringResource(id = R.string.score_3)) {
                                                exploreViewModel.setScoreAndReload(
                                                    3
                                                )
                                            },
                                            Pair(stringResource(id = R.string.score_4)) {
                                                exploreViewModel.setScoreAndReload(
                                                    4
                                                )
                                            },
                                            Pair(stringResource(id = R.string.score_5)) {
                                                exploreViewModel.setScoreAndReload(
                                                    5
                                                )
                                            }),
                                        selectedIndex = state.scoreFilteringIndex
                                    )
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
                            onGetMoreRoutines = { },
                            isLastPage = true,
                            wantsList = (state.viewPreference == PreferencesManager.ViewPreference.LIST)
                        )
                    } else {
                        NoRoutinesMessage(msg = stringResource(id = R.string.nothing_found))
                    }
                } else {

                    state.categories.forEachIndexed { index, category ->
                        if (category.routines.isNotEmpty()) {
                            RoutineButtonGroup(
                                routineList = if (category.routines.size > exploreViewModel.routinePageSize)
                                    category.routines.subList(
                                        0,
                                        exploreViewModel.routinePageSize
                                    ) else category.routines,
                                title = stringResource(id = (Category(0,category.name,null).nameStringResourceId)),
                                onNavigateToViewRoutine = onNavigateToViewRoutine,
                                onGetMoreRoutines = {
                                    exploreViewModel.setCategoryViewMore(index)
                                    onNavigateToViewMore()
                                },
                                isLastPage = category.routines.size < exploreViewModel.routinePageSize,
                                noRoutinesMessage = stringResource(R.string.no_routines_category),
                                wantsList = (state.viewPreference == PreferencesManager.ViewPreference.LIST)
                            )
                        }
                    }
                    if (state.categories.none { it.routines.isNotEmpty() }) {
                        NoRoutinesMessage(msg = stringResource(id = R.string.nothing_found))
                    }
                }
            }
        }
    }
}
