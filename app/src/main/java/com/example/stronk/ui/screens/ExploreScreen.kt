package com.example.stronk.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Filter
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.stronk.MainScreens
import com.example.stronk.model.ExploreViewModel
import com.example.stronk.state.ExploreState
import com.example.stronk.ui.components.LoadDependingContent
import com.example.stronk.ui.components.RoutineButtonGroup
import com.example.stronk.ui.components.SearchBar

@Composable
fun ExploreScreen(
    onNavigateToViewRoutine: (routineId: Int) -> Unit,
    exploreViewModel: ExploreViewModel = viewModel(factory = ExploreViewModel.Factory)
) {
    val state = exploreViewModel.uiState

    LoadDependingContent(loadState = state.loadState) {
        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
            Row(modifier = Modifier.fillMaxWidth()) {
                SearchBar(
                    value = "",
                    label = "Buscar Rutinas",
                    onValueChanged = { s -> exploreViewModel.searchRoutines(s) },
                    modifier = Modifier.fillMaxWidth(0.9f))
                Button(onClick = {} )
                {
                    Icon(
                        Icons.Filled.Filter,
                        contentDescription = "Filter",
                        modifier = Modifier.size(ButtonDefaults.IconSize)
                    )
                }
            }
            for (i in 0 until state.categories.size) {
                val it = state.categories[i]
                RoutineButtonGroup(
                    routineList = state.routineByCategory[i],
                    title = it.first,
                    onNavigateToViewRoutine = onNavigateToViewRoutine,
                    onGetMoreRoutines = { exploreViewModel.getMoreRoutines(it) },
                    showButton = !state.isLastOne[i]
                )
            }
        }
    }

}

@Preview
@Composable
fun PreviewExplore() {
    Box {
        val navController = rememberNavController()
        NavHost(
            navController = navController,
            startDestination = MainScreens.EXPLORE.name
        ) {
            composable(route = MainScreens.EXPLORE.name) {
                ExploreScreen(onNavigateToViewRoutine = { routineId ->
                    navController.navigate("${MainScreens.VIEW_ROUTINE.name}/$routineId")
                })
            }
        }
    }
}