package com.example.mobile_medisupply.features.auth.presentation.register

import androidx.compose.foundation.Image
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.outlined.ArrowDropDown
import androidx.compose.material.icons.outlined.Public
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.mobile_medisupply.R
import com.example.mobile_medisupply.features.auth.data.repository.Pais
import com.example.mobile_medisupply.ui.theme.MobileMediSupplyTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
        onRegisterSuccess: () -> Unit = {},
        onLoginClick: () -> Unit = {},
        modifier: Modifier = Modifier,
        viewModel: RegisterViewModel = hiltViewModel()
) {
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        var companyName by remember { mutableStateOf("") }
        var taxId by remember { mutableStateOf("") }
        var institutionType by remember { mutableStateOf("") }
        var clasificacion by remember { mutableStateOf("") }
        var personInCharge by remember { mutableStateOf("") }
        var selectedPais by remember { mutableStateOf<Pais?>(null) }
        var paisMenuExpanded by remember { mutableStateOf(false) }
        var email by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
        var confirmPassword by remember { mutableStateOf("") }
        var isPasswordVisible by remember { mutableStateOf(false) }
        var isConfirmPasswordVisible by remember { mutableStateOf(false) }
        val focusManager = LocalFocusManager.current

        val isFormValid =
                companyName.isNotBlank() &&
                        taxId.isNotBlank() &&
                        institutionType.isNotBlank() &&
                        clasificacion.isNotBlank() &&
                        personInCharge.isNotBlank() &&
                        selectedPais != null &&
                        email.isNotBlank() &&
                        password.isNotBlank() &&
                        confirmPassword.isNotBlank() &&
                        password == confirmPassword

        LaunchedEffect(uiState.registrationSuccess) {
                if (uiState.registrationSuccess) {
                        onRegisterSuccess()
                        viewModel.onRegistrationNavigated()
                }
        }

        fun attemptRegister() {
                if (uiState.isRegistering || !isFormValid) return
                selectedPais?.let { pais ->
                        viewModel.registrarCliente(
                                nombre = companyName,
                                identificacion = taxId,
                                tipoIdentificacion = 2, // Default value as per API spec
                                tipoInstitucion = institutionType,
                                clasificacion = clasificacion,
                                responsableContacto = personInCharge,
                                paisId = pais.id,
                                email = email,
                                password = password
                        )
                }
        }

        Scaffold(modifier = modifier.fillMaxSize()) { paddingValues ->
                Column(
                        modifier =
                                Modifier.fillMaxSize()
                                        .padding(paddingValues)
                                        .verticalScroll(rememberScrollState())
                                        .padding(horizontal = 24.dp, vertical = 16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                ) {
                        // Logo de MediSupply
                        Image(
                                painter = painterResource(id = R.drawable.logo_medisupply),
                                contentDescription = stringResource(R.string.cd_medisupply_logo),
                                modifier = Modifier.size(100.dp).padding(bottom = 16.dp)
                        )

                        // Título
                        Text(
                                text = stringResource(R.string.register_title),
                                style = MaterialTheme.typography.headlineMedium,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(bottom = 24.dp)
                        )

                        // Formulario
                        Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = MaterialTheme.shapes.large,
                                colors =
                                        CardDefaults.cardColors(
                                                containerColor = MaterialTheme.colorScheme.surface
                                        ),
                                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                        ) {
                                Column(
                                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                                        verticalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                        // Nombre Empresa
                                        OutlinedTextField(
                                                value = companyName,
                                                onValueChange = { companyName = it },
                                                label = {
                                                        Text(
                                                                stringResource(
                                                                        R.string.company_name_label
                                                                )
                                                        )
                                                },
                                                placeholder = {
                                                        Text(
                                                                stringResource(
                                                                        R.string
                                                                                .company_name_placeholder
                                                                )
                                                        )
                                                },
                                                leadingIcon = {
                                                        Icon(
                                                                Icons.Default.Business,
                                                                contentDescription = null
                                                        )
                                                },
                                                keyboardOptions =
                                                        KeyboardOptions(imeAction = ImeAction.Next),
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

                                        // Identificación Tributaria
                                        OutlinedTextField(
                                                value = taxId,
                                                onValueChange = { taxId = it },
                                                label = {
                                                        Text(stringResource(R.string.tax_id_label))
                                                },
                                                placeholder = {
                                                        Text(
                                                                stringResource(
                                                                        R.string.tax_id_placeholder
                                                                )
                                                        )
                                                },
                                                keyboardOptions =
                                                        KeyboardOptions(imeAction = ImeAction.Next),
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

                                        // Tipo Institución
                                        OutlinedTextField(
                                                value = institutionType,
                                                onValueChange = { institutionType = it },
                                                label = {
                                                        Text(
                                                                stringResource(
                                                                        R.string
                                                                                .institution_type_label
                                                                )
                                                        )
                                                },
                                                placeholder = {
                                                        Text(
                                                                stringResource(
                                                                        R.string
                                                                                .institution_type_placeholder
                                                                )
                                                        )
                                                },
                                                keyboardOptions =
                                                        KeyboardOptions(imeAction = ImeAction.Next),
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

                                        // Clasificación (Privada/Pública)
                                        OutlinedTextField(
                                                value = clasificacion,
                                                onValueChange = { clasificacion = it },
                                                label = { Text("Clasificación") },
                                                placeholder = { Text("Ej: Privada, Pública") },
                                                keyboardOptions =
                                                        KeyboardOptions(imeAction = ImeAction.Next),
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

                                        // País Dropdown
                                        Box(modifier = Modifier.fillMaxWidth()) {
                                                OutlinedTextField(
                                                        value = selectedPais?.nombre ?: "",
                                                        onValueChange = {},
                                                        label = { Text("País") },
                                                        placeholder = {
                                                                Text("Selecciona un país")
                                                        },
                                                        readOnly = true,
                                                        leadingIcon = {
                                                                Icon(
                                                                        Icons.Outlined.Public,
                                                                        contentDescription = null
                                                                )
                                                        },
                                                        trailingIcon = {
                                                                IconButton(
                                                                        onClick = {
                                                                                paisMenuExpanded =
                                                                                        !paisMenuExpanded
                                                                        }
                                                                ) {
                                                                        Icon(
                                                                                imageVector =
                                                                                        Icons.Outlined
                                                                                                .ArrowDropDown,
                                                                                contentDescription =
                                                                                        "Seleccionar país"
                                                                        )
                                                                }
                                                        },
                                                        modifier =
                                                                Modifier.fillMaxWidth().clickable {
                                                                        paisMenuExpanded = true
                                                                },
                                                        singleLine = true
                                                )

                                                DropdownMenu(
                                                        expanded = paisMenuExpanded,
                                                        onDismissRequest = {
                                                                paisMenuExpanded = false
                                                        },
                                                        modifier = Modifier.fillMaxWidth()
                                                ) {
                                                        uiState.paises.forEach { pais ->
                                                                DropdownMenuItem(
                                                                        text = {
                                                                                Text(
                                                                                        "${pais.nombre} (${pais.codigoIso})"
                                                                                )
                                                                        },
                                                                        onClick = {
                                                                                selectedPais = pais
                                                                                paisMenuExpanded =
                                                                                        false
                                                                        }
                                                                )
                                                        }
                                                }
                                        }

                                        // Responsable
                                        OutlinedTextField(
                                                value = personInCharge,
                                                onValueChange = { personInCharge = it },
                                                label = {
                                                        Text(
                                                                stringResource(
                                                                        R.string
                                                                                .person_in_charge_label
                                                                )
                                                        )
                                                },
                                                placeholder = {
                                                        Text(
                                                                stringResource(
                                                                        R.string
                                                                                .person_in_charge_placeholder
                                                                )
                                                        )
                                                },
                                                leadingIcon = {
                                                        Icon(
                                                                Icons.Default.Person,
                                                                contentDescription = null
                                                        )
                                                },
                                                keyboardOptions =
                                                        KeyboardOptions(imeAction = ImeAction.Next),
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

                                        // Correo
                                        OutlinedTextField(
                                                value = email,
                                                onValueChange = { email = it },
                                                label = {
                                                        Text(stringResource(R.string.email_label))
                                                },
                                                placeholder = {
                                                        Text(
                                                                stringResource(
                                                                        R.string.email_placeholder
                                                                )
                                                        )
                                                },
                                                leadingIcon = {
                                                        Icon(
                                                                Icons.Default.Email,
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

                                        // Contraseña
                                        OutlinedTextField(
                                                value = password,
                                                onValueChange = { password = it },
                                                label = {
                                                        Text(
                                                                stringResource(
                                                                        R.string.password_label
                                                                )
                                                        )
                                                },
                                                visualTransformation =
                                                        if (isPasswordVisible)
                                                                VisualTransformation.None
                                                        else PasswordVisualTransformation(),
                                                trailingIcon = {
                                                        IconButton(
                                                                onClick = {
                                                                        isPasswordVisible =
                                                                                !isPasswordVisible
                                                                }
                                                        ) {
                                                                Icon(
                                                                        imageVector =
                                                                                if (isPasswordVisible
                                                                                )
                                                                                        Icons.Filled
                                                                                                .VisibilityOff
                                                                                else
                                                                                        Icons.Filled
                                                                                                .Visibility,
                                                                        contentDescription =
                                                                                if (isPasswordVisible
                                                                                )
                                                                                        stringResource(
                                                                                                R.string
                                                                                                        .cd_hide_password
                                                                                        )
                                                                                else
                                                                                        stringResource(
                                                                                                R.string
                                                                                                        .cd_show_password
                                                                                        )
                                                                )
                                                        }
                                                },
                                                keyboardOptions =
                                                        KeyboardOptions(
                                                                keyboardType =
                                                                        KeyboardType.Password,
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

                                        // Validar Contraseña
                                        OutlinedTextField(
                                                value = confirmPassword,
                                                onValueChange = { confirmPassword = it },
                                                label = {
                                                        Text(
                                                                stringResource(
                                                                        R.string
                                                                                .confirm_password_label
                                                                )
                                                        )
                                                },
                                                visualTransformation =
                                                        if (isConfirmPasswordVisible)
                                                                VisualTransformation.None
                                                        else PasswordVisualTransformation(),
                                                isError =
                                                        password != confirmPassword &&
                                                                confirmPassword.isNotEmpty(),
                                                trailingIcon = {
                                                        IconButton(
                                                                onClick = {
                                                                        isConfirmPasswordVisible =
                                                                                !isConfirmPasswordVisible
                                                                }
                                                        ) {
                                                                Icon(
                                                                        imageVector =
                                                                                if (isConfirmPasswordVisible
                                                                                )
                                                                                        Icons.Filled
                                                                                                .VisibilityOff
                                                                                else
                                                                                        Icons.Filled
                                                                                                .Visibility,
                                                                        contentDescription =
                                                                                if (isConfirmPasswordVisible
                                                                                )
                                                                                        stringResource(
                                                                                                R.string
                                                                                                        .cd_hide_password
                                                                                        )
                                                                                else
                                                                                        stringResource(
                                                                                                R.string
                                                                                                        .cd_show_password
                                                                                        )
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
                                                                        attemptRegister()
                                                                }
                                                        ),
                                                singleLine = true,
                                                modifier = Modifier.fillMaxWidth()
                                        )

                                        if (password != confirmPassword &&
                                                        confirmPassword.isNotEmpty()
                                        ) {
                                                Text(
                                                        text = "Las contraseñas no coinciden",
                                                        color = MaterialTheme.colorScheme.error,
                                                        style = MaterialTheme.typography.bodySmall,
                                                        modifier = Modifier.fillMaxWidth()
                                                )
                                        }

                                        Spacer(modifier = Modifier.height(8.dp))

                                        // Botón de registro
                                        Button(
                                                onClick = {
                                                        focusManager.clearFocus()
                                                        attemptRegister()
                                                },
                                                enabled = isFormValid && !uiState.isRegistering,
                                                modifier = Modifier.fillMaxWidth().height(50.dp),
                                                shape = MaterialTheme.shapes.medium
                                        ) {
                                                if (uiState.isRegistering) {
                                                        CircularProgressIndicator(
                                                                modifier = Modifier.size(20.dp),
                                                                strokeWidth = 2.dp,
                                                                color =
                                                                        MaterialTheme.colorScheme
                                                                                .onPrimary
                                                        )
                                                } else {
                                                        Text(
                                                                stringResource(
                                                                        R.string.register_button
                                                                )
                                                        )
                                                }
                                        }

                                        // Mostrar error de registro
                                        if (uiState.error != null) {
                                                Text(
                                                        text = uiState.error ?: "",
                                                        color = MaterialTheme.colorScheme.error,
                                                        style = MaterialTheme.typography.bodyMedium,
                                                        textAlign = TextAlign.Center,
                                                        modifier =
                                                                Modifier.fillMaxWidth()
                                                                        .padding(top = 8.dp)
                                                )
                                        }

                                        Spacer(modifier = Modifier.height(8.dp))

                                        // Enlace para ir a login
                                        TextButton(
                                                onClick = onLoginClick,
                                                modifier =
                                                        Modifier.align(Alignment.CenterHorizontally)
                                        ) {
                                                Text(
                                                        text =
                                                                stringResource(
                                                                        R.string
                                                                                .already_have_account
                                                                ),
                                                        textAlign = TextAlign.Center
                                                )
                                        }
                                }
                        }
                }
        }
}

@Preview(showBackground = true)
@Composable
fun RegisterScreenPreview() {
        MobileMediSupplyTheme { RegisterScreen() }
}
