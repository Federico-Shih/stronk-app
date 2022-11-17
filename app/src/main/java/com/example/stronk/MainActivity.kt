package com.example.stronk

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
import com.example.stronk.ui.components.AppBar
import com.example.stronk.ui.components.BottomBar
import com.example.stronk.ui.screens.*
import com.example.stronk.ui.theme.StronkTheme
import com.google.accompanist.pager.ExperimentalPagerApi
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.stronk.model.*
import com.example.stronk.ui.components.ProfileButton

@Composable
fun MainNavbarButtons(
    onGetViewModel: () -> ViewModel?,
    navigateTo: (String) -> Unit,
    MoreButtons: @Composable () -> Unit = {}
) {
    val userViewModel: MainViewModel = onGetViewModel() as MainViewModel
    Row(verticalAlignment = Alignment.CenterVertically) {
        MoreButtons()
        ProfileButton(
            userViewModel.uiState
        ) { route ->
            userViewModel.logout()
            navigateTo(route)
        }
    }
}

enum class MainScreens(
    val label: Int = R.string.empty,
    val icon: ImageVector = Icons.Filled.Article,
    val hidesBottomNav: Boolean = false,
    val topLeftButtons: @Composable (onGetViewModel: () -> ViewModel?, navigateTo: (String) -> Unit) -> Unit = { _, _ -> },
    val confirmationOnExit: Boolean = false,
    val hidesTopNav: Boolean = false,
) {
    AUTH(hidesBottomNav = true, hidesTopNav = true), REGISTER(
        hidesBottomNav = true,
        hidesTopNav = true
    ),
    VERIFY(hidesBottomNav = true, hidesTopNav = true), EXPLORE(R.string.explore_label,
        Icons.Filled.Search,
        topLeftButtons = { onGetViewModel, navigateTo ->
            MainNavbarButtons(onGetViewModel, navigateTo, MoreButtons = {
                IconButton(onClick = { navigateTo(QR_SCANNER.name) }) {
                    Icon(
                        Icons.Filled.QrCode,
                        contentDescription = "Scan QR",
                        tint = MaterialTheme.colors.onPrimary,
                        modifier = Modifier.size(24.dp)
                    )
                }
            })
        }),
    ROUTINES(R.string.routines_label,
        Icons.Filled.DirectionsRun,
        topLeftButtons = { onGetViewModel, navigateTo ->
            MainNavbarButtons(
                onGetViewModel, navigateTo
            )
        }),
    EXECUTE(
        hidesBottomNav = true, confirmationOnExit = true
    ),
    VIEW_ROUTINE(topLeftButtons = { onGetViewModel, _ ->
        val viewRoutineViewModel: ViewRoutineViewModel = onGetViewModel() as ViewRoutineViewModel
        val state = viewRoutineViewModel.uiState
        Row() {
            val context = LocalContext.current
            var shareExpanded by remember { mutableStateOf(false) }
            if (viewRoutineViewModel.uiState.loadState.status == ApiStatus.SUCCESS) {
                IconButton(onClick = { viewRoutineViewModel.toggleFavourite() }) {
                    Icon(
                        imageVector = if (state.faved) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                        contentDescription = "favorite",
                        tint = MaterialTheme.colors.onPrimary
                    )
                }
                IconButton(onClick = {
                    shareExpanded = !shareExpanded
                }) {
                    Icon(
                        imageVector = Icons.Filled.Share,
                        contentDescription = "share",
                        tint = MaterialTheme.colors.onPrimary
                    )
                }
                DropdownMenu(expanded = shareExpanded,
                    onDismissRequest = { shareExpanded = false }) {
                    DropdownMenuItem(onClick = {
                        shareExpanded = false
                        viewRoutineViewModel.shareRoutineLink(context)
                    }) {
                        Row {
                            Icon(
                                Icons.Filled.Link,
                                contentDescription = "share link",
                                modifier = Modifier.padding(end = 8.dp)
                            )
                            Text(text = stringResource(id = R.string.share_link))
                        }
                    }
                    DropdownMenuItem(onClick = {
                        shareExpanded = false
                        viewRoutineViewModel.showRoutineQr()
                    }) {
                        Row {
                            Icon(
                                Icons.Filled.QrCode,
                                contentDescription = "share qr",
                                modifier = androidx.compose.ui.Modifier.padding(
                                    end = 8.dp
                                )
                            )
                            Text(text = stringResource(id = R.string.show_qr_code))
                        }
                    }
                }
            }

        }
    }),
    QR_SCANNER(hidesBottomNav = true, label = R.string.qr_scanner_label),
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
            val viewRoutineViewModel: ViewRoutineViewModel =
                viewModel(factory = ViewRoutineViewModel.Factory)
            val executeViewModel: ExecuteViewModel = viewModel(factory = ExecuteViewModel.Factory)
            var showConfirmExitDialog by remember { mutableStateOf(false) }
            val loginViewModel: LoginViewModel =
                viewModel(factory = LoginViewModel.Factory)
            val registerViewModel: RegisterViewModel =
                viewModel(factory = RegisterViewModel.Factory)

            StronkTheme {
                val scaffoldState: ScaffoldState = rememberScaffoldState()
                Scaffold(topBar = {
                    if (!currentScreen.hidesTopNav) {
                        AppBar(screen = stringResource(id = currentScreen.label),
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
                            navigateTo = { dest ->
                                navController.navigate(dest)
                            })
                    }
                }, bottomBar = {
                    if (!currentScreen.hidesBottomNav) {
                        BottomBar(
                            onNavClick = { name ->
                                if (name != currentScreen.name) {
                                    navController.navigate(name) {
                                        popUpTo(name) {
                                            inclusive = true
                                        }
                                    }
                                }
                            },
                            currentRoute = currentScreen.name,
                            screenList = bottomBarScreens.toList()
                        )
                    }
                }, scaffoldState = scaffoldState
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(bottom = it.calculateBottomPadding()),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        NavHost(
                            navController = navController, startDestination = initialRoute.name
                        ) {
                            composable(route = MainScreens.EXPLORE.name) {
                                ExploreScreen(onNavigateToViewRoutine = { routineId ->
                                    navController.navigate("${MainScreens.VIEW_ROUTINE.name}/$routineId")
                                })
                            }
                            composable(route = MainScreens.ROUTINES.name) {
                                MyRoutinesScreen(onNavigateToViewRoutine = { routineId ->
                                    navController.navigate("${MainScreens.VIEW_ROUTINE.name}/$routineId")
                                })
                            }
                            composable(
                                route = "${MainScreens.VIEW_ROUTINE.name}/{routineId}",
                                deepLinks = listOf(navDeepLink {
                                    uriPattern = "https://www.stronk.com/routines/{routineId}"
                                    action = Intent.ACTION_VIEW
                                }),
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
                                route = MainScreens.VERIFY.name,
                            ) {
                                VerifyScreen(
                                    onVerified = { navController.navigate(MainScreens.ROUTINES.name) },
                                    email = "",
                                    scaffoldState = scaffoldState,
                                    username = loginViewModel.uiState.username,
                                    password = loginViewModel.uiState.password
                                )
                            }
                            composable(
                                route = "${MainScreens.VERIFY.name}/{email}",
                                arguments = listOf(navArgument("email") {
                                    type = NavType.StringType
                                })
                            ) { backStackEntry ->
                                VerifyScreen(
                                    onVerified = { navController.navigate(MainScreens.ROUTINES.name) },
                                    email = backStackEntry.arguments?.getString("email") ?: "",
                                    scaffoldState = scaffoldState,
                                    username = registerViewModel.uiState.username,
                                    password = registerViewModel.uiState.password,
                                )
                            }
                            composable(
                                route = "${MainScreens.EXECUTE.name}/{routineId}",
                                arguments = listOf(navArgument("routineId") {
                                    type = NavType.IntType
                                })
                            ) { backStackEntry ->
                                ExecuteRoutineScreen(
                                    routineId = backStackEntry.arguments?.getInt("routineId") ?: 0,
                                    onGoBack = {
                                        navController.popBackStack()
                                    },
                                )
                            }
                            composable(
                                route = MainScreens.AUTH.name
                            ) {
                                LoginScreen(
                                    onSubmit = { username, password ->
                                        loginViewModel.login(username, password)
                                    },
                                    dismissMessage = {
                                        loginViewModel.dismissMessage()
                                    },
                                    uiState = loginViewModel.uiState,
                                    scaffoldState = scaffoldState,
                                    navigateToVerify = { navController.navigate(MainScreens.VERIFY.name) },
                                    navigateToRegister = { navController.navigate(MainScreens.REGISTER.name) }
                                )
                                LaunchedEffect(loginViewModel.uiState.apiState.status) {
                                    if (loginViewModel.uiState.apiState.status == ApiStatus.SUCCESS) {
                                        mainViewModel.fetchCurrentUser()
                                        navController.navigate(MainScreens.EXPLORE.name)
                                        loginViewModel.clearUiState()
                                    }
                                }
                            }
                            composable(
                                route = MainScreens.QR_SCANNER.name
                            ) {
                                ScanQrScreen(onNavigateToViewRoutine = { routineId ->
                                    navController.navigate("${MainScreens.VIEW_ROUTINE.name}/$routineId") {
                                        popUpTo(MainScreens.EXPLORE.name) {
                                            inclusive = true
                                        }
                                    }
                                })
                            }
                            composable(
                                route = MainScreens.REGISTER.name
                            ) {
                                RegisterScreen(
                                    onSubmit = { email ->
                                        navController.navigate("${MainScreens.VERIFY.name}/$email")
                                    },
                                    scaffoldState = scaffoldState,
                                    navigateToLogin = { navController.navigate(MainScreens.AUTH.name) },
                                    viewModel = registerViewModel
                                )
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
                                        Row(
                                            horizontalArrangement = Arrangement.End,
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
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
