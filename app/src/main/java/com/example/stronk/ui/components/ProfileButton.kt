package com.example.stronk.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Logout
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.stronk.MainScreens
import com.example.stronk.R
import com.example.stronk.state.MainState

@Preview
@Composable
fun ProfileButton(
    userState: MainState = MainState(),
    navigateTo: (String) -> Unit = {}
) {
    var expanded by remember { mutableStateOf(false) }
    Box {
        Button(onClick = { expanded = !expanded }, modifier = Modifier.clip(CircleShape)) {
            AsyncImage(
                model = userState.currentUser?.avatarUrl,
                contentDescription = "Profile button",
                placeholder = painterResource(id = R.drawable.profile_placeholder),
                error = painterResource(id = R.drawable.profile_placeholder),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(color = Color.Transparent)
            )
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            DropdownMenuItem(onClick = {
                expanded = false
                navigateTo(MainScreens.AUTH.name)
            }) {
                Row {
                    Icon(Icons.Filled.Logout, contentDescription = "logout", modifier = Modifier.padding(end = 8.dp))
                    Text(text = stringResource(id = R.string.logout_button_label))
                }
            }
        }
    }
}