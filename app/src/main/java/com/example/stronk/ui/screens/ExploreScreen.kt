package com.example.stronk.ui.screens

import androidx.compose.material.Button
import androidx.compose.material.Tab
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController

@Composable
fun ExploreScreen(
    onNavigateToViewRoutine: (routineId: Int) -> Unit
) {
    val navController = rememberNavController()
    Button(onClick = { onNavigateToViewRoutine(1) }) {
        Text(text = "My Routine")
    }
}