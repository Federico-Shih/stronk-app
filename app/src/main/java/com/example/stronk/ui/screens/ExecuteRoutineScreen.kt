package com.example.stronk.ui.screens


import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.stronk.R
import com.example.stronk.model.ApiStatus
import com.example.stronk.model.ExecuteViewModel
import com.example.stronk.model.ExploreViewModel
import com.example.stronk.state.*
import com.example.stronk.ui.components.*
import com.example.stronk.ui.theme.StronkTheme
import com.google.accompanist.pager.*
import kotlinx.coroutines.launch

@ExperimentalPagerApi
@ExperimentalAnimationApi
@ExperimentalFoundationApi
@Composable
fun ExecuteRoutineScreen(
    routineId: Int,
    onGoBack: () -> Unit,
    executeViewModel: ExecuteViewModel = viewModel(factory = ExecuteViewModel.Factory)
) {
    //text to speech
    val context = LocalContext.current
    var hasPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.RECORD_AUDIO
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    val requestPermissionLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestPermission()) { result ->
            hasPermission = result
        }


    val speechRecognizerDialogLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    )
    { result ->
        if (result.resultCode == ComponentActivity.RESULT_OK) {
            val data = result.data
            data?.let {
                data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)?.let {
                    if (it.size > 0) {
                        when {
                            it[0].contains("next") || it[0].contains("siguiente") -> executeViewModel.next()
                            it[0].contains("previous") || it[0].contains("anterior") -> executeViewModel.previous()
                        }
                    }
                }
            }
        }
    }
    // ----

    val pagerState = rememberPagerState(pageCount = 2)

    val state = executeViewModel.uiState
    val windowInfo = rememberWindowInfo()
    rememberSaveable {
        executeViewModel.executeRoutine(routineId)
    }
    Scaffold(bottomBar = {
        if (windowInfo.screenWidthInfo == WindowInfo.WindowType.Compact || windowInfo.screenHeightInfo == WindowInfo.WindowType.Expanded) {
            if (state.loadState.status == ApiStatus.SUCCESS) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .background(MaterialTheme.colors.primaryVariant)
                ) {
                    if (!executeViewModel.uiState.emptyRoutine) {
                        if (pagerState.currentPage == 1) {
                            Text(
                                text = String.format(
                                    "%s: %s (%d/%d)",
                                    stringResource(id = R.string.current_cycle),
                                    state.currentCycle.name,
                                    state.cycleRepetition + 1,
                                    state.currentCycle.cycleReps
                                ),
                                color = MaterialTheme.colors.onPrimary,
                                modifier = Modifier
                                    .padding(
                                        top = 4.dp,
                                        start = 0.dp,
                                        bottom = 4.dp
                                    )
                                    .align(Alignment.CenterHorizontally),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Light,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                        RoutineControls(
                            startingTimer = state.currentCycle.exList[state.exerciseIndex].duration?.toLong(),
                            reps = state.currentCycle.exList[state.exerciseIndex].reps,
                            onSkipPrevious = { executeViewModel.previous() },
                            onSkipNext = { executeViewModel.next() },
                            onFinishExecution = { executeViewModel.finish() },
                            isFirstExercise = !state.hasPrevious,
                            isLastExercise = !state.hasNext,
                        )
                    }
                }

            }
        }
    }) {
        Column {
            Tabs(pagerState = pagerState)
            if (state.tts) {
                LaunchedEffect(true) {
                    requestPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                }
                if (!SpeechRecognizer.isRecognitionAvailable(context)) {
                    Toast.makeText(
                        context,
                        stringResource(id = R.string.speech_recognition_not_available),
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
                    intent.putExtra(
                        RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                        RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
                    )
                    intent.putExtra(
                        RecognizerIntent.EXTRA_LANGUAGE,
                        java.util.Locale.getDefault()
                    )
                    intent.putExtra(
                        RecognizerIntent.EXTRA_PROMPT,
                        stringResource(id = R.string.listening)
                    )
                    speechRecognizerDialogLauncher.launch(intent)
                }
                executeViewModel.hideTTS()
            }
            if (windowInfo.screenWidthInfo == WindowInfo.WindowType.Compact || windowInfo.screenHeightInfo == WindowInfo.WindowType.Expanded) {
                TabsContent(
                    pagerState = pagerState,
                    routineId = routineId,
                    onGoBack = onGoBack,
                    executeViewModel = executeViewModel,
                    modifier = Modifier.padding(it)
                )
            } else {
                Row(modifier = Modifier.fillMaxSize()) {
                    Column(
                        modifier = Modifier
                            .fillMaxHeight()
                            .weight(1f)
                    ) {
                        TabsContent(
                            pagerState = pagerState,
                            routineId = routineId,
                            onGoBack = onGoBack,
                            executeViewModel = executeViewModel,
                            modifier = Modifier.padding(it)
                        )
                    }
                    if (state.loadState.status == ApiStatus.SUCCESS) {
                        Column(
                            modifier = Modifier
                                .fillMaxHeight()
                                .weight(0.7f)
                        ) {
                            if (!executeViewModel.uiState.emptyRoutine) {
                                val exercise: ExInfo =
                                    state.currentCycle.exList[state.exerciseIndex]
                                if (pagerState.currentPage == 0) {
                                    AsyncImage(
                                        model = exercise.imageUrl ?: "",
                                        contentDescription = null,
                                        modifier = Modifier
                                            .sizeIn(maxHeight = 80.dp)
                                            .clip(RoundedCornerShape(10.dp))
                                            .padding(10.dp)
                                            .align(Alignment.CenterHorizontally)
                                    )
                                } else {
                                    TitleAndSubtitle(MainText = exercise.name, MainFontSize = 30.sp)
                                    Text(
                                        text = String.format(
                                            "%s: %s (%d/%d)",
                                            stringResource(id = R.string.current_cycle),
                                            state.currentCycle.name,
                                            state.cycleRepetition + 1,
                                            state.currentCycle.cycleReps
                                        ),
                                        color = MaterialTheme.colors.onBackground,
                                        modifier = Modifier.padding(
                                            top = 0.dp,
                                            start = 0.dp,
                                            bottom = 0.dp
                                        ),
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Light,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                                RoutineControls(
                                    startingTimer = state.currentCycle.exList[state.exerciseIndex].duration?.toLong(),
                                    reps = state.currentCycle.exList[state.exerciseIndex].reps,
                                    onSkipPrevious = { executeViewModel.previous() },
                                    onSkipNext = { executeViewModel.next() },
                                    onFinishExecution = { executeViewModel.finish() },
                                    contentColor = MaterialTheme.colors.onBackground,
                                    backgroundColor = MaterialTheme.colors.background,
                                    isFirstExercise = !state.hasPrevious,
                                    isLastExercise = !state.hasNext,
                                )
                            }
                        }
                    }
                }
            }
        }
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
        list.forEachIndexed { index, text ->
            Tab(
                text = {
                    Text(
                        text,
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
fun TabsContent(
    pagerState: PagerState,
    routineId: Int,
    onGoBack: () -> Unit,
    executeViewModel: ExecuteViewModel,
    modifier: Modifier = Modifier
) {

    val coroutineScope = rememberCoroutineScope()
    val windowInfo = rememberWindowInfo()
    val state = executeViewModel.uiState
    LoadDependingContent(loadState = executeViewModel.uiState.loadState) {
        if (executeViewModel.uiState.emptyRoutine) {
            Dialog(
                onDismissRequest = { onGoBack() },
                properties = DialogProperties(
                    dismissOnBackPress = false,
                    dismissOnClickOutside = false
                )
            ) {
                Card(
                    backgroundColor = MaterialTheme.colors.background,
                    contentColor = MaterialTheme.colors.onBackground,
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = stringResource(id = R.string.empty_routine_msg),
                            style = MaterialTheme.typography.h6,
                            modifier = Modifier.padding(bottom = 10.dp)
                        )
                        Row(
                            horizontalArrangement = Arrangement.End,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Button(onClick = {
                                onGoBack()
                            }) {
                                Text(text = stringResource(id = R.string.exit).uppercase())
                            }
                        }
                    }
                }
            }
        } else {
            LaunchedEffect(key1 = pagerState.currentPage) {
                coroutineScope.launch {
                    executeViewModel.setPage(pagerState.currentPage)
                }
            }
            HorizontalPager(state = pagerState) { page ->
                Column(
                    modifier = modifier
                        .fillMaxSize()
                ) {
                    when (page) {
                        0 -> DetailedScreen(
                            executeViewModel,
                            windowInfo.screenWidthInfo == WindowInfo.WindowType.Compact || windowInfo.screenHeightInfo == WindowInfo.WindowType.Expanded
                        )
                        1 -> ResumedScreen(
                            executeViewModel,
                            windowInfo.screenWidthInfo == WindowInfo.WindowType.Compact || windowInfo.screenHeightInfo == WindowInfo.WindowType.Expanded
                        )
                    }
                }
            }
        }
        if (executeViewModel.uiState.finished) {
            Dialog(
                onDismissRequest = {
                    onGoBack()
                    executeViewModel.resetFinish()
                },
                properties = DialogProperties(
                    dismissOnBackPress = false,
                    dismissOnClickOutside = false
                )
            ) {
                Card(
                    backgroundColor = MaterialTheme.colors.background,
                    contentColor = MaterialTheme.colors.onBackground,
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = stringResource(id = R.string.congrats_routine_end),
                            style = MaterialTheme.typography.h6,
                            modifier = Modifier.padding(bottom = 10.dp)
                        )
                        Row(
                            horizontalArrangement = Arrangement.End,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Button(onClick = {
                                onGoBack()
                                executeViewModel.resetFinish()
                            }) {
                                Text(text = stringResource(id = R.string.finish).uppercase())
                            }
                        }
                    }
                }
            }
        }
    }
}


@Composable
@ExperimentalAnimationApi
@ExperimentalFoundationApi
fun ResumedScreen(
    executeViewModel: ExecuteViewModel = viewModel(factory = ExecuteViewModel.Factory),
    shouldShowImage: Boolean = true
) {
    val state = executeViewModel.uiState
    val exercise: ExInfo = state.currentCycle.exList[state.exerciseIndex]
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
            .verticalScroll(rememberScrollState()),

        ) {
        ExecutingCycles(
            prevCycle = state.previousNonEmptyCycle,
            currentCycle = state.currentCycle,
            nextCycle = state.nextNonEmptyCycle,
            currentExercise = state.exerciseIndex,
            currentRepetition = state.cycleRepetition,
            shouldMoveScrolling = state.page == 1,
            expandedExerciseVariant = if (!shouldShowImage) ExerciseItemType.EXPANDED_NO_PIC else
                ExerciseItemType.EXPANDED,
        )
    }

}

@Composable
fun DetailedScreen(
    executeViewModel: ExecuteViewModel = viewModel(factory = ExecuteViewModel.Factory),
    shouldshowImage: Boolean = true
) {
    val state = executeViewModel.uiState
    val exercise: ExInfo = state.currentCycle.exList[state.exerciseIndex]
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (shouldshowImage) {
            AsyncImage(
                model = exercise.imageUrl ?: "",
                contentDescription = null,
                modifier = Modifier
                    .sizeIn(maxHeight = 160.dp)
                    .clip(RoundedCornerShape(10.dp))
            )
        }
        TitleAndSubtitle(
            MainText = exercise.name,
            SecondaryText = exercise.description,
            SecondaryTextHeight = if (shouldshowImage) 70.dp else 30.dp
        )
        InfoCycle(
            currentCycle = state.currentCycle.name,
            cycleRepetitions = state.currentCycle.cycleReps,
            currentCycleRepetition = state.cycleRepetition + 1,
            nextExer = state.nextExercise?.name ?: "---"
        )
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
