package com.example.stronk.ui.screens

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
//import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.stronk.R
import com.example.stronk.model.ExecuteViewModel
import com.example.stronk.state.*
import com.example.stronk.ui.components.ExecutingCycles
import com.example.stronk.ui.components.InfoCycle
import com.example.stronk.ui.components.RoutineControls
import com.example.stronk.ui.components.TitleAndSubtitle
import com.example.stronk.ui.theme.StronkTheme
import com.google.accompanist.pager.*
import kotlinx.coroutines.launch

@ExperimentalPagerApi
@ExperimentalAnimationApi
@ExperimentalFoundationApi
@Composable
fun ExecuteRoutineScreen(routineId: Int) {
    val pagerState = rememberPagerState(pageCount = 2)
    Column {
        Tabs(pagerState = pagerState)
        TabsContent(pagerState = pagerState, routineId = routineId)
    }
}

@ExperimentalPagerApi
@Composable
fun Tabs(pagerState: PagerState) {
    val list = listOf(
        stringResource(id = R.string.detailed).uppercase(),
        stringResource(id = R.string.condensed).uppercase(),
    )
    val scope = rememberCoroutineScope()
    TabRow(
        selectedTabIndex = pagerState.currentPage,

        backgroundColor = MaterialTheme.colors.primaryVariant,

        contentColor = Color.White,

        indicator = { tabPositions ->
            TabRowDefaults.Indicator(
                Modifier.pagerTabIndicatorOffset(pagerState, tabPositions),
                height = 2.dp,
                color = Color.White
            )
        }
    ) {
        // on below line we are specifying icon
        // and text for the individual tab item
        list.forEachIndexed { index, text ->
            // on below line we are creating a tab.
            Tab(
                text = {
                    Text(
                        text,
                        // on below line we are specifying the text color
                        // for the text in that tab
                        color = if (pagerState.currentPage == index) Color.White else Color.LightGray
                    )
                },
                selected = pagerState.currentPage == index,
                onClick = {
                    scope.launch {
                        pagerState.animateScrollToPage(index)
                    }
                }
            )
        }
    }
}

@ExperimentalPagerApi
@ExperimentalAnimationApi
@ExperimentalFoundationApi
@Composable
fun TabsContent(pagerState: PagerState, routineId: Int) {
    val executeViewModel: ExecuteViewModel = viewModel()
    executeViewModel.executeRoutine(routineId)
    val coroutineScope = rememberCoroutineScope()
    LaunchedEffect(key1 = pagerState.currentPage) {
        coroutineScope.launch {
            executeViewModel.setPage(pagerState.currentPage)
        }
    }
    HorizontalPager(state = pagerState) { page ->
        when (page) {
            0 -> DetailedScreen(executeViewModel)
            1 -> ResumedScreen(executeViewModel)
        }
    }
}

@Composable
@ExperimentalAnimationApi
@ExperimentalFoundationApi
fun ResumedScreen(executeViewModel: ExecuteViewModel = viewModel()) {
    val state = executeViewModel.uiState
    val windowInfo= rememberWindowInfo()
    val exercise:ExInfo= state.currentCycle.exList[state.exerciseIndex]
    if(windowInfo.screenWidthInfo is WindowInfo.WindowType.Compact) {
        Scaffold(bottomBar = {
            RoutineControls(
                startingTimer = state.currentCycle.exList[state.exerciseIndex].duration?.toLong(),
                reps = state.currentCycle.exList[state.exerciseIndex].reps,
                onSkipPrevious = { executeViewModel.previous() },
                onSkipNext = { executeViewModel.next() },
            )
        }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
                    .verticalScroll(rememberScrollState()),

                ) {
                ExecutingCycles(
                    prevCycle = state.previousCycle,
                    currentCycle = state.currentCycle,
                    nextCycle = state.nextCycle,
                    currentExercise = state.exerciseIndex,
                    currentRepetition = state.cycleRepetition,
                    shouldMoveScrolling = state.page == 1
                )
            }

        }
    }else{
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(10.dp),
                ) {
                ExecutingCycles(
                    prevCycle = state.previousCycle,
                    currentCycle = state.currentCycle,
                    nextCycle = state.nextCycle,
                    currentExercise = state.exerciseIndex,
                    currentRepetition = state.cycleRepetition,
                    shouldMoveScrolling = state.page == 1
                )
            }
            Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(0.80f)
                        .padding(end = 5.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom
            ) {
                TitleAndSubtitle(MainText = exercise.name)
            RoutineControls(
                startingTimer = state.currentCycle.exList[state.exerciseIndex].duration?.toLong(),
                reps = state.currentCycle.exList[state.exerciseIndex].reps,
                onSkipPrevious = { executeViewModel.previous() },
                onSkipNext = { executeViewModel.next() },
                contentColor = MaterialTheme.colors.onBackground,
                backgroundColor = MaterialTheme.colors.background
            )
        }
        }
    }
}

