package com.example.stronk.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Feedback
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.stronk.R
import com.example.stronk.state.Routine


@Composable
fun RoutineButtonGrid(
    routineList: List<Routine>,
    title: String,
    onNavigateToViewRoutine: (routineId: Int) -> Unit,
    onGetMoreRoutines: () -> Unit,
    isLastPage: Boolean,
    noRoutinesMessage: String = stringResource(R.string.no_routines_message)
) {
        Column(verticalArrangement = Arrangement.Top, modifier = Modifier.padding(5.dp)) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp)
                    .height(50.dp),
                verticalAlignment = Alignment.CenterVertically
            )
            {
                Text(
                    title,
                    modifier = Modifier.padding(bottom = 4.dp),
                    style = MaterialTheme.typography.h5,
                    fontWeight = FontWeight.Bold,
                )
                if (!isLastPage) {
                    Button(
                        onClick = { onGetMoreRoutines() },
                        modifier = Modifier
                    ) {
                        Text(stringResource(id = R.string.show_more).uppercase(),
                            fontWeight = FontWeight.Bold)
                    }
                }
            }
            if (routineList.isEmpty()) {
               NoRoutinesMessage(msg = noRoutinesMessage)
            }else {

                for (i in routineList.indices step 2) {
                    Row(
                        modifier = Modifier
                            .height(IntrinsicSize.Min)
                            .fillMaxWidth()
                            .wrapContentWidth(Alignment.Start),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    )
                    {
                        val onlyOne = i + 1 >= routineList.size
                        Column(
                            modifier = Modifier
                                .fillMaxWidth(if (!onlyOne) 0.5f else 1f)
                                .height(150.dp)
                        ) {
                            val current = routineList[i]
                            RoutineButton(
                                RoutineID = current.id,
                                //TODO cambiar dependiendo de las categorías
                                RoutineImageID = getResourceIdRoutineImage(
                                    category = current.category,
                                    routineId = current.id
                                ),
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
                                .height(150.dp)
                        ) {
                            if (!onlyOne) {
                                val current = routineList[i + 1]
                                RoutineButton(
                                    RoutineID = current.id,
                                    //TODO cambiar dependiendo de las categorías
                                    RoutineImageID = getResourceIdRoutineImage(
                                        category = current.category,
                                        routineId = current.id
                                    ),
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


@Composable
fun RoutineButtonList(
    routineList: List<Routine> = listOf(),
    title: String = "Rutinas",
    onGetMoreRoutines: () -> Unit = {},
    onNavigateToViewRoutine: (routineId: Int) -> Unit = {},
    isLastPage: Boolean = false,
    noRoutinesMessage: String = stringResource(R.string.no_routines_message)
) {
    Column(
        modifier = Modifier
            .padding(5.dp)
            .wrapContentHeight()
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp)
                .height(50.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.h5,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(bottom = 4.dp)
            )
            if (!isLastPage) {
                Button(onClick = { onGetMoreRoutines() }, modifier = Modifier) {
                    Text(
                        text = stringResource(R.string.ShowMore).uppercase(),
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            if (routineList.isEmpty()) {
                NoRoutinesMessage(msg = noRoutinesMessage)
            }
            routineList.forEach { routine ->
                RoutineButton(
                    routine.id,
                    RoutineImageID = getResourceIdRoutineImage(
                        category = routine.category,
                        routineId = routine.id
                    ),
                    routine.name,
                    onNavigateToViewRoutine = onNavigateToViewRoutine,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(125.dp)
                        .padding(bottom = 3.dp)
                )
            }
        }

    }

}