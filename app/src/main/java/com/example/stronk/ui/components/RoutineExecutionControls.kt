package com.example.stronk.ui.components


import androidx.compose.foundation.layout.*
import androidx.compose.material.*

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.stronk.R
import com.example.stronk.ui.theme.StronkTheme
import kotlinx.coroutines.delay

@Composable
fun RoutineControls(StartingTimer: Long?=null, Reps: Int?=null) {
    var totalremainingMilliSeconds: Long by rememberSaveable { mutableStateOf(if(StartingTimer != null) StartingTimer*1000L else 0L ) }
    var timerRunning by remember {mutableStateOf(false)}
    var minutes : Long = (totalremainingMilliSeconds)/(1000*60)
    var seconds : Long = (totalremainingMilliSeconds/1000) % 60
    /*TODO Temporizador Alerta */
    LaunchedEffect(key1 = totalremainingMilliSeconds,key2 = timerRunning) {
        if (timerRunning && totalremainingMilliSeconds > 0L) {
            delay(100L) //Funciona en milisegundos
            totalremainingMilliSeconds-= 100L
        }
    }
    Box(
        Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(10.dp)) {
        if (Reps != null) {
            Column(modifier= Modifier
                .align(Alignment.TopStart)
                .padding(15.dp, 10.dp, 0.dp, 0.dp)){
                Text(text = stringResource(R.string.repetitions))
                Row {
                    Text(text =Reps.toString(), fontSize = MaterialTheme.typography.h3.fontSize, modifier = Modifier.alignByBaseline())
                    Text(text =" reps.", modifier=Modifier.alignByBaseline())
                }
            }
        }
        if (StartingTimer != null) {
            Column(modifier= Modifier
                .align(Alignment.TopEnd)
                .padding(0.dp, 10.dp, 15.dp, 0.dp)){
                Text(text = stringResource(R.string.time_left))//TODO translate
                Text(text = String.format("%02d:%02d",minutes,seconds),fontSize = MaterialTheme.typography.h3.fontSize)

            }
        }
        Row(modifier= Modifier
            .align(Alignment.BottomCenter)
            .fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround){
            IconButton(onClick = { /*TODO*/ }) {
                Icon(Icons.Filled.SkipPrevious, contentDescription = null, modifier= Modifier.size(50.dp))
            }
            if(StartingTimer != null){
                IconButton(onClick = { /*TODO Parar timer*/
                                     if(totalremainingMilliSeconds <= 0L){
                                         totalremainingMilliSeconds = StartingTimer*1000L
                                         timerRunning=true
                                     }else{
                                         timerRunning = !timerRunning
                                     }

                                     },) {
                    val icon= if (!timerRunning && totalremainingMilliSeconds > 0L ) Icons.Filled.PlayArrow
                                else if(timerRunning && totalremainingMilliSeconds > 0L) Icons.Filled.Pause
                                else Icons.Filled.Replay
                    Icon( icon, contentDescription = null, modifier= Modifier.size(50.dp))
                }
            }
            IconButton(onClick = { /*TODO*/ }) {
                Icon(Icons.Filled.SkipNext, contentDescription = null, modifier= Modifier.size(50.dp))
            }
        }
        //routineControls
    }
}
@Preview(showBackground = true)
@Composable
fun RoutineControlsPreview(){
    StronkTheme {
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)) {
                    RoutineControls(StartingTimer = 15, Reps = 10)
                }

            }

        }


    }
}