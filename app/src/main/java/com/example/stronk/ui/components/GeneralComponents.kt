package com.example.stronk.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Feedback
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.stronk.model.ApiState
import com.example.stronk.model.ApiStatus
import com.example.stronk.state.Category
import com.example.stronk.R
import com.example.stronk.model.errorMessage

@Composable
fun LoadDependingContent(
    loadState: ApiState,
    content: @Composable () -> Unit,
) {
    when (loadState.status) {
        ApiStatus.LOADING -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator()
            }
        }
        ApiStatus.FAILURE -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.Error,
                    contentDescription = "Error",
                    modifier = Modifier.size(100.dp)
                )
                Text(stringResource(id = loadState.errorMessage))
            }
        }
        else -> {
            content()
        }
    }
}

@Composable
fun NoRoutinesMessage(msg: String) {
    Row(modifier = Modifier.padding(start = 10.dp)) {
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

@Composable
fun getResourceIdRoutineImage(category: Category, routineId: Int): Int {
    val resourceID = when (category.name) {
        "Espalda" -> if (routineId % 2 == 0) R.drawable.back01 else R.drawable.back02
        "Full Body" -> if (routineId % 2 == 0) R.drawable.fullbody01 else R.drawable.fullbody02
        "Pecho" -> if (routineId % 2 == 0) R.drawable.chest01 else R.drawable.chest02
        "Piernas" -> if (routineId % 2 == 0) R.drawable.leg01 else R.drawable.leg02
        "Brazos" -> if (routineId % 2 == 0) R.drawable.arms01 else R.drawable.arms02
        else -> R.drawable.abdos
    }
    return resourceID
}