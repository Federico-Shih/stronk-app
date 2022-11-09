package com.example.stronk.ui.components

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarOutline
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.example.stronk.R

class ExInfo(val name: String, val reps: Int?, val duration: Int?, val imageUrl: String?)

class CycleInfo(val name: String, val exList: List<ExInfo>, val cycleReps: Int)

@Composable
fun CompleteRoutine(cycleList: List<CycleInfo>) {
    val color = MaterialTheme.colors.primary
    Box(modifier = Modifier.height(IntrinsicSize.Max)) {
        Canvas(modifier = Modifier
            .matchParentSize()
            .padding(start = 87.dp, top = 16.dp, bottom = 16.dp)
            .zIndex(1f)) {
            val canvasHeight = size.height
            drawLine(
                start = Offset(x = 0f, y = 0f),
                end = Offset(x = 0f, y = canvasHeight),
                color = color,
                strokeWidth = 10.dp.value
            )
        }
        Column() {
            StartEndLabel(label = stringResource(id = R.string.start))
            cycleList.forEach { cycle ->
                Cycle(cycle.name, cycle.exList, cycle.cycleReps)
            }
            StartEndLabel(label = stringResource(id = R.string.end))
        }
    }
}

@Composable
fun Cycle(title: String, exList: List<ExInfo>, cycleReps: Int) {


    val dashedStroke = Stroke(width = 4.dp.value, pathEffect = PathEffect.dashPathEffect(floatArrayOf(40f, 40f), 0f))
    val solidStroke = Stroke(width = 10.dp.value)


    val color = MaterialTheme.colors.primary
    Box(modifier = Modifier
        .wrapContentSize()
        .padding(10.dp)) {
        Canvas(modifier = Modifier.matchParentSize()) {
            drawRoundRect(color = color, style= dashedStroke, cornerRadius = CornerRadius(20.dp.toPx()))
        }
        Row(modifier = Modifier
            .align(Alignment.BottomStart)
            .padding(bottom = 25.dp, start = 22.dp)
            .zIndex(1f)
        )
        {
            Text(text = "$cycleReps", fontWeight = FontWeight.SemiBold, fontSize = 20.sp, modifier = Modifier.alignByBaseline())
            Text(text = stringResource(R.string.reps_), fontSize = 11.sp, modifier = Modifier.alignByBaseline())
        }


        Box(modifier = Modifier
            .height(IntrinsicSize.Max)
            .padding(15.dp)) {
            Canvas(modifier = Modifier
                .fillMaxHeight()
                .width(62.dp)
                .zIndex(1f)) {
                drawRoundRect(color = color, style= solidStroke, cornerRadius = CornerRadius(20.dp.toPx()))
            }
            Column(modifier = Modifier
                .padding(start = 40.dp)
                .wrapContentHeight()) {
                Text(text = title, style = MaterialTheme.typography.h6, fontWeight = FontWeight.Bold, modifier = Modifier.padding(start = 32.dp, bottom = 10.dp))
                for (ex in exList) {
                    ExerciseItem(ex, ExerciseItemType.values()[(exList.indexOf(ex)+3) % 4])
                }
            }
        }

    }
}


@Composable
fun StartEndLabel(label: String)
{
    Row(modifier = Modifier
        .height(IntrinsicSize.Max)
        .padding(start = 72.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            Icons.Filled.Circle,
            contentDescription = null,
            modifier = Modifier
                .size(40.dp)
                .padding(end = 10.dp),
            tint = MaterialTheme.colors.primary,
        )
        Text(text = label, fontWeight = FontWeight.SemiBold)
    }
}

enum class ExerciseItemType {
    REGULAR, NO_PIC, EXPANDED, EXPANDED_NO_PIC
}

@Composable
fun ExerciseItem(exercise: ExInfo, variant: ExerciseItemType = ExerciseItemType.REGULAR) {
    val background = if(variant == ExerciseItemType.EXPANDED || variant == ExerciseItemType.EXPANDED_NO_PIC) MaterialTheme.colors.background else Color.Transparent
    val nameSize = if(variant == ExerciseItemType.EXPANDED || variant == ExerciseItemType.EXPANDED_NO_PIC) 24.sp else 16.sp
    val iconTopPadding = if(variant == ExerciseItemType.EXPANDED || variant == ExerciseItemType.EXPANDED_NO_PIC) 27.dp else 0.dp
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .background(background)
            .padding(10.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Max),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Row(horizontalArrangement = Arrangement.Start)
            {
                Icon(
                    Icons.Filled.Circle,
                    contentDescription = null,
                    modifier = Modifier
                        .padding(top = iconTopPadding).size(24.dp),
                    tint = MaterialTheme.colors.primary,
                )
                Column(modifier = Modifier
                    .padding(bottom = 14.dp, start = 10.dp)) {
                    if(variant == ExerciseItemType.EXPANDED || variant == ExerciseItemType.EXPANDED_NO_PIC) {
                        Text(text = stringResource(id = R.string.in_progress_), fontSize = 16.sp, fontWeight = FontWeight.Light)
                    }
                    Text(text = exercise.name, fontWeight = FontWeight.SemiBold, fontSize = nameSize)
                    Column() {
                        if (exercise.reps != null) {
                            Text(text = stringResource(id = R.string.x_repetitions, exercise.reps))
                        }
                        if (exercise.duration != null) {
                            Text(text = stringResource(id = R.string.x_seconds, exercise.duration))
                        }
                    }
                    if(variant == ExerciseItemType.EXPANDED) {
                        AsyncImage(
                            model = exercise.imageUrl,
                            contentDescription = null,
                            modifier = Modifier
                                .size(width = 140.dp, height = 100.dp)
                                .padding(top = 10.dp)
                                .clip(RoundedCornerShape(10.dp)),
                            contentScale = ContentScale.Crop,

                        )
                    }
                }
            }

            if(variant == ExerciseItemType.REGULAR) {
                AsyncImage(
                    model = exercise.imageUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .sizeIn(maxWidth = 64.dp, maxHeight = 48.dp)
                        .align(Alignment.CenterVertically),
                )
            }

        }
    }

}

