package com.example.stronk.ui.screens

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.stronk.ui.theme.StronkTheme
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.stronk.R
import com.example.stronk.model.ApiStatus
import com.example.stronk.model.ViewRoutineViewModel
import com.example.stronk.state.*
import com.example.stronk.ui.components.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneId
import java.util.*

data class RefreshParams(val uiState: ViewRoutineState, val viewModel: ViewRoutineViewModel)

@ExperimentalFoundationApi
@ExperimentalMaterialApi
@ExperimentalAnimationApi
@Composable
fun ViewRoutineScreen(
    routineId: Int,
    onNavigateToExecution: (routineId: Int) -> Unit,
    viewRoutineViewModel: ViewRoutineViewModel
) {
    val state = viewRoutineViewModel.uiState
    rememberSaveable {
        viewRoutineViewModel.initialize()
        viewRoutineViewModel.fetchRoutine(routineId)
    }

    Refreshable(refreshFunction = {
        viewRoutineViewModel.forceFetchRoutine(
            routineId
        )
    }) {
        LoadDependingContent(loadState = state.loadState) {
            Scaffold(floatingActionButton = {
                FloatingActionButton(
                    onClick = { onNavigateToExecution(routineId) },
                    backgroundColor = MaterialTheme.colors.secondary,
                    contentColor = MaterialTheme.colors.onSecondary,
                    modifier = Modifier.size(72.dp)
                ) {
                    Icon(
                        Icons.Filled.PlayArrow,
                        contentDescription = "Play",
                        modifier = Modifier.size(48.dp)
                    )
                }
            }, modifier = Modifier.padding(10.dp)) {
                Column(
                    modifier = Modifier
                        .padding(it)
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                ) {
                    Text(
                        text = state.routine.name,
                        style = MaterialTheme.typography.h5,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        AsyncImage(
                            model = state.routine.user?.avatarUrl ?: "",//TODO avatar default
                            contentDescription = null,
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                        Text(
                            text = stringResource(
                                id = R.string.made_by_x,
                                if (state.routine.user == null) "" else state.routine.user.username
                            ),
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        Column() {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "${stringResource(id = R.string.category)}:",
                                    modifier = Modifier.padding(end = 10.dp),
                                    fontWeight = FontWeight.SemiBold
                                )
                                Chip(onClick = {}) {
                                    Text(text = stringResource(id = state.routine.category.nameStringResourceId))
                                }
                            }
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "${stringResource(id = R.string.difficulty)}:",
                                    modifier = Modifier.padding(end = 10.dp),
                                    fontWeight = FontWeight.SemiBold
                                )
                                Chip(onClick = {}) {
                                    Text(text = stringResource(id = state.routine.difficultyStringResourceId))
                                }
                            }
                        }
                        RatingCard(
                            rating = state.routine.rating, modifier = Modifier
                                .padding(top = 10.dp)
                                .wrapContentWidth()
                                .clickable { viewRoutineViewModel.showRatingDialog() }
                        )
                    }
                    Row() {
                        Text(
                            text = "${stringResource(id = R.string.creation_date)}: ",
                            fontWeight = FontWeight.SemiBold
                        )
                        val date = SimpleDateFormat(
                            "dd/MM/yyyy",
                            Locale.getDefault()
                        ).format(state.routine.creationDate).toString()
                        Text(text = date)
                    }
                    Text(
                        text = "${stringResource(id = R.string.description)}:",
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(text = state.routine.description ?: "")
                    CompleteRoutine(cycleList = state.cycles)
                }
                if (state.showRatingDialog) {
                    Dialog(onDismissRequest = { viewRoutineViewModel.hideRatingDialog() }) {
                        var currentRate by remember { mutableStateOf(0) }
                        Card(backgroundColor = MaterialTheme.colors.background) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = "${stringResource(id = R.string.your_rating_for_this_routine)}:",
                                    style = MaterialTheme.typography.h6,
                                    modifier = Modifier.padding(bottom = 10.dp)
                                )
                                Row() {
                                    Text(
                                        text = "$currentRate",
                                        modifier = Modifier.padding(end = 10.dp)
                                    )
                                    ClickableRatingBar(
                                        currentRating = currentRate,
                                        onRatingChange = { it -> currentRate = it },
                                        starsSize = 24
                                    )
                                }
                                Row(
                                    modifier = Modifier.padding(top = 10.dp),
                                    horizontalArrangement = Arrangement.End
                                ) {
                                    Button(
                                        onClick = { viewRoutineViewModel.hideRatingDialog() },
                                        modifier = Modifier.padding(end = 10.dp)
                                    ) {
                                        Text(text = stringResource(id = R.string.cancel).uppercase())
                                    }
                                    Button(onClick = {
                                        viewRoutineViewModel.rateRoutine(currentRate)
                                        viewRoutineViewModel.hideRatingDialog()
                                    }) {
                                        Text(text = stringResource(id = R.string.ok).uppercase())
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@ExperimentalFoundationApi
@Preview(showBackground = true)
@ExperimentalMaterialApi
@ExperimentalAnimationApi
@Composable
fun ExampleRoutineView() {
    StronkTheme() {
        ViewRoutineScreen(1, {}, viewModel(factory = ViewRoutineViewModel.Factory))
    }
}