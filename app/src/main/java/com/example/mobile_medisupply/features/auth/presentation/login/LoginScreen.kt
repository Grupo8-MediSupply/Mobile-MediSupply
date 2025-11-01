package com.example.mobile_medisupply.features.auth.presentation.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.mobile_medisupply.R
import com.example.mobile_medisupply.ui.theme.MobileMediSupplyTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
        onLoginSuccess: () -> Unit,
        onForgotPasswordClick: () -> Unit,
        onRegisterClick: () -> Unit,
        modifier: Modifier = Modifier,
        viewModel: LoginViewModel = hiltViewModel()
) {
    val focusManager = LocalFocusManager.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }

    LaunchedEffect(uiState.isLoggedIn) {
        if (uiState.isLoggedIn) {
            onLoginSuccess()
            viewModel.onLoginNavigated()
        }
    }

    LoginScreenContent(
            email = email,
            password = password,
            isPasswordVisible = isPasswordVisible,
            onEmailChange = {
                email = it
                if (uiState.error != null) viewModel.clearError()
            },
            onPasswordChange = {
                password = it
                if (uiState.error != null) viewModel.clearError()
            },
            onTogglePasswordVisibility = { isPasswordVisible = !isPasswordVisible },
            onEmailNext = { focusManager.moveFocus(FocusDirection.Down) },
            onPasswordDone = {
                focusManager.clearFocus()
                if (email.isNotBlank() && password.isNotBlank()) {
                    viewModel.login(email, password)
                }
            },
            onLoginClick = {
                focusManager.clearFocus()
                viewModel.login(email, password)
            },
            onForgotPasswordClick = onForgotPasswordClick,
            onRegisterClick = onRegisterClick,
            isLoading = uiState.isLoading,
            error = uiState.error,
            modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LoginScreenContent(
        email: String,
        password: String,
        isPasswordVisible: Boolean,
        onEmailChange: (String) -> Unit,
        onPasswordChange: (String) -> Unit,
        onTogglePasswordVisibility: () -> Unit,
        onEmailNext: () -> Unit,
        onPasswordDone: () -> Unit,
        onLoginClick: () -> Unit,
        onForgotPasswordClick: () -> Unit,
        onRegisterClick: () -> Unit,
        isLoading: Boolean,
        error: String?,
        modifier: Modifier = Modifier
) {
    Scaffold(modifier = modifier.fillMaxSize()) { paddingValues ->
        Column(
                modifier =
                        Modifier.fillMaxSize()
                                .padding(paddingValues)
                                .verticalScroll(rememberScrollState())
                                .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
        ) {
            Image(
                    painter = painterResource(id = R.drawable.logo_medisupply),
                    contentDescription = stringResource(R.string.cd_medisupply_logo),
                    modifier = Modifier.size(120.dp).padding(bottom = 32.dp)
            )

            Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.large,
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                        modifier = Modifier.fillMaxWidth().padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                            text = stringResource(R.string.login_title),
                            style = MaterialTheme.typography.headlineMedium,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(bottom = 32.dp)
                    )

                    OutlinedTextField(
                            value = email,
                            onValueChange = onEmailChange,
                            label = { Text(stringResource(R.string.login_email_label)) },
                            placeholder = { Text(stringResource(R.string.login_email_placeholder)) },
                            leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                            singleLine = true,
                            keyboardOptions =
                                    KeyboardOptions(
                                            keyboardType = KeyboardType.Email,
                                            imeAction = ImeAction.Next
                                    ),
                            keyboardActions = KeyboardActions(onNext = { onEmailNext() }),
                            modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                            value = password,
                            onValueChange = onPasswordChange,
                            label = { Text(stringResource(R.string.login_password_label)) },
                            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                            singleLine = true,
                            trailingIcon = {
                                val icon =
                                        if (isPasswordVisible) Icons.Default.Visibility
                                        else Icons.Default.VisibilityOff
                                IconButton(onClick = onTogglePasswordVisibility) {
                                    Icon(icon, contentDescription = null)
                                }
                            },
                            visualTransformation =
                                    if (isPasswordVisible) VisualTransformation.None
                                    else PasswordVisualTransformation(),
                            keyboardOptions =
                                    KeyboardOptions(
                                            keyboardType = KeyboardType.Password,
                                            imeAction = ImeAction.Done
                                    ),
                            keyboardActions = KeyboardActions(onDone = { onPasswordDone() }),
                            modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                            onClick = onLoginClick,
                            enabled = email.isNotBlank() && password.isNotBlank() && !isLoading,
                            modifier = Modifier.fillMaxWidth().height(48.dp),
                            shape = MaterialTheme.shapes.large
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                    modifier = Modifier.size(20.dp),
                                    strokeWidth = 2.dp,
                                    color = MaterialTheme.colorScheme.onPrimary
                            )
                        } else {
                            Text(stringResource(R.string.login_button))
                        }
                    }

                    if (!error.isNullOrBlank()) {
                        Text(
                                text = error,
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.bodyMedium,
                                textAlign = TextAlign.Center,
                                modifier =
                                        Modifier.fillMaxWidth()
                                                .padding(top = 16.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    TextButton(onClick = onForgotPasswordClick) {
                        Text(stringResource(R.string.forgot_password))
                    }

                    TextButton(onClick = onRegisterClick) {
                        Text(stringResource(R.string.no_account_yet))
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LoginScreenPreview() {
    MobileMediSupplyTheme {
        LoginScreenContent(
                email = "demo@medisupply.com",
                password = "•••••••",
                isPasswordVisible = false,
                onEmailChange = {},
                onPasswordChange = {},
                onTogglePasswordVisibility = {},
                onEmailNext = {},
                onPasswordDone = {},
                onLoginClick = {},
                onForgotPasswordClick = {},
                onRegisterClick = {},
                isLoading = false,
                error = null
        )
    }
}
