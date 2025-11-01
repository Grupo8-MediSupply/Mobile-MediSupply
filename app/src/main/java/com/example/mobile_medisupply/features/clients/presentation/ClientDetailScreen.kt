package com.example.mobile_medisupply.features.clients.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mobile_medisupply.features.clients.data.ClientRepositoryProvider
import com.example.mobile_medisupply.features.clients.domain.model.ClientDetail
import com.example.mobile_medisupply.features.clients.domain.model.ClientOrderSummary
import com.example.mobile_medisupply.ui.theme.MobileMediSupplyTheme

@Composable
fun ClientDetailScreen(
        clientDetail: ClientDetail,
        onBackClick: () -> Unit = {},
        onShowAllOrders: () -> Unit = {},
        onOrderSelected: (ClientOrderSummary) -> Unit = {}
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
            Icon(
                    imageVector = Icons.Outlined.ArrowBack,
                    contentDescription = "Volver",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier =
                            Modifier.padding(bottom = 16.dp).clip(MaterialTheme.shapes.small)
                                    .clickable(onClick = onBackClick)
                                    .padding(4.dp)
            )

            Text(
                    text = clientDetail.name,
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(16.dp))

            ClientDetailCard(clientDetail)

            Spacer(modifier = Modifier.height(24.dp))

            OrdersSection(
                    orders = clientDetail.orders,
                    onShowAll = onShowAllOrders,
                    onOrderSelected = onOrderSelected
            )
        }
    }
}

@Composable
private fun ClientDetailCard(detail: ClientDetail) {
    Card(
            modifier = Modifier.fillMaxWidth(),
            colors =
                    CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface,
                            contentColor = MaterialTheme.colorScheme.onSurface
                    ),
            elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(20.dp)) {
            Text(
                    text = "Detalle",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(12.dp))

            DetailRow(label = "Nombre", value = detail.contact)
            DetailRow(label = "Ciudad", value = detail.city)
            DetailRow(label = "Teléfono", value = detail.phone)
            DetailRow(label = "Tipo institución", value = detail.institutionType)
        }
    }
}

@Composable
private fun DetailRow(label: String, value: String) {
    Row(
            modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun OrdersSection(
        orders: List<ClientOrderSummary>,
        onShowAll: () -> Unit,
        onOrderSelected: (ClientOrderSummary) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                        text = "Órdenes por visita",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                )
                Icon(
                        imageVector = Icons.Outlined.ChevronRight,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(start = 4.dp)
                )
            }
            Text(
                    text = "Ver todas",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.clickable(onClick = onShowAll)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
                modifier =
                        Modifier.fillMaxSize()
                                .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.9f))
                                .clip(MaterialTheme.shapes.large)
        ) {
            items(orders, key = { it.id }) { order ->
                ClientOrderRow(order = order, onClick = { onOrderSelected(order) })
            }
        }
    }
}

@Composable
private fun ClientOrderRow(order: ClientOrderSummary, onClick: () -> Unit) {
    Row(
            modifier =
                    Modifier.fillMaxWidth()
                            .clickable(onClick = onClick)
                            .padding(horizontal = 16.dp, vertical = 18.dp),
            verticalAlignment = Alignment.CenterVertically
    ) {
        // Placeholder for order avatar
        Card(
                colors =
                        CardDefaults.cardColors(
                                containerColor =
                                        MaterialTheme.colorScheme.secondary.copy(alpha = 0.15f),
                                contentColor = MaterialTheme.colorScheme.secondary
                        ),
                shape = MaterialTheme.shapes.medium
        ) {
            Spacer(modifier = Modifier.height(42.dp).fillMaxWidth(0.2f))
        }

        Column(modifier = Modifier.padding(start = 16.dp).weight(1f)) {
            Text(
                    text = order.title,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                    text = order.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Icon(
                imageVector = Icons.Outlined.MoreVert,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Preview(showBackground = true, widthDp = 360)
@Composable
private fun ClientDetailScreenPreview() {
    MobileMediSupplyTheme {
        ClientDetailScreen(
                clientDetail =
                        ClientRepositoryProvider.repository.getClientDetail("clinica-san-rafael")!!
        )
    }
}
