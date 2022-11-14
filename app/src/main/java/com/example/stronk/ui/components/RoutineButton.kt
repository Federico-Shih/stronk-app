package com.example.stronk

import android.graphics.BlurMaskFilter
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.stronk.ui.theme.StronkTheme

@Composable
fun RoutineButton(RoutineID: Int, RoutineImageID: Int, RoutineName: String,modifierButton: Modifier = Modifier, onNavigateToViewRoutine: (routineId: Int) -> Unit={}) {
    val image: Painter = painterResource(id = RoutineImageID)
    Card(modifier = modifierButton
        .padding(10.dp)
        .clickable { onNavigateToViewRoutine(RoutineID) }, elevation = 10.dp,
        border = BorderStroke(2.dp, MaterialTheme.colors.primaryVariant),
        shape = RoundedCornerShape(20.dp)
        ) {
        Box {
            Image(
                painter = image, contentDescription = stringResource(R.string.category_representing_image),
                contentScale = ContentScale.Crop,
                modifier = Modifier.matchParentSize(),
                alignment = Alignment.Center,
            )
            Box(modifier = Modifier.background(
                Brush.verticalGradient(
                    0.6F to Color.Transparent,
                    0.7F to Color.Black.copy(alpha = 0.2F),
                    1F to Color.Black.copy(alpha = 0.9F)
                )).matchParentSize())
            Text(
                text = RoutineName, fontSize = 20.sp, modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
                    .align(Alignment.BottomStart),
                fontStyle = MaterialTheme.typography.h6.fontStyle,
                color = Color.White,
                fontWeight = FontWeight(600)
            )
        }
    }


}

@Preview(showBackground = true)
@Composable
fun RoutineButtonPreview(listRow: List<String> = List(20){"$it"}) {
    StronkTheme {
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
            Box {
                Column(modifier= Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceEvenly) {
                    Row(
                        modifier = Modifier
                            .padding(all = 8.dp)
                            .height(IntrinsicSize.Min)
                            .fillMaxWidth()
                            .wrapContentWidth(Alignment.Start),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Column(modifier = Modifier
                            .fillMaxWidth(0.5f)
                            .height(200.dp)) {
                            RoutineButton(
                                RoutineID = 1,
                                RoutineImageID = R.drawable.abdos,
                                RoutineName = "Abdominales en 15 minutos",
                                modifierButton = Modifier
                                    .fillMaxWidth()
                                    .fillMaxHeight()
                            )
                        }
                        Column(modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)){
                            RoutineButton(
                                RoutineID = 1,
                                RoutineImageID = R.drawable.abdos,
                                RoutineName = "Abdominales en 15 minutos",
                                modifierButton = Modifier
                                    .fillMaxWidth()
                                    .fillMaxHeight()
                            )
                        }
                    }
                    RoutineButton(
                        RoutineID = 1,
                        RoutineImageID = R.drawable.abdos,
                        RoutineName = "Abdominales en 15 minutos",
                        modifierButton = Modifier
                            .fillMaxWidth()
                            .height(150.dp)
                    )
                }


            }
        }
    }
}
