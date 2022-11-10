package com.example.stronk.ui.screens

import android.content.res.Resources
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.stronk.R
import com.google.accompanist.pager.*
import kotlinx.coroutines.launch

@Preview
@ExperimentalPagerApi
@Composable
fun ExecuteRoutineScreen() {
    val pagerState = rememberPagerState(pageCount = 2)
    Tabs(pagerState = pagerState)
    TabsContent(pagerState = pagerState)
}

@ExperimentalPagerApi
@Composable
fun Tabs(pagerState: PagerState) {
    val list = listOf(
        Resources.getSystem().getString(R.string.condensed),
        Resources.getSystem().getString(R.string.detailed),
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
        list.forEachIndexed { index, _ ->
            Tab(
                text = {
                    Text(
                        list[index],
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
@Composable
fun TabsContent(pagerState: PagerState) {
    HorizontalPager(state = pagerState) {
            page ->
        when (page) {
            0 -> ResumedScreen(data = "Welcome to Home Screen")
            1 -> DetailedScreen(data = "Welcome to Shopping Screen")
        }
    }
}

@Composable
fun ResumedScreen(data: String) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = data,
            style = MaterialTheme.typography.h5,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun DetailedScreen(data: String) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = data,
            style = MaterialTheme.typography.h5,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
    }
}