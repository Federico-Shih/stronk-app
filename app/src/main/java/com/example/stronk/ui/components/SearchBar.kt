package com.example.stronk.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.stronk.R

@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    value: String,
    label: String,
    onValueChanged: (String) -> Unit
) {

    var textValue = value

    OutlinedTextField(
        modifier = modifier
            .fillMaxWidth(.9f),
        value = textValue,
        onValueChange = {
            textValue = it
            onValueChanged(it)
        },
        label = { Text(text = label) },
        textStyle = MaterialTheme.typography.subtitle1,
        singleLine = true,
        trailingIcon = {
            IconButton(onClick = {  }) {
                Icon(imageVector = Icons.Filled.Search, contentDescription = stringResource(R.string.clear))
            }
        },
    )
}