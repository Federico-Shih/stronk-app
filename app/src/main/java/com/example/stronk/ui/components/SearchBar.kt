package com.example.stronk.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
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
    label: String,
    onValueChanged: (String) -> Unit
) {

    val text = remember { mutableStateOf(TextFieldValue(""))}

    OutlinedTextField(
        modifier = modifier
            .fillMaxWidth(.85f),
        value = text.value,
        onValueChange = {
            text.value = it
            if(text.value.text.isEmpty() || text.value.text.length > 2)
                onValueChanged(it.text)
        },
        label = { Text(label) },
        textStyle = MaterialTheme.typography.subtitle1,
        trailingIcon = {
            if(text.value.text.isNotEmpty()) {
                IconButton(onClick = { text.value = TextFieldValue(""); onValueChanged("") }) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = stringResource(R.string.clear)
                    )
                }
            }
        },
    )
}