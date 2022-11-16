package com.example.stronk.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.stronk.R
import com.example.stronk.state.Routine


@Composable
fun RoutineButtonGroup(
    routineList: List<Routine>,
    title: String,
    onNavigateToViewRoutine: (routineId: Int) -> Unit,
    onGetMoreRoutines: () -> Unit,
    showButton: Boolean
) {
    Card(
        modifier = Modifier
            .padding(15.dp),
        elevation = 10.dp
    ) {
        Column(verticalArrangement = Arrangement.Top) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            )
            {
                Text(
                    title,
                    modifier = Modifier
                        .padding(start = 5.dp, top = 5.dp)
                        .alignByBaseline(),
                    style = MaterialTheme.typography.h4
                )
                if (showButton) {
                    Button(
                        onClick = { onGetMoreRoutines() },
                        modifier = Modifier.alignByBaseline()
                    ) {
                        Text(stringResource(id = R.string.show_more).uppercase())
                    }
                }
            }

            for (i in routineList.indices step 2) {
                Row(
                    modifier = Modifier
                        .height(IntrinsicSize.Min)
                        .fillMaxWidth()
                        .wrapContentWidth(Alignment.Start),
                    horizontalArrangement = Arrangement.SpaceEvenly
                )
                {
                    val onlyOne = i+1 >= routineList.size
                    Column(
                        modifier = Modifier
                            .fillMaxWidth(if(!onlyOne) 0.5f else 1f)
                            .height(200.dp)
                    ) {
                        val current = routineList[i]
                        RoutineButton(
                            RoutineID = current.id,
                            //TODO cambiar dependiendo de las categorías
                            RoutineImageID = R.drawable.abdos,
                            RoutineName = current.name,
                            modifier = Modifier
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
                        if (!onlyOne) {
                            val current = routineList[i + 1]
                            RoutineButton(
                                RoutineID = current.id,
                                //TODO cambiar dependiendo de las categorías
                                RoutineImageID = R.drawable.abdos,
                                RoutineName = current.name,
                                modifier = Modifier
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


