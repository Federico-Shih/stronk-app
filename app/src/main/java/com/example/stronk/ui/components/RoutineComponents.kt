package com.example.stronk.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.example.stronk.R
import com.example.stronk.state.CycleInfo
import com.example.stronk.state.ExInfo
import kotlinx.coroutines.launch

@Composable
@ExperimentalAnimationApi
@ExperimentalFoundationApi
fun ExecutingCycles(
    prevCycle: CycleInfo?,
    currentCycle: CycleInfo,
    nextCycle: CycleInfo?,
    currentExercise: Int,
    currentRepetition: Int,
    shouldMoveScrolling: Boolean = false,
    expandedExerciseVariant: ExerciseItemType = ExerciseItemType.EXPANDED,
) {

    val color = MaterialTheme.colors.primary
    Box(modifier = Modifier.height(IntrinsicSize.Max)) {
        Canvas(
            modifier = Modifier
                .matchParentSize()
                .padding(
                    start = 87.dp,
                    top = if (prevCycle == null) 10.dp else 0.dp,
                    bottom = if (nextCycle == null) 10.dp else 0.dp
                )
                .zIndex(1f)
        ) {
            val canvasHeight = size.height
            drawLine(
                start = Offset(x = 0f, y = 0f),
                end = Offset(x = 0f, y = canvasHeight),
                color = color,
                strokeWidth = 10.dp.value
            )
        }
        Column {
            if (prevCycle != null) {
                CollapsedCycle(
                    title = prevCycle.name,
                    cycleReps = prevCycle.cycleReps,
                    label = stringResource(id = R.string.prev_cy)
                )
            } else {
                StartEndLabel(label = stringResource(id = R.string.start))
            }

            ExecuteCycle(
                title = currentCycle.name,
                exList = currentCycle.exList,
                cycleReps = currentCycle.cycleReps,
                currentExercise = currentExercise,
                currentRepetition = currentRepetition,
                shouldMoveScrolling = shouldMoveScrolling,
                expandedExerciseVariant = expandedExerciseVariant
            )
            if (nextCycle != null) {
                CollapsedCycle(
                    title = nextCycle.name,
                    cycleReps = nextCycle.cycleReps,
                    label = stringResource(id = R.string.next_cy)
                )
            } else {
                StartEndLabel(label = stringResource(id = R.string.end))
            }
        }
    }
}

