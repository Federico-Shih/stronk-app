package com.example.stronk.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.stronk.BottomBarScreens

@Preview
@Composable
fun AppBar(modifier: Modifier = Modifier, screen: String = "Hola", canGoBack: Boolean = false, goBack: () -> Unit = {}) {
    TopAppBar(modifier = modifier
        .fillMaxWidth()
        .background(color = MaterialTheme.colors.primary)
    ) {
        if (canGoBack)
        {
            IconButton(onClick = goBack) {
                Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "BackButton", tint = MaterialTheme.colors.secondary)
            }
        }
        Text(
            text = screen,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colors.secondary,
            fontSize = 20.sp,
            modifier = Modifier.padding(10.dp))
    }
}

@Preview
@Composable
fun BottomBar(modifier: Modifier = Modifier, onNavClick: (route: String) -> Unit = {}, currentRoute: String? = BottomBarScreens.EXERCISES.name) {
    BottomAppBar(modifier = modifier.background(color = MaterialTheme.colors.primary)) {
        BottomBarScreens.values().forEach {
            BottomNavigationItem(
                onClick = {
                    onNavClick(it.name)
                },
                icon= { Icon(imageVector = it.icon, contentDescription = it.name)},
                label={ Text(text = it.label) },
                alwaysShowLabel = true,
                selected = currentRoute == it.name,
            )
        }
    }
}