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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.stronk.R
import com.example.stronk.model.ApiStatus
import com.example.stronk.model.RegisterViewModel
import com.example.stronk.network.ApiErrorCode
import com.example.stronk.state.InputError
import com.example.stronk.state.LoginState

@Composable
fun RegisterScreen(
    onSubmit: (email: String) -> Unit,
    scaffoldState: ScaffoldState,
    viewModel: RegisterViewModel = viewModel(factory = RegisterViewModel.Factory),
    navigateToLogin: () -> Unit,
) {
    RegisterForm(onSubmit = { username: String, email: String, firstname: String, lastname: String, password: String, confirmPassword: String ->
        viewModel.register(
            username,
            email,
            firstname,
            lastname,
            password,
            confirmPassword,
            onSubmit
        )
    }, navigateToLogin = navigateToLogin, viewModel = viewModel)

    val context = LocalContext.current
    val uiState = viewModel.uiState
    if (uiState.apiState.status == ApiStatus.FAILURE) {
        val actionLabel = stringResource(id = R.string.dismiss)
        LaunchedEffect(scaffoldState.snackbarHostState) {
            val result = scaffoldState.snackbarHostState.showSnackbar(
                message =
                when (uiState.apiState.code) {
                    ApiErrorCode.DATA_CONSTRAINT.code -> context.resources.getString(R.string.email_username_taken_err)
                    ApiErrorCode.CONNECTION_ERROR.code -> context.resources.getString(R.string.connection_error)
                    else -> context.resources.getString(R.string.unknown_error_message)
                },
                actionLabel = actionLabel
            )
            when (result) {
                SnackbarResult.Dismissed -> viewModel.dismissMessage()
                SnackbarResult.ActionPerformed -> viewModel.dismissMessage()
            }
        }
    }
}

