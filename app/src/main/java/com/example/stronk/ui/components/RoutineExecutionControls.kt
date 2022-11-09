package com.example.stronk.ui.components


import androidx.compose.foundation.layout.*
import androidx.compose.material.*

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.stronk.ui.theme.StronkTheme
import java.util.Timer

@Composable
fun RoutineControls(StartingTimer: Int?=null, Reps: Int?=null) {
    var totalremainingSeconds by remember { mutableStateOf(StartingTimer ?: 0) }
    var timerPaused by remember {mutableStateOf(false)}
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
                Text(text = "Repeticiones:")
                Row {
                    Text(text =Reps.toString(), fontSize = MaterialTheme.typography.h3.fontSize, modifier = Modifier.alignByBaseline())
                    Text(text ="reps.", modifier=Modifier.alignByBaseline())
                }
            }
        }
        if (StartingTimer != null) {
            Column(modifier= Modifier
                .align(Alignment.TopEnd)
                .padding(0.dp, 10.dp, 15.dp, 0.dp)){
                Text(text = "Tiempo Restante:")
                Text(text = "$minutes:$seconds",fontSize = MaterialTheme.typography.h3.fontSize)

            }
        }
        Row(modifier= Modifier
            .align(Alignment.BottomCenter)
            .fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround){
            IconButton(onClick = { /*TODO*/ }) {
                Icon(Icons.Filled.SkipPrevious, contentDescription = null, modifier= Modifier.size(50.dp))
            }
            IconButton(onClick = { /*TODO Parar timer*/ },) {
                val icon= if (timerPaused) Icons.Filled.PlayArrow else Icons.Filled.Pause
                Icon( icon, contentDescription = null, modifier= Modifier.size(50.dp))
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