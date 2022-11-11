package com.example.stronk.ui.screens

import android.content.res.Resources
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.stronk.R
import com.example.stronk.model.ExecuteViewModel
import com.example.stronk.state.currentCycle
import com.example.stronk.state.nextCycle
import com.example.stronk.state.previousCycle
import com.example.stronk.ui.components.ExecutingCycles
import com.example.stronk.ui.theme.StronkTheme
import com.google.accompanist.pager.*
import kotlinx.coroutines.launch


@ExperimentalPagerApi
@Composable
fun Tabs(pagerState: PagerState) {
    val list = listOf(
        stringResource(id = R.string.condensed),
        stringResource(id = R.string.detailed),
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
fun TabsContent(pagerState: PagerState) {
    HorizontalPager(state = pagerState) {
            page ->
        when (page) {
            0 -> ResumedScreen()
            1 -> DetailedScreen()
        }
    }
}

@Composable
fun ResumedScreen() {
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
    executeViewModel.executeRoutine(0)
    val state = executeViewModel.uiState
    var i by remember {
        mutableStateOf(0)
    }
    Column(
        modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()),

    ) {
        ExecutingCycles(
            prevCycle = state.previousCycle,
            currentCycle = state.currentCycle,
            nextCycle = state.nextCycle,
            currentExercise = state.exerciseIndex,
            currentRepetition = state.cycleRepetition
        )
        Button(onClick = { executeViewModel.next() }) {
            Text("Next Exercise")
        }
        Button(onClick = { executeViewModel.previous() }) {
            Text("Previous Exercise")
        }
        Text("cycle: ${state.cycleIndex}, exercise: ${state.exerciseIndex}, repetition: ${state.cycleRepetition}")


    }
}

@Preview
@ExperimentalPagerApi
@ExperimentalAnimationApi
@Composable
fun ExecuteRoutineScreen() {
    StronkTheme() {
        DetailedScreen()
    }

}