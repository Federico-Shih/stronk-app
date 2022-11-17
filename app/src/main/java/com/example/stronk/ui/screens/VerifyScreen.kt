package com.example.stronk.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.stronk.R
import com.example.stronk.model.ApiStatus
import com.example.stronk.model.RegisterViewModel
import com.example.stronk.network.ApiErrorCode

@Composable
fun VerifyScreen(
    onVerified: () -> Unit,
    modifier: Modifier = Modifier,
    email: String = "",
    registerViewModel: RegisterViewModel = viewModel(factory = RegisterViewModel.Factory),
    scaffoldState: ScaffoldState,
    username: String = "",
    password: String = "",
) {
    val uiState = registerViewModel.uiState
    var verifyToken by rememberSaveable { mutableStateOf("") }
    var emailInput by rememberSaveable { mutableStateOf("") }
    Column(modifier = modifier.padding(20.dp)) {
        Spacer(modifier = Modifier.height(50.dp))
        Text(
            stringResource(id = R.string.verify_email_label),
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
        if (email == "") {
            OutlinedTextField(
                value = emailInput,
                modifier = Modifier
                    .padding(bottom = 3.dp)
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally),
                textStyle = MaterialTheme.typography.subtitle1,
                onValueChange = {
                    emailInput = it
                },
                label = { Text(stringResource(id = R.string.email_label)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = MaterialTheme.colors.secondaryVariant,
                    unfocusedBorderColor = MaterialTheme.colors.secondary,
                    errorBorderColor = MaterialTheme.colors.error
                ),
            )
        }
        OutlinedTextField(
            value = verifyToken,
            modifier = Modifier
                .padding(bottom = 3.dp)
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally),
            textStyle = MaterialTheme.typography.subtitle1,
            onValueChange = {
                if (it.length <= 6) {
                    verifyToken = it.uppercase()
                }
            },
            label = { Text(stringResource(id = R.string.verify_token_label)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Ascii),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = MaterialTheme.colors.secondaryVariant,
                unfocusedBorderColor = MaterialTheme.colors.secondary,
                errorBorderColor = MaterialTheme.colors.error
            ),
        )
        if (email != "") {
            Text(text = stringResource(id = R.string.verify_note_label))
        }
        Button(
            onClick = {
                registerViewModel.verifyCode(
                    if (email != "") email else emailInput,
                    code = verifyToken,
                    username = username,
                    password = password,
                    onVerified
                )
            },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(stringResource(id = R.string.verify_token_label).uppercase())
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
    val context = LocalContext.current
    if (uiState.apiState.status == ApiStatus.FAILURE) {
        val actionLabel = stringResource(id = R.string.dismiss)
        LaunchedEffect(scaffoldState.snackbarHostState) {
            val result = scaffoldState.snackbarHostState.showSnackbar(
                message =
                when (uiState.apiState.code) {
                    ApiErrorCode.CONNECTION_ERROR.code -> context.resources.getString(R.string.connection_error)
                    ApiErrorCode.EMAIL_NOT_VERIFIED.code -> context.resources.getString(R.string.unable_to_verify)
                    else -> context.resources.getString(R.string.unknown_error_message)
                },
                actionLabel = actionLabel
            )
            when (result) {
                SnackbarResult.Dismissed -> registerViewModel.dismissMessage()
                SnackbarResult.ActionPerformed -> registerViewModel.dismissMessage()
            }
        }
    }
}

