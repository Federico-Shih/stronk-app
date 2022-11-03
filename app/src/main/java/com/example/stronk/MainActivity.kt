package com.example.stronk

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.stronk.ui.components.AppBar
import com.example.stronk.ui.theme.StronkTheme

enum class MainScreens {
    AUTH, EXPLORE, RUTINES, EXERCISES, EXECUTE, VIEW_RUTINE, VIEW_EXERCISE
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {        
            val navController = rememberNavController()
            val backStackEntry by navController.currentBackStackEntryAsState()

            StronkTheme {
                Scaffold(topBar = {
                    AppBar(screen = backStackEntry?.destination?.route ?: MainScreens.EXPLORE.name)
                }) {
                    NavHost(navController = navController, startDestination = MainScreens.EXPLORE.name) {
                        composable(route = MainScreens.EXPLORE.name) {
                            Greeting(name = "hola")
                        }
                        composable(route = MainScreens.RUTINES.name) {
                            Greeting(name = "rutines")
                        }
                        composable(route = MainScreens.EXERCISES.name) {
                            Greeting(name = "hola")
                        }
                        composable(route = MainScreens.EXECUTE.name) {
                            Greeting(name = "hola")
                        }
                        composable(route = MainScreens.VIEW_EXERCISE.name) {
                            Greeting(name = "hola")
                        }
                        composable(route = MainScreens.VIEW_RUTINE.name) {
                            Greeting(name = "hola")
                        }
                    }
                }
            }
        }
    }
}



@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    StronkTheme {
        Greeting("Android")
    }
}