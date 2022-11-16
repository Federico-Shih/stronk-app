package com.example.stronk.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.RadioButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun OrderBy(optionsList: List<Pair<String, () -> Unit>>)
{
    Card(shape = RoundedCornerShape(30.dp), elevation = 10.dp, border = BorderStroke(2.dp, MaterialTheme.colors.primary)) {
        Row {
            optionsList.forEach {
                Column {
                    RadioButton(selected = false, onClick = { it.second }, modifier = Modifier.padding(bottom = 2.dp))
                    Text(
                        text = it.first,
                        style = MaterialTheme.typography.body1.merge(),
                        modifier = Modifier.align( Alignment.CenterHorizontally).padding(top = 2.dp)
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewOrderBy()
{
    Column() {
        val value = listOf(Pair("a",{}), Pair("b", {}), Pair("c", {}))
        OrderBy(optionsList = value)
    }
}

