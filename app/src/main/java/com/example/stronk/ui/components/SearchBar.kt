package com.example.stronk.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import com.example.stronk.R

@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    value: String,
    label: String,
    onValueChanged: (String) -> Unit
) {

    val text = remember { mutableStateOf(TextFieldValue("Text"))}

    OutlinedTextField(
        modifier = modifier
            .fillMaxWidth(.9f),
        value = text.value,
        onValueChange = {
            text.value = it
            onValueChanged(it.text)
        },
        label = { Text(label) },
        textStyle = MaterialTheme.typography.subtitle1,
        trailingIcon = {
            IconButton(onClick = {  }) {
                Icon(imageVector = Icons.Filled.Search, contentDescription = stringResource(R.string.clear))
            }
        },
    )
}