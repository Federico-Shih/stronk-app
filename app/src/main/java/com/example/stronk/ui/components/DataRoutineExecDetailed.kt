package com.example.stronk.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.stronk.ui.theme.StronkTheme

@Composable
fun TitleAndSubtitle(MainText: String, SecondaryText: String)
{
    Card(modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight()
        .padding(10.dp)
        .shadow(10.dp)
    ) {
        Column {
            Text(
                text = MainText, fontSize = 25.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                fontStyle = MaterialTheme.typography.h2.fontStyle,
                color = MaterialTheme.colors.primary, // quizás mejor directamente negro
                fontWeight = FontWeight(600)
            )
            // TODO: Cómo centro el divider?
            Divider(color = Color.Black, thickness = 2.dp, modifier = Modifier.fillMaxWidth(0.95F))
            Text(
                text = SecondaryText,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                fontStyle = MaterialTheme.typography.h4.fontStyle,
                color = MaterialTheme.colors.secondary, // quizás mejor directamente negro
                fontWeight = FontWeight(400)
            )
        }
    }
}

@Preview
@Composable
fun MyPreview(MainText: String = "asasdas", SecondaryText: String = "sdfsdfsf")
{
    StronkTheme {
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
            TitleAndSubtitle(MainText = MainText, SecondaryText = SecondaryText)
        }
    }
}