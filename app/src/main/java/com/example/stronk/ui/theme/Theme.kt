package com.example.stronk.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.google.accompanist.systemuicontroller.rememberSystemUiController

private val DarkColorPalette = darkColors(
    primary = LimeTwist,
    primaryVariant = DarkestForest,
    secondary = MustardGreen,
    secondaryVariant = BanksiaLeaf,
    background = TwilightZone,
    surface = DarkestSpruce,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = Color.White,
    onSurface = Color.White,
    error = LiveRed
)

private val LightColorPalette = lightColors(
    primary = OliveGreen,
    primaryVariant = DarkOliveGreen,
    secondary = MustardGreen,
    secondaryVariant = LightOliveGreen,
    background = Color.White,
    surface = LightPaleIcelandic,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = Color.Black,
    onSurface = Color.Black,
    error = LiveRed
)

@Composable
fun StronkTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {

    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }
    val systemUiController = rememberSystemUiController()
    if(darkTheme){
        systemUiController.setSystemBarsColor(
            color = LimeTwist,
        )
    }else{
        systemUiController.setSystemBarsColor(
            color = OliveGreen
        )
    }


    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}