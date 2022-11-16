package com.example.stronk.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.RadioButton
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.stronk.R

@Composable
fun OrderBy(optionsList: List<Pair<String, () -> Unit>>) {
    var selectedIndex by remember {
        mutableStateOf(-1)
    }
    Card(
        modifier = Modifier
            .wrapContentWidth()
            .wrapContentHeight()
            .padding(10.dp)
            .padding(10.dp),
        shape = RoundedCornerShape(20.dp),
        elevation = 10.dp,
        border = BorderStroke(2.dp, MaterialTheme.colors.primary),

    ) {
        Row {
            Text(text = stringResource(R.string.OrderBy), modifier = Modifier.padding(10.dp).align(Alignment.CenterVertically))
            optionsList.forEachIndexed() { index, elem ->
                Column(modifier= Modifier
                    .wrapContentWidth()
                    .padding(horizontal = 5.dp)){
                    RadioButton(selected = index==selectedIndex, onClick = {
                        selectedIndex = index
                        elem.second()
                    })
                    Text(
                        text = elem.first,
                        style = MaterialTheme.typography.body1,
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(0.dp)
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewOrderBy() {
    Column() {
        val value = listOf(Pair("a", {}), Pair("b", {}), Pair("c", {}))
        OrderBy(optionsList = value)
    }
}

