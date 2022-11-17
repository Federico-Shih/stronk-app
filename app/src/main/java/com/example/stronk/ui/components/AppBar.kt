package com.example.stronk.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.stronk.MainScreens
import com.example.stronk.model.LoginViewModel

@Composable
fun AppBar(
    modifier: Modifier = Modifier,
    screen: String = "Hola",
    canGoBack: Boolean = false,
    goBack: () -> Unit = {},
    TopRightButtons: @Composable (
        onGetViewModel: List<() -> ViewModel?>,
        navigateTo: (String) -> Unit
    ) -> Unit,
    onGetViewModel: List<() -> ViewModel?> = listOf { null },
    navigateTo: (String) -> Unit
) {
    TopAppBar(
        modifier = modifier
            .fillMaxWidth(),
        backgroundColor = MaterialTheme.colors.primary,
        contentColor = MaterialTheme.colors.onPrimary,
    ) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Row(horizontalArrangement = Arrangement.Start) {
                if (canGoBack) {
                    IconButton(onClick = goBack) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "BackButton",
                            tint = MaterialTheme.colors.onPrimary
                        )
                    }
                }
                Text(
                    text = screen,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    modifier = Modifier.padding(10.dp),
                    color = MaterialTheme.colors.onPrimary
                )
            }

            TopRightButtons(onGetViewModel, navigateTo)
        }
    }
}

@Composable
fun BottomBar(
    modifier: Modifier = Modifier,
    onNavClick: (route: String) -> Unit = {},
    currentRoute: String,
    screenList: List<MainScreens>
) {
    BottomAppBar(
        backgroundColor = MaterialTheme.colors.primary,
        contentColor = MaterialTheme.colors.onPrimary,
        modifier = modifier
    ) {
        screenList.forEach {
            BottomNavigationItem(
                onClick = {
                    onNavClick(it.name)
                },
                icon = { Icon(imageVector = it.icon, contentDescription = it.name) },
                label = { Text(stringResource(id = it.label)) },
                alwaysShowLabel = true,
                selected = currentRoute == it.name,
            )
        }
    }
}