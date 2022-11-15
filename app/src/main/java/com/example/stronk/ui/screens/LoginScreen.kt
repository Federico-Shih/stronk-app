package com.example.stronk.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.stronk.R
import com.example.stronk.model.ApiStatus
import com.example.stronk.state.LoginState

@Composable
fun LoginScreen(
    onSubmit: (username: String, password: String) -> Unit,
    uiState: LoginState = LoginState(),
    scaffoldState: ScaffoldState,
    dismissMessage: () -> Unit
) {
    var isError by rememberSaveable { mutableStateOf(false) }
    LaunchedEffect(uiState.isWrongPasswordOrUser) {
        if (uiState.isWrongPasswordOrUser) {
            isError = true
        }
    }

    Column(modifier = Modifier.fillMaxWidth(0.9F)) {
        var email by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
        var passwordVisible by rememberSaveable { mutableStateOf(false) }
        Spacer(modifier = Modifier.height(50.dp))
        Text(
            stringResource(id = R.string.login_label),
            modifier = Modifier.padding(top = 4.dp, start = 4.dp),
            style = MaterialTheme.typography.h4
        )
        Spacer(modifier = Modifier.height(5.dp))
        Divider(
            color = MaterialTheme.colors.primary, thickness = 10.dp,
            modifier = Modifier
                .fillMaxWidth(0.15F)
                .padding(start = 10.dp, bottom = 3.dp)
        )
        OutlinedTextField(
            value = email,
            modifier = Modifier
                .padding(bottom = 3.dp)
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally),
            textStyle = MaterialTheme.typography.subtitle1,
            onValueChange = {
                email = it
                isError = false
            },
            label = { Text(stringResource(id = R.string.username_label)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            isError = isError,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = MaterialTheme.colors.secondaryVariant,
                unfocusedBorderColor = MaterialTheme.colors.secondary,
                errorBorderColor = MaterialTheme.colors.error
            )
        )
        OutlinedTextField(
            value = password,
            modifier = Modifier
                .padding(bottom = 3.dp)
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally),
            textStyle = MaterialTheme.typography.subtitle1,
            onValueChange = {
                password = it
                isError = false
            },
            label = { Text(stringResource(id = R.string.password_label)) },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            isError = isError,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = MaterialTheme.colors.secondaryVariant,
                unfocusedBorderColor = MaterialTheme.colors.secondary,
                errorBorderColor = MaterialTheme.colors.error
            ),
            trailingIcon = {
                val image = if (passwordVisible)
                    Icons.Filled.Visibility
                else Icons.Filled.VisibilityOff

                val description = if (passwordVisible) "Hide password" else "Show password"

                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(imageVector = image, description)
                }
            }
        )
        if (isError) {
            Text(
                text = stringResource(id = R.string.error_pass_user),
                color = MaterialTheme.colors.error,
                style = MaterialTheme.typography.body2,
                modifier = Modifier.padding(start = 16.dp)
            )
        }
        Row {
            var checked by remember { mutableStateOf(false) }
            Checkbox(
                checked = checked,
                onCheckedChange = { checked = !checked },
                modifier = Modifier.padding(4.dp)
            )
            Text("Recordar Contraseña", modifier = Modifier.align(Alignment.CenterVertically))
        }
        Button(
            onClick = { onSubmit(email, password) },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(stringResource(id = R.string.login_button_label).uppercase())
        }
        if (uiState.apiState.status == ApiStatus.LOADING) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator()
            }
        }
        if (uiState.apiState.status == ApiStatus.FAILURE) {
            val actionLabel = stringResource(id = R.string.dismiss)
            LaunchedEffect(scaffoldState.snackbarHostState) {
                val result = scaffoldState.snackbarHostState.showSnackbar(
                    message = uiState.apiState.message,
                    actionLabel = actionLabel
                )
                when (result) {
                    SnackbarResult.Dismissed -> dismissMessage()
                    SnackbarResult.ActionPerformed -> dismissMessage()
                }
            }
        }
    }
}
