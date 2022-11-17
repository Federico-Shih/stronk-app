package com.example.stronk.ui.components

import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ViewList
import androidx.compose.material.icons.filled.ViewModule
import androidx.compose.runtime.Composable
import com.example.stronk.network.PreferencesManager

@Composable
fun RoutineLayoutButton(
    layoutSelected: PreferencesManager.ViewPreference,
    changeLayout: (preference : PreferencesManager.ViewPreference) -> Unit
) {
    IconButton(onClick = {changeLayout(
        if (layoutSelected == PreferencesManager.ViewPreference.LIST)
            PreferencesManager.ViewPreference.GRID
        else
            PreferencesManager.ViewPreference.LIST
    ) }) {
        Icon(
            imageVector = if (layoutSelected == PreferencesManager.ViewPreference.LIST)
                Icons.Filled.ViewList
            else
                Icons.Filled.ViewModule,
            contentDescription = "Change routine layout"
        )
    }
}