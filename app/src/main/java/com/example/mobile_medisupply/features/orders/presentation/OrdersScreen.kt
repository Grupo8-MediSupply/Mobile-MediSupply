package com.example.mobile_medisupply.features.orders.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AddCircle
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.example.mobile_medisupply.features.orders.data.OrdersRepositoryProvider
import com.example.mobile_medisupply.features.orders.domain.model.OrderStatus
import com.example.mobile_medisupply.features.orders.domain.model.OrderSummary
import com.example.mobile_medisupply.ui.theme.MobileMediSupplyTheme

@Composable
fun OrdersScreen(
        orders: List<OrderSummary> =
                OrdersRepositoryProvider.repository.getProcessedOrders(),
        onCreateOrderClick: () -> Unit = {},
        onOrderClick: (OrderSummary) -> Unit = {},
        onOrderMoreActionsClick: (OrderSummary) -> Unit = {}
) {
    Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.surface
    ) {
        Column(
                modifier =
                        Modifier.fillMaxSize()
                                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.25f))
                                .padding(horizontal = 16.dp, vertical = 24.dp)
        ) {
            Header(onCreateOrderClick = onCreateOrderClick)

            Spacer(modifier = Modifier.height(16.dp))

            OrdersList(
                    orders = orders,
                    onOrderClick = onOrderClick,
                    onOrderMoreActionsClick = onOrderMoreActionsClick
            )
        }
    }
}

@Composable
private fun Header(onCreateOrderClick: () -> Unit) {
    Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
                text = "Ordenes",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary
        )

        ElevatedButton(onClick = onCreateOrderClick) {
            Icon(imageVector = Icons.Outlined.AddCircle, contentDescription = null)
            Spacer(modifier = Modifier.size(8.dp))
            Text(text = "Crear Orden")
        }
    }
}

@Composable
private fun OrdersList(
        orders: List<OrderSummary>,
        onOrderClick: (OrderSummary) -> Unit,
        onOrderMoreActionsClick: (OrderSummary) -> Unit,
        modifier: Modifier = Modifier
) {
    Card(
            modifier = modifier.fillMaxWidth(),
            colors =
                    CardDefaults.cardColors(
                            containerColor =
                                    MaterialTheme.colorScheme.surface.copy(alpha = 0.96f)
                    ),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        if (orders.isEmpty()) {
            EmptyState()
        } else {
            LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                itemsIndexed(orders, key = { _, order -> order.id }) { index, order ->
                    OrderRow(
                            order = order,
                            onClick = { onOrderClick(order) },
                            onMoreActionsClick = { onOrderMoreActionsClick(order) }
                    )
                    if (index < orders.lastIndex) {
                        HorizontalDivider(
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
private fun OrderRow(
        order: OrderSummary,
        onClick: () -> Unit,
        onMoreActionsClick: () -> Unit
) {
    Row(
            modifier =
                    Modifier.fillMaxWidth()
                            .clip(MaterialTheme.shapes.medium)
                            .clickable(onClick = onClick)
                            .background(MaterialTheme.colorScheme.surface)
                            .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
                modifier =
                        Modifier.size(44.dp)
                                .clip(MaterialTheme.shapes.small)
                                .background(
                                        MaterialTheme.colorScheme.secondary.copy(alpha = 0.15f)
                                ),
                contentAlignment = Alignment.Center
        ) {
            Icon(
                    imageVector = Icons.Outlined.Description,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
            )
        }

        Column(modifier = Modifier.padding(start = 16.dp).weight(1f)) {
            Text(
                    text = order.clientName,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                    text = "Orden #${order.orderNumber}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            OrderStatusBadge(order.status)
        }

        Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.Center
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                        imageVector = Icons.Outlined.CalendarMonth,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.size(4.dp))
                Text(
                        text = order.createdAt,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = FontWeight.Medium
                )
            }

            IconButton(onClick = onMoreActionsClick) {
                Icon(
                        imageVector = Icons.Outlined.MoreVert,
                        contentDescription = "Más opciones"
                )
            }
        }
    }
}

@Composable
private fun OrderStatusBadge(status: OrderStatus) {
    val background =
            when (status) {
                OrderStatus.PROCESSED ->
                        MaterialTheme.colorScheme.tertiary.copy(alpha = 0.12f)
                OrderStatus.IN_PROGRESS ->
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
                OrderStatus.PENDING ->
                        MaterialTheme.colorScheme.error.copy(alpha = 0.12f)
            }
    val contentColor =
            when (status) {
                OrderStatus.PROCESSED -> MaterialTheme.colorScheme.tertiary
                OrderStatus.IN_PROGRESS -> MaterialTheme.colorScheme.primary
                OrderStatus.PENDING -> MaterialTheme.colorScheme.error
            }

    Box(
            modifier =
                    Modifier.padding(top = 8.dp)
                            .clip(MaterialTheme.shapes.small)
                            .background(background)
                            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
                text = status.displayName,
                style = MaterialTheme.typography.labelSmall,
                color = contentColor
        )
    }
}

@Composable
private fun EmptyState() {
    Column(
            modifier =
                    Modifier.fillMaxWidth()
                            .padding(vertical = 48.dp, horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
    ) {
        Icon(
                imageVector = Icons.Outlined.Description,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(48.dp)
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
                text = "Aún no tienes órdenes procesadas.",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurface
        )
        Text(
                text = "Crea una nueva orden para comenzar.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun OrdersScreenPreview() {
    MobileMediSupplyTheme {
        OrdersScreen(
                orders =
                        listOf(
                                OrderSummary(
                                        id = "order-1",
                                        orderNumber = "7777777",
                                        clientName = "Cliente A",
                                        status = OrderStatus.PROCESSED,
                                        createdAt = "12 Mar 2024"
                                ),
                                OrderSummary(
                                        id = "order-2",
                                        orderNumber = "8888888",
                                        clientName = "Cliente B",
                                        status = OrderStatus.PROCESSED,
                                        createdAt = "09 Mar 2024"
                                )
                        )
        )
    }
}
