package com.example.mobile_medisupply.features.auth.presentation.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import com.example.mobile_medisupply.R
import com.example.mobile_medisupply.ui.theme.MobileMediSupplyTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
        onLoginClick: (String, String) -> Unit = { _, _ -> },
        onForgotPasswordClick: () -> Unit = {},
        modifier: Modifier = Modifier
) {
        var email by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
        var isPasswordVisible by remember { mutableStateOf(false) }
        val focusManager = LocalFocusManager.current

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

                        // Logo de MediSupply
                        Image(
                                painter =
                                        painterResource(
                                                id = R.drawable.logo_medisupply
                                        ), // Cambia por el nombre de tu imagen
                                contentDescription = "MediSupply Logo",
                                modifier = Modifier.size(120.dp).padding(bottom = 32.dp)
                        )

                        // Card principal con formulario
                        Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = MaterialTheme.shapes.large,
                                colors =
                                        CardDefaults.cardColors(
                                                containerColor = MaterialTheme.colorScheme.surface
                                        ),
                                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                        ) {
                                Column(
                                        modifier = Modifier.fillMaxWidth().padding(24.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                ) {

                                        // Título
                                        Text(
                                                text = stringResource(R.string.login_title),
                                                style = MaterialTheme.typography.headlineMedium,
                                                color = MaterialTheme.colorScheme.primary,
                                                fontWeight = FontWeight.Bold,
                                                textAlign = TextAlign.Center,
                                                modifier = Modifier.padding(bottom = 32.dp)
                                        )

                                        // Campo de email
                                        OutlinedTextField(
                                                value = email,
                                                onValueChange = { email = it },
                                                label = {
                                                        Text(
                                                                stringResource(
                                                                        R.string.login_email_label
                                                                )
                                                        )
                                                },
                                                placeholder = {
                                                        Text(
                                                                stringResource(
                                                                        R.string
                                                                                .login_email_placeholder
                                                                )
                                                        )
                                                },
                                                leadingIcon = {
                                                        Icon(
                                                                imageVector = Icons.Default.Email,
                                                                contentDescription = null
                                                        )
                                                },
                                                keyboardOptions =
                                                        KeyboardOptions(
                                                                keyboardType = KeyboardType.Email,
                                                                imeAction = ImeAction.Next
                                                        ),
                                                keyboardActions =
                                                        KeyboardActions(
                                                                onNext = {
                                                                        focusManager.moveFocus(
                                                                                FocusDirection.Down
                                                                        )
                                                                }
                                                        ),
                                                singleLine = true,
                                                modifier = Modifier.fillMaxWidth()
                                        )

                                        Spacer(modifier = Modifier.height(16.dp))

                                        // Campo de contraseña
                                        OutlinedTextField(
                                                value = password,
                                                onValueChange = { password = it },
                                                label = {
                                                        Text(
                                                                stringResource(
                                                                        R.string
                                                                                .login_password_label
                                                                )
                                                        )
                                                },
                                                visualTransformation =
                                                        if (isPasswordVisible) {
                                                                VisualTransformation.None
                                                        } else {
                                                                PasswordVisualTransformation()
                                                        },
                                                trailingIcon = {
                                                        IconButton(
                                                                onClick = {
                                                                        isPasswordVisible =
                                                                                !isPasswordVisible
                                                                },
                                                                modifier = Modifier.size(48.dp)
                                                        ) {
                                                                Icon(
                                                                        imageVector =
                                                                                if (isPasswordVisible
                                                                                ) {
                                                                                        Icons.Filled
                                                                                                .VisibilityOff
                                                                                } else {
                                                                                        Icons.Filled
                                                                                                .Visibility
                                                                                },
                                                                        contentDescription =
                                                                                if (isPasswordVisible
                                                                                ) {
                                                                                        stringResource(
                                                                                                R.string
                                                                                                        .cd_hide_password
                                                                                        )
                                                                                } else {
                                                                                        stringResource(
                                                                                                R.string
                                                                                                        .cd_show_password
                                                                                        )
                                                                                }
                                                                )
                                                        }
                                                },
                                                keyboardOptions =
                                                        KeyboardOptions(
                                                                keyboardType =
                                                                        KeyboardType.Password,
                                                                imeAction = ImeAction.Done
                                                        ),
                                                keyboardActions =
                                                        KeyboardActions(
                                                                onDone = {
                                                                        focusManager.clearFocus()
                                                                        if (email.isNotBlank() &&
                                                                                        password.isNotBlank()
                                                                        ) {
                                                                                onLoginClick(
                                                                                        email,
                                                                                        password
                                                                                )
                                                                        }
                                                                }
                                                        ),
                                                singleLine = true,
                                                modifier = Modifier.fillMaxWidth()
                                        )

                                        Spacer(modifier = Modifier.height(24.dp))

                                        // Botón de login
                                        Button(
                                                onClick = {
                                                        focusManager.clearFocus()
                                                        onLoginClick(email, password)
                                                },
                                                enabled =
                                                        email.isNotBlank() && password.isNotBlank(),
                                                modifier = Modifier.fillMaxWidth().height(48.dp),
                                                shape = MaterialTheme.shapes.large
                                        ) { Text(stringResource(R.string.login_button)) }

                                        Spacer(modifier = Modifier.height(16.dp))

                                        // Enlace de recuperación de contraseña
                                        TextButton(onClick = onForgotPasswordClick) {
                                                Text(stringResource(R.string.forgot_password))
                                        }
                                }
                        }
                }
        }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
        MobileMediSupplyTheme { LoginScreen() }
}
