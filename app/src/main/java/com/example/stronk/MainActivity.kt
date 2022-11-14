package com.example.stronk

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.example.stronk.model.LoginViewModel
import com.example.stronk.model.ViewRoutineViewModel
import com.example.stronk.ui.components.AppBar
import com.example.stronk.ui.components.BottomBar
import com.example.stronk.ui.screens.*
import com.example.stronk.ui.theme.StronkTheme
import com.google.accompanist.pager.ExperimentalPagerApi
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.stronk.model.ApiStatus
import com.example.stronk.model.MainViewModel
import com.example.stronk.model.ExecuteViewModel
import com.example.stronk.model.LoginViewModel.Companion.Factory

enum class MainScreens(
    val label: Int = R.string.empty,
    val icon: ImageVector = Icons.Filled.Article,
    val hidesBottomNav: Boolean = false,
    val topLeftButtons: @Composable (onGetViewModel: () -> ViewModel?, navigateTo: (String) -> Unit) -> Unit = { _, _ -> {}},
    val confirmationOnExit: Boolean = false,
    val hidesTopNav: Boolean = false,
) {
    AUTH(hidesBottomNav = true, hidesTopNav = true),
    EXPLORE(R.string.explore_label, Icons.Filled.Search),
    ROUTINES(
        R.string.routines_label,
        Icons.Filled.DirectionsRun
    ),
    EXECUTE(hidesBottomNav = true, confirmationOnExit = true),
    VIEW_ROUTINE(topLeftButtons = { onGetViewModel, _ ->
        val viewRoutineViewModel: ViewRoutineViewModel = onGetViewModel() as ViewRoutineViewModel
        val state = viewRoutineViewModel.uiState
        Row() {
            val context = LocalContext.current
            if (viewRoutineViewModel.uiState.loadState.status == ApiStatus.SUCCESS) {
                IconButton(onClick = { viewRoutineViewModel.favRoutine() }) {
                    Icon(
                        imageVector = if (state.faved) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                        contentDescription = "favorite",
                        tint = MaterialTheme.colors.onPrimary
                    )
                }
                IconButton(onClick = {
                    viewRoutineViewModel.shareRoutine(
                        context
                    )
                }) {
                    Icon(
                        imageVector = Icons.Filled.Share,
                        contentDescription = "share",
                        tint = MaterialTheme.colors.onPrimary
                    )
                }
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
    private val initialRoute = MainScreens.AUTH

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val backStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = backStackEntry?.destination?.route ?: initialRoute.name
            val currentScreen = MainScreens.valueOf(currentRoute.split("/")[0])
            val mainViewModel: MainViewModel = viewModel(factory = MainViewModel.Factory)
            val viewRoutineViewModel: ViewRoutineViewModel = viewModel(factory = ViewRoutineViewModel.Factory)
            var showConfirmExitDialog by remember { mutableStateOf(false) }
            StronkTheme {
                val scaffoldState: ScaffoldState = rememberScaffoldState()
                Scaffold(
                    topBar = {
                        if (!currentScreen.hidesTopNav) {
                            AppBar(
                                screen = stringResource(id = currentScreen.label),
                                canGoBack = currentScreen !in bottomBarScreens,
                                goBack = {
                                    if (!currentScreen.confirmationOnExit) {
                                        navController.popBackStack()
                                    } else {
                                        showConfirmExitDialog = true
                                    }
                                },
                                TopRightButtons = currentScreen.topLeftButtons,
                                onGetViewModel = if (currentScreen == MainScreens.VIEW_ROUTINE) {
                                    { viewRoutineViewModel }
                                } else if (currentScreen in bottomBarScreens) {
                                    { mainViewModel }
                                } else {
                                    { null }
                                },
                                navigateTo = {
                                    navController.navigate(it)
                                }
                            )
                        }
                    },
                    bottomBar = {
                        if (!currentScreen.hidesBottomNav) {
                            BottomBar(
                                onNavClick = { name ->
                                    navController.navigate(name)
                                },
                                currentRoute = currentRoute,
                                screenList = bottomBarScreens.toList()
                            )
                        }
                    },
                    scaffoldState = scaffoldState
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(bottom = it.calculateBottomPadding()),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        NavHost(
                            navController = navController,
                            startDestination = initialRoute.name
                        ) {
                            composable(route = MainScreens.EXPLORE.name) {
                                ExploreScreen(onNavigateToViewRoutine = { routineId ->
                                    navController.navigate("${MainScreens.VIEW_ROUTINE.name}/$routineId")
                                })
                            }
                            composable(route = MainScreens.ROUTINES.name) {
                                myRoutinesScreen(onNavigateToViewRoutine = { routineId ->
                                    navController.navigate("${MainScreens.VIEW_ROUTINE.name}/$routineId")
                                })
                            }
                            composable(
                                route = "${MainScreens.VIEW_ROUTINE.name}/{routineId}",
                                deepLinks = listOf(
                                    navDeepLink {
                                        uriPattern = "https://www.stronk.com/routines/{routineId}"
                                        action = Intent.ACTION_VIEW
                                    }
                                ),
                                arguments = listOf(navArgument("routineId") {
                                    type = NavType.IntType
                                })
                            ) { backStackEntry ->
                                ViewRoutineScreen(
                                    routineId = backStackEntry.arguments?.getInt("routineId") ?: 0,
                                    onNavigateToExecution = { routineId ->
                                        navController.navigate("${MainScreens.EXECUTE.name}/$routineId")
                                    },
                                    viewRoutineViewModel = viewRoutineViewModel
                                )
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
                            composable(
                                route = MainScreens.AUTH.name
                            ) {
                                val loginViewModel: LoginViewModel =
                                    viewModel(factory = LoginViewModel.Factory)
                                LoginScreen(
                                    onSubmit = { username, password ->
                                        loginViewModel.login(username, password)
                                    },
                                    dismissMessage = {
                                        loginViewModel.dismissMessage()
                                    },
                                    uiState = loginViewModel.uiState,
                                    scaffoldState = scaffoldState
                                )
                                LaunchedEffect(loginViewModel.uiState.apiState.status) {
                                    if (loginViewModel.uiState.apiState.status == ApiStatus.SUCCESS)
                                    {
                                        navController.navigate(MainScreens.ROUTINES.name)
                                    }
                                }
                            }
                        }
                    }
                    if (showConfirmExitDialog) {
                        Dialog(onDismissRequest = { showConfirmExitDialog = false }) {
                            Card(
                                backgroundColor = MaterialTheme.colors.background,
                                contentColor = MaterialTheme.colors.onBackground,
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text(
                                        text = stringResource(id = R.string.exit_confirmation),
                                        style = MaterialTheme.typography.h6,
                                        modifier = Modifier.padding(bottom = 10.dp)
                                    )
                                    Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
                                        Button(
                                            onClick = { showConfirmExitDialog = false },
                                            modifier = Modifier.padding(end = 10.dp)
                                        ) {
                                            Text(text = stringResource(id = R.string.cancel).uppercase())
                                        }
                                        Button(onClick = {
                                            showConfirmExitDialog = false
                                            navController.popBackStack()
                                        }) {
                                            Text(text = stringResource(id = R.string.exit).uppercase())
                                        }
                                    }

                                }
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
