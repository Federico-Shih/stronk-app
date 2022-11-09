package com.example.stronk.ui.components


import androidx.compose.foundation.layout.*
import androidx.compose.material.*

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.stronk.R
import com.example.stronk.ui.theme.StronkTheme
import kotlinx.coroutines.delay
import java.util.Timer

@Composable
fun RoutineControls(StartingTimer: Long?=null, Reps: Int?=null) {
    var totalremainingSeconds: Long by remember { mutableStateOf(StartingTimer ?: 0L) }
    var timerRunning by remember {mutableStateOf(false)}
    val minutes = totalremainingSeconds/60
    val seconds = totalremainingSeconds%60
    /*TODO Temporizador */
    Box(
        Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(10.dp)) {
        if (Reps != null) {
            Column(modifier= Modifier
                .align(Alignment.TopStart)
                .padding(15.dp, 10.dp, 0.dp, 0.dp)){
                Text(text = stringResource(R.string.RepetitionsExecControls))//Todo TRANSLATE
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
                Text(text = stringResource(R.string.TimeExecControls))//TODO translate
                Text(text = "$minutes:$seconds",fontSize = MaterialTheme.typography.h3.fontSize)

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
                                     if(totalremainingSeconds <= 0L){
                                         totalremainingSeconds = StartingTimer ?: 0L
                                         timerRunning=true
                                     }else{
                                         timerRunning = !timerRunning
                                     }

                                     },) {
                    val icon= if (!timerRunning && totalremainingSeconds > 0L ) Icons.Filled.PlayArrow
                                else if(timerRunning && totalremainingSeconds > 0L) Icons.Filled.Pause
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
                    RoutineControls(StartingTimer = 150, Reps = 10)
                }

            }

        }


    }
}