@Composable
fun RegisterForm(
    onSubmit: (
        username: String,
        email: String,
        firstname: String,
        lastname: String,
        password: String,
        confirmPassword: String,
    ) -> Unit = { _, _, _, _, _, _ -> },
    viewModel: RegisterViewModel,
    navigateToLogin: () -> Unit
) {
    val uiState = viewModel.uiState
    val passError = uiState.passwordInputState
    val usernameErr = uiState.usernameInputState
    val firstnameErr = uiState.firstnameInputState
    val lastnameErr = uiState.lastnameInputState
    val emailErr = uiState.emailInputState

    println(passError)
    Column(modifier = Modifier.padding(20.dp)) {
        var username by remember { mutableStateOf("") }
        var firstname by remember { mutableStateOf("") }
        var lastname by remember { mutableStateOf("") }
        var email by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
        var confirmPassword by remember { mutableStateOf("") }

        var passwordVisible by rememberSaveable { mutableStateOf(false) }
        Spacer(modifier = Modifier.height(50.dp))
        Text(
            stringResource(id = R.string.register_label),
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
            value = username,
            modifier = Modifier
                .padding(bottom = 3.dp)
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally),
            textStyle = MaterialTheme.typography.subtitle1,
            onValueChange = {
                username = it
                viewModel.clearUsername()
            },
            label = { Text(stringResource(id = R.string.username_label)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = MaterialTheme.colors.secondaryVariant,
                unfocusedBorderColor = MaterialTheme.colors.secondary,
                errorBorderColor = MaterialTheme.colors.error
            ),
            isError = usernameErr.hasError
        )
        if (usernameErr.hasError) {
            Text(
                text = when (usernameErr.errorCode) {
                    InputError.EMPTY_ERROR.code -> stringResource(id = R.string.empty_input)
                    ApiErrorCode.DATA_CONSTRAINT.code -> stringResource(id = R.string.email_username_taken_err)
                    else -> ""
                },
                color = MaterialTheme.colors.error,
                style = MaterialTheme.typography.body2,
                modifier = Modifier.padding(start = 16.dp)
            )
        }
        OutlinedTextField(
            value = firstname,
            modifier = Modifier
                .padding(bottom = 3.dp)
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally),
            textStyle = MaterialTheme.typography.subtitle1,
            onValueChange = {
                firstname = it
                viewModel.clearFirstname()
            },
            label = { Text(stringResource(id = R.string.firstname_label)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = MaterialTheme.colors.secondaryVariant,
                unfocusedBorderColor = MaterialTheme.colors.secondary,
                errorBorderColor = MaterialTheme.colors.error
            ),
            isError = firstnameErr.hasError
        )
        if (firstnameErr.hasError) {
            Text(
                text = when (firstnameErr.errorCode) {
                    InputError.EMPTY_ERROR.code -> stringResource(id = R.string.empty_input)
                    else -> ""
                },
                color = MaterialTheme.colors.error,
                style = MaterialTheme.typography.body2,
                modifier = Modifier.padding(start = 16.dp)
            )
        }
        OutlinedTextField(
            value = lastname,
            modifier = Modifier
                .padding(bottom = 3.dp)
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally),
            textStyle = MaterialTheme.typography.subtitle1,
            onValueChange = {
                lastname = it
                viewModel.clearLastname()
            },
            label = { Text(stringResource(id = R.string.lastname_label)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = MaterialTheme.colors.secondaryVariant,
                unfocusedBorderColor = MaterialTheme.colors.secondary,
                errorBorderColor = MaterialTheme.colors.error
            ),
            isError = lastnameErr.hasError
        )
        if (lastnameErr.hasError) {
            Text(
                text = when (lastnameErr.errorCode) {
                    InputError.EMPTY_ERROR.code -> stringResource(id = R.string.empty_input)
                    else -> ""
                },
                color = MaterialTheme.colors.error,
                style = MaterialTheme.typography.body2,
                modifier = Modifier.padding(start = 16.dp)
            )
        }
        OutlinedTextField(
            value = email,
            modifier = Modifier
                .padding(bottom = 3.dp)
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally),
            textStyle = MaterialTheme.typography.subtitle1,
            onValueChange = {
                email = it
                viewModel.clearEmail()
            },
            label = { Text(stringResource(id = R.string.email_label)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = MaterialTheme.colors.secondaryVariant,
                unfocusedBorderColor = MaterialTheme.colors.secondary,
                errorBorderColor = MaterialTheme.colors.error
            ),
            isError = emailErr.hasError
        )
        if (emailErr.hasError) {
            Text(
                text = when (emailErr.errorCode) {
                    InputError.EMPTY_ERROR.code -> stringResource(id = R.string.empty_input)
                    ApiErrorCode.DATA_CONSTRAINT.code -> stringResource(id = R.string.email_username_taken_err)
                    else -> ""
                },
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
                viewModel.clearPassword()
            },
            label = { Text(stringResource(id = R.string.password_label)) },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
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
            },
            isError = uiState.passwordInputState.hasError
        )
        OutlinedTextField(
            value = confirmPassword,
            modifier = Modifier
                .padding(bottom = 3.dp)
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally),
            textStyle = MaterialTheme.typography.subtitle1,
            onValueChange = {
                confirmPassword = it
                viewModel.clearPassword()
            },
            label = { Text(stringResource(id = R.string.confirmpass_label)) },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
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
            },
            isError = passError.hasError
        )
        if (passError.hasError) {
            Text(
                text = when (passError.errorCode) {
                    InputError.PASSNOTMATCH.code -> stringResource(id = R.string.pass_no_match)
                    InputError.EMPTY_ERROR.code -> stringResource(id = R.string.empty_input)
                    else -> {
                        ""
                    }
                },
                color = MaterialTheme.colors.error,
                style = MaterialTheme.typography.body2,
                modifier = Modifier.padding(start = 16.dp)
            )
        }
        TextButton(onClick = { navigateToLogin() }) {
            Text(
                text = stringResource(id = R.string.has_user_cta),
                color = MaterialTheme.colors.secondary
            )
        }
        Button(
            onClick = { onSubmit(username, email, firstname, lastname, password, confirmPassword) },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(stringResource(id = R.string.register_button_label).uppercase())
        }
        if (uiState.apiState.status == ApiStatus.LOADING) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator()
            }
        }
    }
}