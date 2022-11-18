package com.example.stronk.ui.screens

import androidx.compose.animation.animateContentSize
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.stronk.R
import com.example.stronk.model.ApiStatus
import com.example.stronk.network.ApiErrorCode
import com.example.stronk.state.InputError
import com.example.stronk.state.InputState
import com.example.stronk.state.LoginState

@Composable
fun LoginScreen(
    onSubmit: (username: String, password: String) -> Unit,
    uiState: LoginState,
    scaffoldState: ScaffoldState,
    dismissMessage: () -> Unit,
    navigateToRegister: () -> Unit,
    navigateToVerify: () -> Unit,
    onInitialLoad: suspend () -> Unit,
) {
    var isError by remember { mutableStateOf(InputState()) }
    var usernameError by remember { mutableStateOf(InputState()) }
    val once by remember { mutableStateOf(1) }
    LaunchedEffect(uiState.isWrongPasswordOrUser) {
        if (uiState.isWrongPasswordOrUser) {
            isError = isError.copy(hasError = false)
        }
    }
    LaunchedEffect(once) {
        onInitialLoad()
    }
    Column(modifier = Modifier
        .padding(20.dp)
        .fillMaxHeight()
        .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        var email by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
        var passwordVisible by rememberSaveable { mutableStateOf(false) }
        Column(modifier = Modifier
            .fillMaxHeight()
            .weight(1F)) {
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
                    usernameError = usernameError.copy(hasError = false)
                },
                label = { Text(stringResource(id = R.string.username_label)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                isError = usernameError.hasError,
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = MaterialTheme.colors.secondaryVariant,
                    unfocusedBorderColor = MaterialTheme.colors.secondary,
                    errorBorderColor = MaterialTheme.colors.error
                )
            )
            if (usernameError.hasError) {
                Text(
                    text = stringResource(id = R.string.empty_input),
                    color = MaterialTheme.colors.error,
                    style = MaterialTheme.typography.body2,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }
            OutlinedTextField(
                value = password,
                modifier = Modifier
                    .padding(bottom = 3.dp)
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally),
                textStyle = MaterialTheme.typography.subtitle1,
                onValueChange = {
                    password = it
                    isError = isError.copy(hasError = false)
                },
                label = { Text(stringResource(id = R.string.password_label)) },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                isError = isError.hasError,
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
            if (isError.hasError) {
                Text(
                    text = stringResource(id = when (isError.errorCode) {
                        InputError.EMPTY_ERROR.code -> R.string.empty_input
                        else -> R.string.error_pass_user
                    }),
                    color = MaterialTheme.colors.error,
                    style = MaterialTheme.typography.body2,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }
            TextButton(onClick = {
                navigateToRegister()
            }) {
                Text(
                    text = stringResource(id = R.string.no_user_cta),
                    color = MaterialTheme.colors.secondary
                )
            }
        }

        Button(
            onClick = {
                if (email.isEmpty()) {
                    usernameError = usernameError.copy(hasError = true, errorCode = InputError.EMPTY_ERROR.code)
                }
                if (password.isEmpty()) {
                    isError = isError.copy(hasError = true, errorCode = InputError.EMPTY_ERROR.code)
                }
                if (email.isNotEmpty() && password.isNotEmpty()) {
                    onSubmit(email, password)
                }
            },
            modifier = Modifier
                .fillMaxWidth(0.9F)
                .height(60.dp)
                .animateContentSize()
        ) {
            if (uiState.apiState.status == ApiStatus.LOADING) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator(color = Color.White)
                }
            } else {
                Text(stringResource(id = R.string.login_button_label), fontSize = 20.sp)
            }
        }

        val context = LocalContext.current

        LaunchedEffect(uiState.apiState) {
            if (uiState.apiState.status == ApiStatus.FAILURE) {
                isError = isError.copy(hasError = true, errorCode = uiState.apiState.code)
            }
        }
        if (uiState.apiState.status == ApiStatus.FAILURE && uiState.apiState.code != ApiErrorCode.EMAIL_NOT_VERIFIED.code) {
            val actionLabel = stringResource(id = R.string.dismiss)
            LaunchedEffect(scaffoldState.snackbarHostState) {
                val result = scaffoldState.snackbarHostState.showSnackbar(
                    message =
                    when (uiState.apiState.code) {
                        ApiErrorCode.INVALID_USER_PASS.code -> context.resources.getString(R.string.error_pass_user)
                        ApiErrorCode.CONNECTION_ERROR.code -> context.resources.getString(R.string.connection_error)
                        else -> context.resources.getString(R.string.unknown_error_message)
                    },
                    actionLabel = actionLabel
                )
                when (result) {
                    SnackbarResult.Dismissed -> dismissMessage()
                    SnackbarResult.ActionPerformed -> dismissMessage()
                }
            }
        } else if (uiState.apiState.code == ApiErrorCode.EMAIL_NOT_VERIFIED.code) {
            LaunchedEffect(true) {
                navigateToVerify()
            }
        }
    }
}