@Composable
fun RatingBar(
    modifier: Modifier = Modifier,
    rating: Int = 0,
    stars: Int = 5,
    starsColor: Color = MaterialTheme.colors.secondary,
    starsSize: Int = 18,
) {

    Row(modifier = modifier) {
        repeat(rating) {
            Icon(imageVector = Icons.Filled.Star, contentDescription = null, tint = starsColor, modifier = Modifier.size(starsSize.dp))
        }

        repeat(stars - rating) {
            Icon(
                imageVector = Icons.Filled.StarOutline,
                contentDescription = null,
                tint = starsColor,
                modifier = Modifier.size(starsSize.dp)
            )
        }
    }
}

@Composable
fun RatingCard(rating: Int, modifier: Modifier)
{
    Card(modifier = modifier) {
        Row(modifier = Modifier.padding(10.dp), verticalAlignment = Alignment.CenterVertically) {
            Text(text = "$rating", fontSize = 16.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(end = 10.dp))
            RatingBar(rating = rating, stars = 5, starsColor = MaterialTheme.colors.primary)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ExerciseList() {
    StronkTheme {
        Box(modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()))
        {
            CompleteRoutine(
                cycleList = listOf(
                    CycleInfo(
                        "Sugerida por copilot",
                        listOf(
                            ExInfo(
                                "Pushups",
                                10,
                                3,
                                "https://images.ecestaticos.com/WAot9QyeV2vzRuE1gVu55WLdv7Y=/0x0:0x0/1200x900/filters:fill(white):format(jpg)/f.elconfidencial.com%2Foriginal%2Fb3c%2Fc7c%2Fff6%2Fb3cc7cff6cc1ee44df172f15afa3e4f9.jpg"
                            ),
                            ExInfo(
                                "Squats",
                                10,
                                null,
                                "https://images.ecestaticos.com/WAot9QyeV2vzRuE1gVu55WLdv7Y=/0x0:0x0/1200x900/filters:fill(white):format(jpg)/f.elconfidencial.com%2Foriginal%2Fb3c%2Fc7c%2Fff6%2Fb3cc7cff6cc1ee44df172f15afa3e4f9.jpg"
                            ),
                            ExInfo(
                                "Pullups",
                                null,
                                10,
                                "https://images.ecestaticos.com/WAot9QyeV2vzRuE1gVu55WLdv7Y=/0x0:0x0/1200x900/filters:fill(white):format(jpg)/f.elconfidencial.com%2Foriginal%2Fb3c%2Fc7c%2Fff6%2Fb3cc7cff6cc1ee44df172f15afa3e4f9.jpg"
                            ),
                            ExInfo(
                                "Planks",
                                null,
                                10,
                                "https://images.ecestaticos.com/WAot9QyeV2vzRuE1gVu55WLdv7Y=/0x0:0x0/1200x900/filters:fill(white):format(jpg)/f.elconfidencial.com%2Foriginal%2Fb3c%2Fc7c%2Fff6%2Fb3cc7cff6cc1ee44df172f15afa3e4f9.jpg"
                            ),
                        ),
                        6
                    ),
                    CycleInfo(
                        "AAAAAA",
                        listOf(
                            ExInfo(
                                "Pushups",
                                10,
                                null,
                                "https://images.ecestaticos.com/WAot9QyeV2vzRuE1gVu55WLdv7Y=/0x0:0x0/1200x900/filters:fill(white):format(jpg)/f.elconfidencial.com%2Foriginal%2Fb3c%2Fc7c%2Fff6%2Fb3cc7cff6cc1ee44df172f15afa3e4f9.jpg"
                            ),
                            ExInfo(
                                "Squats",
                                10,
                                50,
                                "https://images.ecestaticos.com/WAot9QyeV2vzRuE1gVu55WLdv7Y=/0x0:0x0/1200x900/filters:fill(white):format(jpg)/f.elconfidencial.com%2Foriginal%2Fb3c%2Fc7c%2Fff6%2Fb3cc7cff6cc1ee44df172f15afa3e4f9.jpg"
                            ),
                            ExInfo(
                                "Pullups",
                                null,
                                10,
                                "https://images.ecestaticos.com/WAot9QyeV2vzRuE1gVu55WLdv7Y=/0x0:0x0/1200x900/filters:fill(white):format(jpg)/f.elconfidencial.com%2Foriginal%2Fb3c%2Fc7c%2Fff6%2Fb3cc7cff6cc1ee44df172f15afa3e4f9.jpg"
                            ),
                            ExInfo(
                                "Planks",
                                null,
                                10,
                                "https://images.ecestaticos.com/WAot9QyeV2vzRuE1gVu55WLdv7Y=/0x0:0x0/1200x900/filters:fill(white):format(jpg)/f.elconfidencial.com%2Foriginal%2Fb3c%2Fc7c%2Fff6%2Fb3cc7cff6cc1ee44df172f15afa3e4f9.jpg"
                            ),
                        ),
                        81
                    )
                )
            )
        }
    }
}