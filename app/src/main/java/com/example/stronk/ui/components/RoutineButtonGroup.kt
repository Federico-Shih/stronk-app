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
import com.example.stronk.ui.theme.StronkTheme


@Composable
fun RoutineButtonGroup(listRow: List<String>, title: String)
{
    Card(modifier = Modifier
        .padding(15.dp),
        elevation = 10.dp) {
        Column( verticalArrangement = Arrangement.Top) {
            Text(title, modifier = Modifier.padding(start = 20.dp, top = 10.dp), style = MaterialTheme.typography.h4)
            for (i in 1 until listRow.size step 2) {
                Row(
                    modifier = Modifier
                        .padding(all = 8.dp)
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
                        RoutineButton(
                            RoutineID = 1,
                            RoutineImageID = R.drawable.abdos,
                            RoutineName = (i).toString(),
                            modifierButton = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight()
                        )
                    }
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                    ) {
                        RoutineButton(
                            RoutineID = 2,
                            RoutineImageID = R.drawable.abdos,
                            RoutineName = (i+1).toString(),
                            modifierButton = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight()
                        )
                    }
                }
            }
            Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth())
            {
                Button(onClick = { /*TODO*/ }, shape = CircleShape) {
                    Text("Ver más")
                }
            }
        }
    }
}

@Composable
@Preview
fun PreviewGroup(listRow: List<String> = List(4){"$it"})
{
    StronkTheme {
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
            Box {
                RoutineButtonGroup(listRow, "Title")
            }
        }
    }
}

