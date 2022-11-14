package com.example.stronk.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.stronk.R
import com.example.stronk.RoutineButton
import com.example.stronk.network.dtos.RoutineData
import com.example.stronk.state.Routine
import com.example.stronk.ui.theme.StronkTheme


@Composable
fun RoutineButtonGroup(
    routineList: List<Routine>,
    title: String,
    onNavigateToViewRoutine: (routineId: Int) -> Unit,
    onGetMoreRoutines: () -> Unit
) {
    Card(
        modifier = Modifier
            .padding(15.dp),
        elevation = 10.dp
    ) {
        Column(verticalArrangement = Arrangement.Top) {
            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth())
            {
                Text(
                    title,
                    modifier = Modifier.padding(start = 5.dp, top = 5.dp).alignByBaseline(),
                    style = MaterialTheme.typography.h4
                )
                Button(onClick = { onGetMoreRoutines() }, shape = CircleShape, modifier = Modifier.alignByBaseline()) {
                    Text("Ver más")
                }
            }

            for (i in 0 until routineList.size step 2) {
                Row(
                    modifier = Modifier
                        .height(IntrinsicSize.Min)
                        .fillMaxWidth()
                        .wrapContentWidth(Alignment.Start),
                    horizontalArrangement = Arrangement.SpaceEvenly
                )
                {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth(0.5f)
                            .height(200.dp)
                    ) {
                        val current = routineList[i]
                        RoutineButton(
                            RoutineID = current.id,
                            //TODO cambiar dependiendo de las categorías
                            RoutineImageID = R.drawable.abdos,
                            RoutineName = current.name,
                            modifierButton = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight(),
                            onNavigateToViewRoutine = onNavigateToViewRoutine
                        )
                    }
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                    ) {
                        if (i + 1 < routineList.size) {
                            val current = routineList[i + 1]
                            RoutineButton(
                                RoutineID = current.id,
                                //TODO cambiar dependiendo de las categorías
                                RoutineImageID = R.drawable.abdos,
                                RoutineName = current.name,
                                modifierButton = Modifier
                                    .fillMaxWidth()
                                    .fillMaxHeight(),
                                onNavigateToViewRoutine = onNavigateToViewRoutine
                            )
                        }
                    }
                }
            }
        }
    }
}


