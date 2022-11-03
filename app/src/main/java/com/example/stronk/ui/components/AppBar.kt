package com.example.stronk.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Preview
@Composable
fun AppBar(modifier: Modifier = Modifier, screen: String = "Hola") {
    Box(modifier = modifier
        .fillMaxWidth()
        .background(color = MaterialTheme.colors.primary)
        .padding(10.dp)
    ) {
        Text(text = screen, fontWeight = FontWeight.Bold)
    }
}