package com.example.stronk.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Circle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.stronk.ui.theme.StronkTheme
import androidx.compose.foundation.Canvas
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight

class ExInfo(val name: String, val reps: Int?, val duration: Int?, val imageUrl: String?)

@ExperimentalMaterialApi
@Composable
fun Cycle(title: String, exList: List<ExInfo>) {


    val dashedStroke = Stroke(width = 10.dp.value, pathEffect = PathEffect.dashPathEffect(floatArrayOf(20f, 20f), 0f))
    val solidStroke = Stroke(width = 10.dp.value)


    val color = MaterialTheme.colors.primary
    Box(modifier = Modifier.wrapContentSize().padding(10.dp)) {
        Canvas(modifier = Modifier.matchParentSize()) {
            drawRoundRect(color = color, style= dashedStroke, cornerRadius = CornerRadius(8.dp.toPx()))
        }

        Column(modifier = Modifier.padding(10.dp)
        ) {
            Text(text = title, style = MaterialTheme.typography.h5, fontWeight = FontWeight.Bold)
            Box(modifier = Modifier.wrapContentSize().padding(10.dp)) {
                Canvas(modifier = Modifier.matchParentSize().padding(end = 260.dp)) {
                    drawRoundRect(color = color, style= solidStroke, cornerRadius = CornerRadius(8.dp.toPx()))
                }
                Column(modifier = Modifier.padding(start = 10.dp)) {
                    for (ex in exList) {
                        ExerciseItem(ex)
                    }
                }
            }
        }
    }
}




@Composable
@ExperimentalMaterialApi
fun ExerciseItem(exercise: ExInfo) {
        ListItem(
            icon = {
                Icon(
                    Icons.Filled.Circle,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                    tint = MaterialTheme.colors.primary,
                )
            },
            text = { Text(text = exercise.name, fontWeight = FontWeight.SemiBold) },
            secondaryText = {
                Row() {
                    if (exercise.reps != null) {
                        Text(text = "${exercise.reps} repetitions")
                        if (exercise.duration != null) {
                            Text(text = " | ")
                        }
                    }
                    if (exercise.duration != null) {
                        Text(text = "${exercise.duration} seconds")
                    }
                }
            },
            trailing = {
                AsyncImage(
                    model = exercise.imageUrl,
                    contentDescription = null,
                    modifier = Modifier.sizeIn(maxHeight = 48.dp, maxWidth = 64.dp),
                )
            }
        )
}

@Preview(showBackground = true)
@Composable
@ExperimentalMaterialApi
fun ExerciseList() {
    StronkTheme {
        Cycle("Sugerida por copilot",
            listOf(
                ExInfo("Pushups", 10, null, "https://images.ecestaticos.com/WAot9QyeV2vzRuE1gVu55WLdv7Y=/0x0:0x0/1200x900/filters:fill(white):format(jpg)/f.elconfidencial.com%2Foriginal%2Fb3c%2Fc7c%2Fff6%2Fb3cc7cff6cc1ee44df172f15afa3e4f9.jpg"),
                ExInfo("Squats", 10, null, "https://images.ecestaticos.com/WAot9QyeV2vzRuE1gVu55WLdv7Y=/0x0:0x0/1200x900/filters:fill(white):format(jpg)/f.elconfidencial.com%2Foriginal%2Fb3c%2Fc7c%2Fff6%2Fb3cc7cff6cc1ee44df172f15afa3e4f9.jpg"),
                ExInfo("Pullups", 10, null, "https://images.ecestaticos.com/WAot9QyeV2vzRuE1gVu55WLdv7Y=/0x0:0x0/1200x900/filters:fill(white):format(jpg)/f.elconfidencial.com%2Foriginal%2Fb3c%2Fc7c%2Fff6%2Fb3cc7cff6cc1ee44df172f15afa3e4f9.jpg"),
                ExInfo("Planks", null, 10, "https://images.ecestaticos.com/WAot9QyeV2vzRuE1gVu55WLdv7Y=/0x0:0x0/1200x900/filters:fill(white):format(jpg)/f.elconfidencial.com%2Foriginal%2Fb3c%2Fc7c%2Fff6%2Fb3cc7cff6cc1ee44df172f15afa3e4f9.jpg"),
            )
        )
    }
}