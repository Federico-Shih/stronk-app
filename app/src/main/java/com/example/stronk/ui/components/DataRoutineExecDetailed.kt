package com.example.stronk.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.stronk.R
import com.example.stronk.ui.theme.LightOliveGreen
import com.example.stronk.ui.theme.StronkTheme

@Composable
fun TitleAndSubtitle(
    MainText: String,
    SecondaryText: String? = null,
    MainFontSize: TextUnit = 24.sp,
    SecondaryFontSize: TextUnit = 18.sp,
    SecondaryTextHeight: Dp = 70.dp
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(5.dp),
        backgroundColor = Color.Transparent, elevation = 0.dp,
    ) {
        Column {
            Text(
                text = MainText,
                textAlign = TextAlign.Center,
                fontSize = MainFontSize,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp),
                color = MaterialTheme.colors.onBackground,
                fontWeight = FontWeight(600)
            )
            Divider(
                color = MaterialTheme.colors.onBackground, thickness = 2.dp,
                modifier = Modifier
                    .fillMaxWidth(0.95F)
                    .align(Alignment.CenterHorizontally)
            )
            if (SecondaryText != null) {
                Text(
                    text = SecondaryText,
                    textAlign = TextAlign.Center,
                    fontSize = SecondaryFontSize,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp)
                        .height(SecondaryTextHeight)
                        .verticalScroll(rememberScrollState()),
                    color = MaterialTheme.colors.onBackground,
                    fontWeight = FontWeight(400),
                )
            }
        }
    }
}

@Composable
fun InfoCycle(
    currentCycle: String,
    cycleRepetitions: Int,
    currentCycleRepetition: Int,
    nextExer: String,
) {
    Column(
        modifier = Modifier
            .padding(10.dp)
            .wrapContentHeight()
    ) {
        /*Row {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(3.dp)
                    .background(color = MaterialTheme.colors.primary, CircleShape)
                    .padding(10.dp),
                text = "Ciclo Actual: $currentCycle",
                textAlign = TextAlign.Start,
                color = MaterialTheme.colors.secondary,
            )
        }*/
        // TODO: que los textos como currentCycle se alineen a la derecha
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = MaterialTheme.colors.secondaryVariant,
                    RoundedCornerShape(10.dp)
                )
                .padding(10.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Text(
                color = MaterialTheme.colors.onSecondary,
                fontWeight = FontWeight.Normal,
                text = stringResource(R.string.current_cycle)
            )
            Text(
                color = MaterialTheme.colors.onSecondary,
                fontWeight = FontWeight.Bold,
                text = String.format(
                    "%s (%d/%d)",
                    currentCycle,
                    currentCycleRepetition,
                    cycleRepetitions
                )
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = MaterialTheme.colors.secondaryVariant,
                    RoundedCornerShape(10.dp)
                )
                .padding(10.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Text(
                color = MaterialTheme.colors.onSecondary,
                fontWeight = FontWeight.Normal,
                text = stringResource(R.string.next_exercise)
            )
            Text(
                color = MaterialTheme.colors.onSecondary,
                fontWeight = FontWeight.Bold,
                text = nextExer
            )
        }
    }
}

@Preview
@Composable
fun MyPreview(MainText: String = "Abdominales", SecondaryText: String = "Descanso") {
    StronkTheme {
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
            //TitleAndSubtitle(MainText = MainText, SecondaryText = SecondaryText)
            InfoCycle(
                currentCycle = MainText,
                cycleRepetitions = 3,
                currentCycleRepetition = 2,
                nextExer = SecondaryText
            )
        }
    }
}