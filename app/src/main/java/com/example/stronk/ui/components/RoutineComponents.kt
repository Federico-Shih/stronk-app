package com.example.stronk

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.Role.Companion.Image
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import coil.request.ImageRequest
import com.example.stronk.ui.theme.StronkTheme
import androidx.compose.foundation.Canvas
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color

class ExInfo(val name: String, val reps: Int?, val duration: Int?, val imageUrl: String?)

@ExperimentalMaterialApi
@Composable
fun Cycle(title: String, exList: List<ExInfo>) {

    val stroke = Stroke(width = 8.dp.value,
        pathEffect = PathEffect.dashPathEffect(floatArrayOf(40.dp.value), 20.dp.value)
    )
    Box() {
        Canvas(modifier = Modifier
            .fillMaxWidth()) {
            drawRoundRect(Color.Blue, style= stroke, cornerRadius = CornerRadius(8.dp.toPx()))
        }

        Column(
        ) {
            Text(text = title, style = MaterialTheme.typography.h5)
            for (ex in exList) {
                ExerciseItem(ex)
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
            text = { Text(text = exercise.name) },
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