@Composable
fun DetailedScreen(executeViewModel: ExecuteViewModel = viewModel()) {
    val state = executeViewModel.uiState
    val exercise:ExInfo= state.currentCycle.exList[state.exerciseIndex]
    val windowInfo= rememberWindowInfo()
    if(windowInfo.screenWidthInfo is WindowInfo.WindowType.Compact){
        Scaffold(bottomBar = {
            RoutineControls(
                startingTimer = state.currentCycle.exList[state.exerciseIndex].duration?.toLong(),
                reps = state.currentCycle.exList[state.exerciseIndex].reps,
                onSkipPrevious = { executeViewModel.previous() },
                onSkipNext = { executeViewModel.next() },
            )
            }
        ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(it),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    AsyncImage(
                        model = exercise.imageUrl,
                        contentDescription = null,
                        modifier = Modifier
                            .sizeIn(maxHeight = 200.dp)
                            .clip(RoundedCornerShape(10.dp))
                    )
                    TitleAndSubtitle(MainText = exercise.name, SecondaryText = exercise.description)
                    InfoCycle(
                        currentCycle = state.currentCycle.name,
                        cycleRepetitions = state.currentCycle.cycleReps,
                        currentCycleRepetition = state.cycleRepetition+1,
                        nextExer = state.nextExercise?.name ?:"---"
                    )
                }
        }
    }else{

        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .wrapContentWidth()
                        .padding(start = 5.dp)
                        .alignByBaseline(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    TitleAndSubtitle(MainText = exercise.name, SecondaryText = exercise.description)
                    Row(modifier=Modifier.fillMaxWidth(),horizontalArrangement = Arrangement.SpaceEvenly){
                        AsyncImage(
                            model = exercise.imageUrl,
                            contentDescription = null,
                            modifier = Modifier
                                .sizeIn(maxHeight = 100.dp)
                                .clip(RoundedCornerShape(10.dp))
                        )
                        InfoCycle(
                            currentCycle = state.currentCycle.name,
                            cycleRepetitions = state.currentCycle.cycleReps,
                            currentCycleRepetition = state.cycleRepetition+1,
                            nextExer = state.nextExercise?.name ?:"---"
                        )
                    }

                }
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(0.60f)
                        .padding(end = 5.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Bottom
                ) {
                    RoutineControls(
                        startingTimer = state.currentCycle.exList[state.exerciseIndex].duration?.toLong(),
                        reps = state.currentCycle.exList[state.exerciseIndex].reps,
                        onSkipPrevious = { executeViewModel.previous() },
                        onSkipNext = { executeViewModel.next() },
                        contentColor = MaterialTheme.colors.onBackground,
                        backgroundColor = MaterialTheme.colors.background
                    )
                 }
        }

        }
}


@ExperimentalAnimationApi
@ExperimentalFoundationApi
@Preview(showBackground = true)
@Composable
fun PreviewDetailedScreen() {
    StronkTheme {
        DetailedScreen()
    }

}
