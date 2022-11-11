package com.example.stronk.ui.screens

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.stronk.R
import com.example.stronk.model.ExecuteViewModel
import com.example.stronk.state.currentCycle
import com.example.stronk.state.nextCycle
import com.example.stronk.state.previousCycle
import com.example.stronk.ui.components.ExecutingCycles
import com.example.stronk.ui.components.RoutineControls
import com.google.accompanist.pager.*
import kotlinx.coroutines.launch

@ExperimentalPagerApi
@ExperimentalAnimationApi
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
        stringResource(id = R.string.condensed).uppercase(),
        stringResource(id = R.string.detailed).uppercase(),
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
@Composable
fun TabsContent(pagerState: PagerState, routineId: Int) {
    val executeViewModel: ExecuteViewModel = viewModel()
    executeViewModel.executeRoutine(routineId)
    HorizontalPager(state = pagerState) { page ->
        when (page) {
            0 -> ResumedScreen(executeViewModel)
            1 -> DetailedScreen(executeViewModel)
        }
    }
}

@Composable
fun ResumedScreen(executeViewModel: ExecuteViewModel = viewModel()) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Resumed Screen",
            style = MaterialTheme.typography.h5,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
@ExperimentalAnimationApi
fun DetailedScreen(executeViewModel: ExecuteViewModel = viewModel()) {
    val state = executeViewModel.uiState
    Scaffold(bottomBar = {
        Box(modifier = Modifier.height(170.dp).background(MaterialTheme.colors.primary)) {
            RoutineControls(
                startingTimer = state.currentCycle.exList[state.exerciseIndex].duration?.toLong(),
                reps = state.currentCycle.exList[state.exerciseIndex].reps,
                onSkipPrevious = { executeViewModel.previous() },
                onSkipNext = { executeViewModel.next() },
            )
        }

    }) {
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
                currentRepetition = state.cycleRepetition
            )
        }
    }

}
