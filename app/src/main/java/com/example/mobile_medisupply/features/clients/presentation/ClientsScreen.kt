package com.example.mobile_medisupply.features.clients.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mobile_medisupply.ui.theme.MobileMediSupplyTheme

data class ClientSummary(
        val name: String,
        val contact: String,
        val location: String
)

private val sampleClients =
        listOf(
                ClientSummary("Clínica San Rafael", "Laura Gómez", "Ciudad de México"),
                ClientSummary("Hospital Vida Plena", "Carlos Martínez", "Guadalajara"),
                ClientSummary("Centro Médico Los Álamos", "Ana Torres", "Bogotá"),
                ClientSummary("Salud Integral Norte", "Ricardo Pérez", "Medellín"),
                ClientSummary("Hospital Ángeles del Valle", "María Sánchez", "Monterrey"),
                ClientSummary("Clínica Santa Lucía", "Javier López", "Querétaro"),
                ClientSummary("Centro Médico Las Lomas", "Paula Hernández", "Puebla"),
                ClientSummary("Hospital Buen Vivir", "Andrés Silva", "Cali"),
                ClientSummary("Clínica Esperanza Azul", "Sofía Díaz", "Toluca")
        )

@Composable
fun ClientsScreen(
        clients: List<ClientSummary> = sampleClients,
        onClientSelected: (ClientSummary) -> Unit = {}
) {
    Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.surface
    ) {
        Column(
                modifier =
                        Modifier.fillMaxSize()
                                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f))
                                .padding(horizontal = 16.dp, vertical = 24.dp)
        ) {
            Text(
                    text = "Clientes",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(16.dp))

            Card(
                    modifier = Modifier.fillMaxSize(),
                    colors =
                            CardDefaults.cardColors(
                                    containerColor =
                                            MaterialTheme.colorScheme.surface.copy(alpha = 0.96f)
                            ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(0.dp)
                ) {
                    items(clients, key = { it.name }) { client ->
                        ClientRow(client = client, onClick = { onClientSelected(client) })

                        Divider(
                                color = MaterialTheme.colorScheme.surfaceVariant,
                                thickness = 1.dp,
                                modifier = Modifier.padding(horizontal = 16.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ClientRow(client: ClientSummary, onClick: () -> Unit) {
    Row(
            modifier =
                    Modifier.fillMaxWidth()
                            .clip(MaterialTheme.shapes.medium)
                            .clickable(onClick = onClick)
                            .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
                modifier =
                        Modifier.height(36.dp).background(
                                color =
                                        MaterialTheme.colorScheme.secondary.copy(alpha = 0.15f),
                                shape = MaterialTheme.shapes.small
                        ).padding(6.dp),
                contentAlignment = Alignment.Center
        ) {
            Icon(
                    imageVector = Icons.Outlined.Person,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
            )
        }

        Column(modifier = Modifier.padding(start = 16.dp).weight(1f)) {
            Text(
                    text = client.name,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                    text = "${client.contact} • ${client.location}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Icon(
                imageVector = Icons.Outlined.ChevronRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ClientsScreenPreview() {
    MobileMediSupplyTheme {
        ClientsScreen()
    }
}
