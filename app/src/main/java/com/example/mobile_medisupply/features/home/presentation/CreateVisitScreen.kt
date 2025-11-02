package com.example.mobile_medisupply.features.home.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowDropDown
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.example.mobile_medisupply.R
import com.example.mobile_medisupply.features.clients.data.ClientRepositoryProvider
import com.example.mobile_medisupply.features.clients.domain.model.ClientSummary
import com.example.mobile_medisupply.features.home.domain.model.CreateVisitRequest
import com.example.mobile_medisupply.ui.theme.MobileMediSupplyTheme
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.IconButton
import androidx.compose.ui.Alignment

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateVisitScreen(
        clients: List<ClientSummary> = ClientRepositoryProvider.repository.getClients(),
        onBackClick: () -> Unit,
        onSubmit: (CreateVisitRequest) -> Unit,
        modifier: Modifier = Modifier,
        initialClientId: String? = null
) {
    var selectedClient by remember {
        mutableStateOf(
                initialClientId?.let { id ->
                    clients.find { it.id == id }
                }
        )
    }
    var clientMenuExpanded by remember { mutableStateOf(false) }
    var address by rememberSaveable { mutableStateOf(selectedClient?.location.orEmpty()) }
    var scheduledDate by rememberSaveable { mutableStateOf("") }
    var showDatePicker by remember { mutableStateOf(false) }

    LaunchedEffect(selectedClient) {
        if (selectedClient != null) {
            address = selectedClient?.location.orEmpty()
        }
    }

    val dateFormatter = remember {
        SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
    }
    val datePickerState = rememberDatePickerState()

    if (showDatePicker) {
        DatePickerDialog(
                onDismissRequest = { showDatePicker = false },
                confirmButton = {
                    TextButton(
                            onClick = {
                                val millis = datePickerState.selectedDateMillis
                                if (millis != null) {
                                    scheduledDate = dateFormatter.format(Date(millis))
                                }
                                showDatePicker = false
                            }
                    ) {
                        Text(text = stringResource(R.string.action_ok))
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDatePicker = false }) {
                        Text(text = stringResource(R.string.cancel))
                    }
                },
                properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            DatePicker(state = datePickerState)
        }
    }

    val isValid = remember(selectedClient, address, scheduledDate) {
        selectedClient != null && address.isNotBlank() && scheduledDate.isNotBlank()
    }

    Scaffold(modifier = modifier.fillMaxSize()) { paddingValues ->
        Column(
                modifier =
                        Modifier.fillMaxSize()
                                .padding(paddingValues)
                                .background(
                                        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f)
                                )
                                .padding(horizontal = 16.dp, vertical = 24.dp)
        ) {
            Text(
                    text = stringResource(R.string.create_visit_title),
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (clients.isEmpty()) {
                Text(
                        text = stringResource(R.string.create_visit_empty_clients),
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth().padding(top = 32.dp)
                )

                Spacer(modifier = Modifier.height(24.dp))

                TextButton(onClick = onBackClick) {
                    Text(text = stringResource(R.string.cancel))
                }
            } else {
                Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors =
                                CardDefaults.cardColors(
                                        containerColor =
                                                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.65f)
                                ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Column(
                            modifier = Modifier.fillMaxWidth().padding(20.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                                text = stringResource(R.string.create_visit_subtitle),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                    text = stringResource(R.string.create_visit_card_title),
                                    style = MaterialTheme.typography.titleLarge,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    fontWeight = FontWeight.SemiBold
                            )

                            IconButton(onClick = { showDatePicker = true }) {
                                Icon(
                                        imageVector = Icons.Outlined.CalendarMonth,
                                        contentDescription =
                                                stringResource(R.string.create_visit_pick_date_cd)
                                )
                            }
                        }

                        HorizontalDivider(color = MaterialTheme.colorScheme.surfaceVariant)

                        Box(modifier = Modifier.fillMaxWidth()) {
                            OutlinedTextField(
                                    value = selectedClient?.name.orEmpty(),
                                    onValueChange = {},
                                    label = {
                                        Text(stringResource(R.string.create_visit_client_label))
                                    },
                                    placeholder = {
                                        Text(stringResource(R.string.create_visit_client_placeholder))
                                    },
                                    readOnly = true,
                                    trailingIcon = {
                                        IconButton(onClick = { clientMenuExpanded = !clientMenuExpanded }) {
                                            Icon(
                                                    imageVector = Icons.Outlined.ArrowDropDown,
                                                    contentDescription =
                                                            stringResource(
                                                                    R.string.create_visit_select_client_cd
                                                            )
                                            )
                                        }
                                    },
                                    modifier =
                                            Modifier.fillMaxWidth()
                                                    .clickable { clientMenuExpanded = true },
                                    singleLine = true
                            )

                            DropdownMenu(
                                    expanded = clientMenuExpanded,
                                    onDismissRequest = { clientMenuExpanded = false },
                                    modifier = Modifier.fillMaxWidth()
                            ) {
                                clients.forEach { client ->
                                    DropdownMenuItem(
                                            text = { Text(client.name) },
                                            onClick = {
                                                selectedClient = client
                                                clientMenuExpanded = false
                                            }
                                    )
                                }
                            }
                        }

                        OutlinedTextField(
                                value = address,
                                onValueChange = { address = it },
                                label = { Text(stringResource(R.string.create_visit_address_label)) },
                                placeholder = {
                                    Text(
                                            selectedClient?.location
                                                    ?: stringResource(
                                                            R.string.create_visit_address_placeholder
                                                    )
                                    )
                                },
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true
                        )

                        OutlinedTextField(
                                value = scheduledDate,
                                onValueChange = {},
                                label = { Text(stringResource(R.string.create_visit_date_label)) },
                                placeholder = {
                                    Text(stringResource(R.string.create_visit_date_placeholder))
                                },
                                readOnly = true,
                                trailingIcon = {
                                    IconButton(onClick = { showDatePicker = true }) {
                                        Icon(
                                                imageVector = Icons.Outlined.CalendarMonth,
                                                contentDescription =
                                                        stringResource(R.string.create_visit_pick_date_cd)
                                        )
                                    }
                                },
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true
                        )

                        Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End
                        ) {
                            TextButton(onClick = onBackClick) {
                                Text(text = stringResource(R.string.cancel))
                            }

                            Spacer(modifier = Modifier.size(16.dp))

                            TextButton(
                                    onClick = {
                                        if (selectedClient != null) {
                                            onSubmit(
                                                    CreateVisitRequest(
                                                            clientId = selectedClient!!.id,
                                                            clientName = selectedClient!!.name,
                                                            address = address.trim(),
                                                            scheduledDate = scheduledDate
                                                    )
                                            )
                                        }
                                    },
                                    enabled = isValid
                            ) {
                                Text(text = stringResource(R.string.action_ok))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 360)
@Composable
private fun CreateVisitScreenPreview() {
    val sampleClient =
            ClientSummary(
                    id = "clinica-san-rafael",
                    name = "Clínica San Rafael",
                    contact = "Laura Gómez",
                    location = "Bogotá",
                    phone = "601 4455 8899",
                    institutionType = "Hospital"
            )
    MobileMediSupplyTheme {
        CreateVisitScreen(
                clients = listOf(sampleClient),
                onBackClick = {},
                onSubmit = {}
        )
    }
}
