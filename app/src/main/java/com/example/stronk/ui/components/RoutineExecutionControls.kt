package com.example.stronk.ui.components


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.stronk.R
import com.example.stronk.model.ExecuteViewModel
import com.example.stronk.state.currentCycle
import com.example.stronk.ui.theme.StronkTheme
import kotlinx.coroutines.delay
import kotlin.properties.Delegates

@Composable
fun RoutineControls(
    modifier: Modifier = Modifier,
    startingTimer: Long? = null,
    reps: Int? = null,
    backgroundColor: Color = MaterialTheme.colors.primary,
    contentColor: Color = MaterialTheme.colors.onPrimary,
    onSkipPrevious: () -> Unit,
    onSkipNext: () -> Unit,
) {
    var totalremainingMilliSeconds: Long by rememberSaveable { mutableStateOf(if (startingTimer != null) startingTimer * 1000L else 0L) }
    var timerRunning by remember { mutableStateOf(false) }
    var minutes: Long = (totalremainingMilliSeconds) / (1000 * 60)
    var seconds: Long = (totalremainingMilliSeconds / 1000) % 60
    /*TODO Temporizador Alerta */
    LaunchedEffect(key1 = totalremainingMilliSeconds, key2 = timerRunning) {
        if (timerRunning && totalremainingMilliSeconds > 0L) {
            delay(100L) //Funciona en milisegundos
            totalremainingMilliSeconds -= 100L
        }
    }
    LaunchedEffect(key1 = startingTimer) {
        totalremainingMilliSeconds = if (startingTimer != null) {
            startingTimer * 1000L
        } else {
            0L
        }
    }
    Column(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(backgroundColor),
        verticalArrangement = Arrangement.SpaceEvenly,
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            if (reps != null) {
                Column(
                    modifier = Modifier
                        .padding(20.dp, 10.dp, 0.dp, 0.dp)
                        .align(Alignment.TopStart),
                ) {
                    Text(text = stringResource(R.string.repetitions), color = contentColor)
                    Row {
                        Text(
                            text = reps.toString(),
                            fontSize = MaterialTheme.typography.h3.fontSize,
                            modifier = Modifier.alignByBaseline(),
                            color = contentColor
                        )
                        Text(
                            text = " reps.",
                            modifier = Modifier.alignByBaseline(),
                            color = contentColor
                        )
                    }
                }
            }
            if (startingTimer != null) {
                Column(
                    modifier = Modifier
                        .padding(0.dp, 10.dp, 20.dp, 0.dp)
                        .align(Alignment.TopEnd),
                ) {
                    Text(text = stringResource(R.string.time_left), color = contentColor)
                    Text(
                        text = String.format("%02d:%02d", minutes, seconds),
                        fontSize = MaterialTheme.typography.h3.fontSize,
                        color = contentColor
                    )

                }
            }
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(30.dp, 0.dp, 30.dp, 0.dp)
        ) {
            IconButton(onClick = {
                onSkipPrevious()
                timerRunning = false
            }, modifier = Modifier.align(Alignment.CenterStart)) {
                Icon(
                    Icons.Filled.SkipPrevious,
                    contentDescription = null,
                    modifier = Modifier.size(50.dp),
                    tint = contentColor
                )
            }
            if (startingTimer != null) {
                IconButton(
                    onClick = {
                        if (totalremainingMilliSeconds <= 0L) {
                            totalremainingMilliSeconds = startingTimer * 1000L
                            timerRunning = true
                        } else {
                            timerRunning = !timerRunning
                        }

                    },
                    modifier = Modifier.align(Alignment.Center)
                ) {
                    val icon =
                        if (!timerRunning && totalremainingMilliSeconds > 0L) Icons.Filled.PlayArrow
                        else if (timerRunning && totalremainingMilliSeconds > 0L) Icons.Filled.Pause
                        else Icons.Filled.Replay
                    Icon(
                        icon,
                        contentDescription = null,
                        modifier = Modifier.size(50.dp),
                        tint = contentColor
                    )
                }
            }
            IconButton(
                onClick = {
                    onSkipNext()
                    timerRunning = false
                },
                modifier = Modifier.align(Alignment.CenterEnd)
            ) {
                Icon(
                    Icons.Filled.SkipNext,
                    contentDescription = null,
                    modifier = Modifier.size(50.dp),
                    tint = contentColor
                )
            }
        }
        //routineControls
    }
}

@Preview(showBackground = true)
@Composable
fun RoutineControlsPreview() {
    StronkTheme {
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
            Column(modifier = Modifier.fillMaxWidth()) {
                RoutineControls(startingTimer = 15, reps = 10, onSkipNext = {}, onSkipPrevious = {})
            }

        }


    }
}