@ExperimentalAnimationApi
@Composable
@ExperimentalFoundationApi
fun CompleteRoutine(cycleList: List<CycleInfo>) {
    val color = MaterialTheme.colors.primary
    Box(modifier = Modifier.height(IntrinsicSize.Max)) {
        Canvas(
            modifier = Modifier
                .matchParentSize()
                .padding(start = 87.dp, top = 16.dp, bottom = 16.dp)
                .zIndex(1f)
        ) {
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
fun CollapsedCycle(title: String, cycleReps: Int, label: String) {
    val dashedStroke = Stroke(
        width = 4.dp.value,
        pathEffect = PathEffect.dashPathEffect(floatArrayOf(40f, 40f), 0f)
    )
    val color = MaterialTheme.colors.primary
    Box(
        modifier = Modifier
            .wrapContentSize()
            .padding(10.dp)
    ) {
        Canvas(modifier = Modifier.matchParentSize()) {
            drawRoundRect(
                color = color,
                style = dashedStroke,
                cornerRadius = CornerRadius(20.dp.toPx())
            )
        }
        Row(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(bottom = 0.dp, start = 20.dp)
                .zIndex(1f)
        )
        {

            Text(
                text = "$cycleReps",
                fontWeight = FontWeight.SemiBold,
                fontSize = 20.sp,
                modifier = Modifier.alignByBaseline()
            )
            Text(
                text = stringResource(R.string.reps_),
                fontSize = 11.sp,
                modifier = Modifier.alignByBaseline()
            )
        }
        Row(
            modifier = Modifier
                .height(IntrinsicSize.Max)
                .padding(start = 65.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Filled.Circle,
                contentDescription = null,
                modifier = Modifier
                    .size(34.dp)
                    .padding(end = 10.dp),
                tint = MaterialTheme.colors.primary,
            )
            Row(modifier = Modifier.padding(end = 10.dp)) {
                Text(
                    text = "${label}: ",
                    fontWeight = FontWeight.Light,
                    fontSize = 14.sp,
                    modifier = Modifier.alignByBaseline()
                )
                Text(
                    text = title,
                    style = MaterialTheme.typography.h6,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.alignByBaseline()
                )
            }
        }
    }
}

@Composable
@ExperimentalAnimationApi
@ExperimentalFoundationApi
fun ExecuteCycle(
    title: String,
    exList: List<ExInfo>,
    cycleReps: Int,
    currentExercise: Int,
    currentRepetition: Int,
    shouldMoveScrolling: Boolean = false,
    expandedExerciseVariant: ExerciseItemType = ExerciseItemType.EXPANDED
) {
    val dashedStroke = Stroke(
        width = 4.dp.value,
        pathEffect = PathEffect.dashPathEffect(floatArrayOf(40f, 40f), 0f)
    )
    val solidStroke = Stroke(width = 10.dp.value)

    val color = MaterialTheme.colors.primary
    Box(
        modifier = Modifier
            .wrapContentSize()
            .padding(10.dp)
    ) {
        Canvas(modifier = Modifier.matchParentSize()) {
            drawRoundRect(
                color = color,
                style = dashedStroke,
                cornerRadius = CornerRadius(20.dp.toPx())
            )
        }
        Row(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(bottom = 25.dp, start = 22.dp)
                .zIndex(1f)
        )
        {
            Text(
                text = "${currentRepetition + 1}",
                fontWeight = FontWeight.SemiBold,
                fontSize = 28.sp,
                modifier = Modifier.alignByBaseline()
            )
            Text(text = "/$cycleReps", fontSize = 14.sp, modifier = Modifier.alignByBaseline())
        }


        Box(
            modifier = Modifier
                .height(IntrinsicSize.Max)
                .padding(15.dp)
        ) {
            Canvas(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(62.dp)
                    .zIndex(1f)
            ) {
                drawRoundRect(
                    color = color,
                    style = solidStroke,
                    cornerRadius = CornerRadius(20.dp.toPx())
                )
            }
            Column(
                modifier = Modifier
                    .padding(start = 40.dp)
                    .wrapContentHeight()
            ) {
                Column(modifier = Modifier.padding(start = 42.dp, bottom = 10.dp)) {
                    Text(
                        text = "${stringResource(id = R.string.current_cycle)}: ",
                        fontWeight = FontWeight.Light,
                        fontSize = 14.sp
                    )
                    Text(
                        text = title,
                        style = MaterialTheme.typography.h6,
                        fontWeight = FontWeight.Bold
                    )
                }
                for (ex in exList) {
                    ExerciseItem(
                        exercise = ex,
                        variant = if (currentExercise == exList.indexOf(ex)) expandedExerciseVariant else ExerciseItemType.NO_PIC,
                        shouldMoveScrolling = shouldMoveScrolling
                    )
                }
            }
        }

    }
}

@Composable
@ExperimentalAnimationApi
@ExperimentalFoundationApi
fun Cycle(title: String, exList: List<ExInfo>, cycleReps: Int) {

    val dashedStroke = Stroke(
        width = 4.dp.value,
        pathEffect = PathEffect.dashPathEffect(floatArrayOf(40f, 40f), 0f)
    )
    val solidStroke = Stroke(width = 10.dp.value)


    val color = MaterialTheme.colors.primary
    Box(
        modifier = Modifier
            .wrapContentSize()
            .padding(10.dp)
    ) {
        Canvas(modifier = Modifier.matchParentSize()) {
            drawRoundRect(
                color = color,
                style = dashedStroke,
                cornerRadius = CornerRadius(20.dp.toPx())
            )
        }
        Row(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(bottom = 25.dp, start = 22.dp)
                .zIndex(1f)
        )
        {
            Text(
                text = "$cycleReps",
                fontWeight = FontWeight.SemiBold,
                fontSize = 20.sp,
                modifier = Modifier.alignByBaseline()
            )
            Text(
                text = stringResource(R.string.reps_),
                fontSize = 11.sp,
                modifier = Modifier.alignByBaseline()
            )
        }


        Box(
            modifier = Modifier
                .height(IntrinsicSize.Max)
                .padding(15.dp)
        ) {
            Canvas(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(62.dp)
                    .zIndex(1f)
            ) {
                drawRoundRect(
                    color = color,
                    style = solidStroke,
                    cornerRadius = CornerRadius(20.dp.toPx())
                )
            }
            Column(
                modifier = Modifier
                    .padding(start = 40.dp)
                    .wrapContentHeight()
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.h6,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 32.dp, bottom = 10.dp)
                )
                for (ex in exList) {
                    ExerciseItem(ex, ExerciseItemType.REGULAR)
                }
            }
        }

    }
}


@Composable
fun StartEndLabel(label: String) {
    Row(
        modifier = Modifier
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
@ExperimentalFoundationApi
fun ExerciseItem(
    exercise: ExInfo,
    variant: ExerciseItemType = ExerciseItemType.REGULAR,
    shouldMoveScrolling: Boolean = false
) {
    val background =
        if (variant == ExerciseItemType.EXPANDED || variant == ExerciseItemType.EXPANDED_NO_PIC) MaterialTheme.colors.surface else Color.Transparent
    val nameSize =
        if (variant == ExerciseItemType.EXPANDED || variant == ExerciseItemType.EXPANDED_NO_PIC) 24.sp else 16.sp
    val iconTopPadding =
        if (variant == ExerciseItemType.EXPANDED || variant == ExerciseItemType.EXPANDED_NO_PIC) 27.dp else 0.dp

    val bringIntoViewRequester = remember { BringIntoViewRequester() }
    val coroutineScope = rememberCoroutineScope()
    val surroundings = Rect(0f, (-200).dp.value, 0f, if(variant == ExerciseItemType.EXPANDED) 800.dp.value else 400.dp.value)

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .background(background)
            .padding(10.dp)
            .bringIntoViewRequester(bringIntoViewRequester)
    ) {
        LaunchedEffect(key1 = variant) {
            if (shouldMoveScrolling && (variant == ExerciseItemType.EXPANDED || variant == ExerciseItemType.EXPANDED_NO_PIC)) {
                coroutineScope.launch {
                    bringIntoViewRequester.bringIntoView(surroundings)
                }
            }
        }

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
                        .padding(top = iconTopPadding)
                        .size(24.dp),
                    tint = MaterialTheme.colors.primary,
                )
                Column(
                    modifier = Modifier
                        .padding(bottom = 14.dp, start = 10.dp)
                ) {
                    if (variant == ExerciseItemType.EXPANDED || variant == ExerciseItemType.EXPANDED_NO_PIC) {
                        Text(
                            text = stringResource(id = R.string.in_progress_),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Light
                        )
                    }
                    Text(
                        text = exercise.name,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = nameSize
                    )
                    Column() {
                        if (exercise.reps != null) {
                            Text(text = stringResource(id = R.string.x_repetitions, exercise.reps))
                        }
                        if (exercise.duration != null) {
                            Text(text = stringResource(id = R.string.x_seconds, exercise.duration))
                        }
                    }
                    if (variant == ExerciseItemType.EXPANDED) {
                        AsyncImage(
                            model = exercise.imageUrl?:"",//TODO Imagen default
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

            if (variant == ExerciseItemType.REGULAR) {
                AsyncImage(
                    model = exercise.imageUrl?:"",
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
            Icon(
                imageVector = Icons.Filled.Star,
                contentDescription = null,
                tint = starsColor,
                modifier = Modifier.size(starsSize.dp)
            )
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
fun ClickableRatingBar(
    modifier: Modifier = Modifier,
    stars: Int = 5,
    starsColor: Color = MaterialTheme.colors.secondary,
    starsSize: Int = 18,
    currentRating: Int,
    onRatingChange: (Int) -> Unit
) {

    Row(modifier = modifier) {
        repeat(stars) {
            Icon(
                imageVector = if (it < currentRating) Icons.Filled.Star else Icons.Filled.StarOutline,
                contentDescription = null,
                tint = starsColor,
                modifier = Modifier
                    .size(starsSize.dp)
                    .clickable { onRatingChange(it + 1) },
            )
        }
    }
}

@Composable
fun RatingCard(rating: Int, modifier: Modifier) {
    Card(modifier = modifier) {
        Row(modifier = Modifier.padding(10.dp), verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "$rating",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(end = 10.dp)
            )
            RatingBar(rating = rating, stars = 5, starsColor = MaterialTheme.colors.primary)
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//@ExperimentalAnimationApi
//@ExperimentalFoundationApi
//fun ExerciseList() {
//    StronkTheme {
//        Box(modifier = Modifier
//            .fillMaxSize()
//            .verticalScroll(rememberScrollState()))
//        {
//            ExecutingCycles(
//                currentCycle = CycleInfo(
//                    "Sugerida por copilot",
//                    listOf(
//                        ExInfo(
//                            "Pushups",
//                            10,
//                            3,
//                            "https://images.ecestaticos.com/WAot9QyeV2vzRuE1gVu55WLdv7Y=/0x0:0x0/1200x900/filters:fill(white):format(jpg)/f.elconfidencial.com%2Foriginal%2Fb3c%2Fc7c%2Fff6%2Fb3cc7cff6cc1ee44df172f15afa3e4f9.jpg"
//                        ),
//                        ExInfo(
//                            "Squats",
//                            10,
//                            null,
//                            "https://images.ecestaticos.com/WAot9QyeV2vzRuE1gVu55WLdv7Y=/0x0:0x0/1200x900/filters:fill(white):format(jpg)/f.elconfidencial.com%2Foriginal%2Fb3c%2Fc7c%2Fff6%2Fb3cc7cff6cc1ee44df172f15afa3e4f9.jpg"
//                        ),
//                        ExInfo(
//                            "Pullups",
//                            null,
//                            10,
//                            "https://images.ecestaticos.com/WAot9QyeV2vzRuE1gVu55WLdv7Y=/0x0:0x0/1200x900/filters:fill(white):format(jpg)/f.elconfidencial.com%2Foriginal%2Fb3c%2Fc7c%2Fff6%2Fb3cc7cff6cc1ee44df172f15afa3e4f9.jpg"
//                        ),
//                        ExInfo(
//                            "Planks",
//                            null,
//                            10,
//                            "https://images.ecestaticos.com/WAot9QyeV2vzRuE1gVu55WLdv7Y=/0x0:0x0/1200x900/filters:fill(white):format(jpg)/f.elconfidencial.com%2Foriginal%2Fb3c%2Fc7c%2Fff6%2Fb3cc7cff6cc1ee44df172f15afa3e4f9.jpg"
//                        ),
//                    ),
//                    64
//                ),
//                prevCycle = CycleInfo(
//                    "Sugerida por copilot",
//                    listOf(
//                        ExInfo(
//                            "Pushups",
//                            10,
//                            3,
//                            "https://images.ecestaticos.com/WAot9QyeV2vzRuE1gVu55WLdv7Y=/0x0:0x0/1200x900/filters:fill(white):format(jpg)/f.elconfidencial.com%2Foriginal%2Fb3c%2Fc7c%2Fff6%2Fb3cc7cff6cc1ee44df172f15afa3e4f9.jpg"
//                        ),
//                        ExInfo(
//                            "Squats",
//                            10,
//                            null,
//                            "https://images.ecestaticos.com/WAot9QyeV2vzRuE1gVu55WLdv7Y=/0x0:0x0/1200x900/filters:fill(white):format(jpg)/f.elconfidencial.com%2Foriginal%2Fb3c%2Fc7c%2Fff6%2Fb3cc7cff6cc1ee44df172f15afa3e4f9.jpg"
//                        ),
//                        ExInfo(
//                            "Pullups",
//                            null,
//                            10,
//                            "https://images.ecestaticos.com/WAot9QyeV2vzRuE1gVu55WLdv7Y=/0x0:0x0/1200x900/filters:fill(white):format(jpg)/f.elconfidencial.com%2Foriginal%2Fb3c%2Fc7c%2Fff6%2Fb3cc7cff6cc1ee44df172f15afa3e4f9.jpg"
//                        ),
//                        ExInfo(
//                            "Planks",
//                            null,
//                            10,
//                            "https://images.ecestaticos.com/WAot9QyeV2vzRuE1gVu55WLdv7Y=/0x0:0x0/1200x900/filters:fill(white):format(jpg)/f.elconfidencial.com%2Foriginal%2Fb3c%2Fc7c%2Fff6%2Fb3cc7cff6cc1ee44df172f15afa3e4f9.jpg"
//                        ),
//                    ),
//                    6
//                ),
//                nextCycle = CycleInfo(
//                    "Sugerida por copilot",
//                    listOf(
//                        ExInfo(
//                            "Pushups",
//                            10,
//                            3,
//                            "https://images.ecestaticos.com/WAot9QyeV2vzRuE1gVu55WLdv7Y=/0x0:0x0/1200x900/filters:fill(white):format(jpg)/f.elconfidencial.com%2Foriginal%2Fb3c%2Fc7c%2Fff6%2Fb3cc7cff6cc1ee44df172f15afa3e4f9.jpg"
//                        ),
//                        ExInfo(
//                            "Squats",
//                            10,
//                            null,
//                            "https://images.ecestaticos.com/WAot9QyeV2vzRuE1gVu55WLdv7Y=/0x0:0x0/1200x900/filters:fill(white):format(jpg)/f.elconfidencial.com%2Foriginal%2Fb3c%2Fc7c%2Fff6%2Fb3cc7cff6cc1ee44df172f15afa3e4f9.jpg"
//                        ),
//                        ExInfo(
//                            "Pullups",
//                            null,
//                            10,
//                            "https://images.ecestaticos.com/WAot9QyeV2vzRuE1gVu55WLdv7Y=/0x0:0x0/1200x900/filters:fill(white):format(jpg)/f.elconfidencial.com%2Foriginal%2Fb3c%2Fc7c%2Fff6%2Fb3cc7cff6cc1ee44df172f15afa3e4f9.jpg"
//                        ),
//                        ExInfo(
//                            "Planks",
//                            null,
//                            10,
//                            "https://images.ecestaticos.com/WAot9QyeV2vzRuE1gVu55WLdv7Y=/0x0:0x0/1200x900/filters:fill(white):format(jpg)/f.elconfidencial.com%2Foriginal%2Fb3c%2Fc7c%2Fff6%2Fb3cc7cff6cc1ee44df172f15afa3e4f9.jpg"
//                        ),
//                    ),
//                    6
//                ),
//
//                currentExercise = 2,
//                currentRepetition = 17
//            )
//        }
//    }
//}