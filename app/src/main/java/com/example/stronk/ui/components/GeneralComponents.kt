package com.example.stronk.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Feedback
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.stronk.model.ApiState
import com.example.stronk.model.ApiStatus

@Composable
fun LoadDependingContent(
    loadState: ApiState,
    content: @Composable () -> Unit,
) {
    when (loadState.status) {
        ApiStatus.LOADING -> {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator()
            }
        }
        ApiStatus.FAILURE -> {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.Error,
                    contentDescription = "Error",
                    modifier = Modifier.size(100.dp)
                )
                Text(loadState.message)
            }
        }
        else -> {
            content()
        }
    }
}

@Composable
fun NoRoutinesMessage(msg: String) {
    Row(modifier = Modifier.padding(start = 4.dp)) {
        Icon(
            imageVector = Icons.Filled.Feedback,
            contentDescription = "no routines",
            modifier = Modifier
                .size(24.dp)
                .alignByBaseline()
        )
        Text(
            text = msg,
            modifier = Modifier
                .alignByBaseline()
                .padding(start = 10.dp)
        )
    }
}