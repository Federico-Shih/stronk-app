package com.example.stronk

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.stronk.ui.theme.StronkTheme

@Composable
fun RoutineButton(RoutineID: Int, RoutineImageID: Int, RoutineName: String){
    val image: Painter = painterResource(id = RoutineImageID)
    Card(modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight()
        .padding(10.dp)
        .shadow(10.dp)
        .clip(RoundedCornerShape(5.dp))
        .clickable {/* TODO Conectar api*/ }, elevation = 10.dp,
            border= BorderStroke(1.dp, MaterialTheme.colors.primary),
    ) {
        Box{
            Image(
                painter = image, contentDescription = "Image representative to the Category",
                contentScale = ContentScale.Crop, modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
            )
            Text(
                text = RoutineName, fontSize = 20.sp, modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
                    .align(Alignment.BottomStart),
                fontStyle = MaterialTheme.typography.h6.fontStyle,
                color = MaterialTheme.colors.secondary,
                fontWeight = FontWeight(400)
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
                Row(
                    modifier = Modifier
                        .padding(all = 8.dp)
                        .height(IntrinsicSize.Min).fillMaxWidth().wrapContentWidth(Alignment.Start),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Column(modifier = Modifier.fillMaxWidth(0.5f).height(200.dp)) {
                        RoutineButton(
                            RoutineID = 1,
                            RoutineImageID = R.drawable.abdos,
                            RoutineName = "Abdominales en 15 minutos"
                        )
                    }
                    Column(modifier = Modifier.fillMaxWidth().height(200.dp)){
                        RoutineButton(
                            RoutineID = 1,
                            RoutineImageID = R.drawable.abdos,
                            RoutineName = "Abdominales en 15 minutos"
                        )
                    }


                    
              
                }
            }
        }
    }
}
