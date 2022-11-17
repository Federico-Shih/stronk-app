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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.stronk.R

@Composable
fun OrderBy(title: String? = null, optionsList: List<Pair<String, () -> Unit>>) {
    var selectedIndex by remember {
        mutableStateOf(0)
    }
    Column(modifier = Modifier.padding(horizontal = 10.dp)) {
        if(title!=null)
            Text(text = title, fontWeight = FontWeight.SemiBold)
        Column {
            optionsList.forEachIndexed() { index, elem ->
                Row(verticalAlignment = Alignment.CenterVertically
                ){
                    RadioButton(selected = index==selectedIndex, onClick = {
                        selectedIndex = index
                        elem.second()
                    })
                    Text(
                        text = elem.first,
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
        val value = listOf(Pair("Dificultad", {}), Pair("Categoria", {}), Pair("c", {}))
        OrderBy(optionsList = value)
    }
}

