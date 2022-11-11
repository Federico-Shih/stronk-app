package com.example.stronk

import android.content.res.Resources
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsRun
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.stronk.ui.components.AppBar
import com.example.stronk.ui.components.BottomBar
import com.example.stronk.ui.screens.ExploreScreen
import com.example.stronk.ui.screens.ViewRoutineScreen
import com.example.stronk.ui.theme.StronkTheme

enum class MainScreens {
    AUTH, EXPLORE, RUTINES, EXERCISES, EXECUTE, VIEW_RUTINE, VIEW_EXERCISE
}

enum class BottomBarScreens(val label: String, val icon: ImageVector) {
    EXPLORE("Explore", Icons.Filled.Search),
    RUTINES("Routines", Icons.Filled.DirectionsRun),
    EXERCISES("Exercises", Icons.Filled.FitnessCenter),
}

@ExperimentalAnimationApi
@ExperimentalMaterialApi
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val backStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = backStackEntry?.destination?.route ?: MainScreens.EXPLORE.name
            StronkTheme {
                Scaffold(
                    topBar = {
                        AppBar(
                            screen = currentRoute,
                            canGoBack = currentRoute !in BottomBarScreens.values().map { it.name },
                            goBack = { navController.popBackStack() }
                        )
                    },
                    bottomBar = {
                        BottomBar(
                            onNavClick = { name ->
                                navController.navigate(name)
//                            {
//                                navController.graph.startDestinationRoute?.let { screenRoute ->
//                                    popUpTo(screenRoute) {
//                                        saveState = true
//                                    }
//                                    launchSingleTop = true
//                                    restoreState = true
//                                }
//                            }
                            },
                            currentRoute = currentRoute
                        )
                    }
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(bottom = it.calculateBottomPadding())
                    ) {
                        NavHost(
                            navController = navController,
                            startDestination = BottomBarScreens.EXPLORE.name
                        ) {
                            composable(route = BottomBarScreens.EXPLORE.name) {
                                ExploreScreen(onNavigateToViewRoutine = { routineId ->
                                    navController.navigate("${MainScreens.VIEW_RUTINE.name}/$routineId")
                                })
                            }
                            composable(route = BottomBarScreens.RUTINES.name) {
                                Greeting(name = "rutines")
                            }
                            composable(route = BottomBarScreens.EXERCISES.name) {
                                Greeting(name = "hola")
                            }
                            composable(
                                route = "${MainScreens.VIEW_RUTINE.name}/{routineId}",
                                arguments = listOf(navArgument("routineId") {
                                    type = NavType.IntType
                                })
                            ) { backStackEntry ->
                                ViewRoutineScreen(backStackEntry.arguments?.getInt("routineId") ?: 0)
                            }
                        }
                    }
                }
            }
        }
    }
}

/*
Scaffold(
                    topBar = {
                        AppBar(
                            screen = backStackEntry?.destination?.route ?: MainScreens.EXPLORE.name,
                            canGoBack = navController.graph.startDestinationRoute != currentRoute,
                            goBack = { navController.popBackStack() }
                        )
                    },
                    bottomBar = {
                        BottomBar(onNavClick = {
                            name ->
                                navController.navigate(name) {
                                    navController.graph.startDestinationRoute?.let { screenRoute ->
                                        popUpTo(screenRoute) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            },
                            currentRoute=currentRoute
                        )
                    }
                ) {

                }
 */


@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}
