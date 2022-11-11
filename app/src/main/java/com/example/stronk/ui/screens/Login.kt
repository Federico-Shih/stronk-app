package com.example.stronk.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Preview
@Composable
fun LoginScreen()
{
    Column {
        var email by remember { mutableStateOf("Email")}
        var password by remember { mutableStateOf("Password") }
        Text("Inicio de Sesión", modifier = Modifier.padding(top = 4.dp, start = 4.dp))
        Divider(color = Color.Black, thickness = 10.dp,
            modifier = Modifier.fillMaxWidth(0.15F).padding(start = 10.dp, bottom = 3.dp))
        OutlinedTextField(
            value = "",
            modifier = Modifier.padding(bottom = 3.dp).fillMaxWidth(.9f).align(Alignment.CenterHorizontally),
            textStyle = MaterialTheme.typography.subtitle1,
            onValueChange = { email = it },
            label = { Text("Inicio de Sesión") },
            keyboardOptions = KeyboardOptions( keyboardType = KeyboardType.Email)
        )
        OutlinedTextField(
            value = "",
            modifier = Modifier.padding(bottom = 3.dp).fillMaxWidth(.9f).align(Alignment.CenterHorizontally),
            textStyle = MaterialTheme.typography.subtitle1,
            onValueChange = { password = it },
            label = { Text("Contraseña") },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions( keyboardType = KeyboardType.Password)
        )
        Row {
            var checked by remember { mutableStateOf(false) }
            Checkbox(checked = checked, onCheckedChange = { checked = !checked }, modifier = Modifier.padding(4.dp))
            Text("Recordar Contraseña", modifier = Modifier.align(Alignment.CenterVertically))
        }
        Button(onClick = { /*TODO*/ }, modifier = Modifier.align(Alignment.CenterHorizontally)) {
            Text("Iniciar Sesión")
        }

    }
}
