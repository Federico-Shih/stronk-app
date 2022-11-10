package com.example.stronk.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.stronk.ui.components.RatingCard
import com.example.stronk.ui.theme.StronkTheme
import androidx.compose.material.Chip
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.ui.res.stringResource
import com.example.stronk.R
import com.example.stronk.state.CycleInfo
import com.example.stronk.state.ExInfo
import com.example.stronk.ui.components.CompleteRoutine

@ExperimentalMaterialApi
@Composable
fun ViewRoutineScreen() {
    val routineName = "Abdominales en 15 minutos"
    val routineDescription = "Esta rutina es perfecta para cuando no tienes mucho tiempo, pero quieres hacer algo de ejercicio."
    val creatorProfileImage = "https://picsum.photos/100"
    val creatorName = "Juan Perez"
    val category = "Full Body"
    val difficulty = "Principiante"
    val rating = 3
    val cycleList = listOf(
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

    Column(modifier = Modifier
        .padding(10.dp)
        .fillMaxSize().verticalScroll(rememberScrollState())) {
        Text(text = routineName, style = MaterialTheme.typography.h5, fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 4.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(
                model = creatorProfileImage,
                contentDescription = null,
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
            Text(text = stringResource(id = R.string.made_by_x, creatorName), modifier = Modifier.padding(start = 8.dp))
        }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Top) {
            Column() {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "${stringResource(id = R.string.category)}:", modifier = Modifier.padding(end = 10.dp), fontWeight = FontWeight.SemiBold)
                    Chip(onClick = {}) {
                        Text(text = category)
                    }
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "${stringResource(id = R.string.difficulty)}:", modifier = Modifier.padding(end = 10.dp), fontWeight = FontWeight.SemiBold)
                    Chip(onClick = {}) {
                        Text(text = difficulty)
                    }
                }
            }
            RatingCard(rating = rating, modifier = Modifier
                .padding(top = 10.dp)
                .wrapContentWidth())
        }
        Text(text = "${stringResource(id = R.string.description)}:", fontWeight = FontWeight.SemiBold)
        Text(text = routineDescription)
        CompleteRoutine(cycleList = cycleList)
    }
}

@Preview(showBackground = true)
@ExperimentalMaterialApi
@Composable
fun ExampleRoutineView() {
    StronkTheme() {
        ViewRoutineScreen()
    }
}