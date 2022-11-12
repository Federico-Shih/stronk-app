package com.example.stronk

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.stronk.ui.components.AppBar
import com.example.stronk.ui.components.BottomBar
import com.example.stronk.ui.screens.ExecuteRoutineScreen
import com.example.stronk.ui.screens.ExploreScreen
import com.example.stronk.ui.screens.ViewRoutineScreen
import com.example.stronk.ui.theme.StronkTheme
import com.google.accompanist.pager.ExperimentalPagerApi

enum class MainScreens(
    val label: Int = R.string.empty,
    val icon: ImageVector = Icons.Filled.Article,
    val hidesBottomNav: Boolean = false,
    val topLeftButtons: @Composable () -> Unit = {}
) {
    AUTH,
    EXPLORE(R.string.explore_label, Icons.Filled.Search),
    ROUTINES(
        R.string.routines_label,
        Icons.Filled.DirectionsRun
    ),
    EXECUTE(hidesBottomNav = true),
    VIEW_ROUTINE(topLeftButtons = {
        val viewRoutineViewModel: com.example.stronk.model.ViewRoutineViewModel =
            androidx.lifecycle.viewmodel.compose.viewModel()
        val state = viewRoutineViewModel.uiState
        Row() {
            Text(text = state.routine.name, style = MaterialTheme.typography.h5)
            IconButton(onClick = { viewRoutineViewModel.favRoutine() }) {
                Icon(
                    imageVector = if(state.faved) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                    contentDescription = "favorite",
                )
            }
            IconButton(onClick = { viewRoutineViewModel.shareRoutine() }) {
                Icon(
                    imageVector = Icons.Filled.Share,
                    contentDescription = "share",
                )
            }
        }
    })
}

@ExperimentalAnimationApi
@ExperimentalMaterialApi
@ExperimentalPagerApi
@ExperimentalFoundationApi
class MainActivity : ComponentActivity() {

    private val bottomBarScreens = listOf(MainScreens.EXPLORE, MainScreens.ROUTINES)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val backStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = backStackEntry?.destination?.route ?: MainScreens.EXPLORE.name
            val currentScreen = MainScreens.valueOf(currentRoute.split("/")[0])
            StronkTheme {
                Scaffold(
                    topBar = {
                        AppBar(
                            screen = stringResource(id = currentScreen.label),
                            canGoBack = currentScreen !in bottomBarScreens,
                            goBack = { navController.popBackStack() },
                            TopRightButtons = currentScreen.topLeftButtons
                        )
                    },
                    bottomBar = {
                        if (!currentScreen.hidesBottomNav) {
                            BottomBar(
                                onNavClick = { name ->
                                    navController.navigate(name)
                                },
                                currentRoute = currentRoute,
                                screenList = bottomBarScreens
                            )
                        }
                    }
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(bottom = it.calculateBottomPadding())
                    ) {
                        NavHost(
                            navController = navController,
                            startDestination = MainScreens.EXPLORE.name
                        ) {
                            composable(route = MainScreens.EXPLORE.name) {
                                ExploreScreen(onNavigateToViewRoutine = { routineId ->
                                    navController.navigate("${MainScreens.VIEW_ROUTINE.name}/$routineId")
                                })
                            }
                            composable(route = MainScreens.ROUTINES.name) {
                                Greeting(name = "rutines")
                            }
                            composable(
                                route = "${MainScreens.VIEW_ROUTINE.name}/{routineId}",
                                arguments = listOf(navArgument("routineId") {
                                    type = NavType.IntType
                                })
                            ) { backStackEntry ->
                                ViewRoutineScreen(
                                    routineId = backStackEntry.arguments?.getInt("routineId") ?: 0,
                                    onNavigateToExecution = { routineId ->
                                        navController.navigate("${MainScreens.EXECUTE.name}/$routineId")
                                    })
                            }
                            composable(
                                route = "${MainScreens.EXECUTE.name}/{routineId}",
                                arguments = listOf(navArgument("routineId") {
                                    type = NavType.IntType
                                })
                            ) { backStackEntry ->
                                ExecuteRoutineScreen(
                                    backStackEntry.arguments?.getInt("routineId") ?: 0
                                )
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
