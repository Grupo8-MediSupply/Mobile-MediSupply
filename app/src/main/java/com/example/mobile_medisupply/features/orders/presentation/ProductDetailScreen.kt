package com.example.mobile_medisupply.features.orders.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.mobile_medisupply.features.orders.domain.model.ProductCatalogItem

@Composable
fun ProductDetailScreen(
        product: ProductCatalogItem,
        currentQuantity: Int,
        onBackClick: () -> Unit,
        onQuantityConfirmed: (Int) -> Unit
) {
    var quantity by remember(product.id) { mutableStateOf(currentQuantity.coerceAtLeast(0)) }

    Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.surface
    ) {
        Column(
                modifier =
                        Modifier.fillMaxSize()
                                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f))
        ) {
            DetailHeader(productName = product.name, onBackClick = onBackClick)

            Column(
                    modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp)
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                DetailSummaryCard(product = product)

                Spacer(modifier = Modifier.height(16.dp))

                InventoryCard(product)

                Spacer(modifier = Modifier.height(12.dp))

                PricingCard(product)

                Spacer(modifier = Modifier.height(24.dp))

                QuantitySection(
                        quantity = quantity,
                        onIncrement = { quantity += 1 },
                        onDecrement = { quantity = (quantity - 1).coerceAtLeast(0) }
                ) {
                    onQuantityConfirmed(quantity)
                }
            }
        }
    }
}

@Composable
private fun DetailHeader(productName: String, onBackClick: () -> Unit) {
    Row(
            modifier =
                    Modifier.fillMaxWidth()
                            .background(MaterialTheme.colorScheme.surface)
                            .padding(horizontal = 8.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onBackClick) {
            Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Volver",
                    tint = MaterialTheme.colorScheme.primary
            )
        }

        Spacer(modifier = Modifier.size(8.dp))

        Text(
                text = "MediSupply",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
        )
    }

    Text(
            text = productName,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
    )
}

@Composable
private fun DetailSummaryCard(product: ProductCatalogItem) {
    Card(
            modifier = Modifier.fillMaxWidth(),
            colors =
                    CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                    ),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
            Text(
                    text = "Descripción",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                    text = product.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                    text = product.presentation,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(12.dp))

            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                DetailRow(label = "Unidad", value = product.unit)
                DetailRow(label = "Categoría", value = product.category)
                DetailRow(label = "Proveedor", value = product.provider)
                DetailRow(label = "País", value = product.country)
            }

            if (!product.guidelineLabel.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(16.dp))
                TextButton(onClick = { /* TODO: abrir normativa */ }) {
                    Text(text = product.guidelineLabel, color = MaterialTheme.colorScheme.primary)
                }
            }
        }
    }
}

@Composable
private fun InventoryCard(product: ProductCatalogItem) {
    Card(
            modifier = Modifier.fillMaxWidth(),
            colors =
                    CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                    ),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
            Text(
                    text = "Inventario",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(12.dp))
            DetailRow(label = "Stock total", value = product.inventory.total.toString())
            DetailRow(label = "Reservado", value = product.inventory.reserved.toString())
            DetailRow(label = "Disponible", value = product.inventory.available.toString())
            DetailRow(label = "Bodega", value = product.inventory.warehouse)
        }
    }
}

@Composable
private fun PricingCard(product: ProductCatalogItem) {
    Card(
            modifier = Modifier.fillMaxWidth(),
            colors =
                    CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                    ),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
            Text(
                    text = "Precio",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                    text = product.pricing.formattedPrice,
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                    text = "Últ. cambio: ${product.pricing.lastUpdated}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun QuantitySection(
        quantity: Int,
        onIncrement: () -> Unit,
        onDecrement: () -> Unit,
        onConfirm: () -> Unit
) {
    Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Button(onClick = onConfirm) {
            Text("Agregar")
        }

        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            QuantityButton(symbol = "-", enabled = quantity > 0, onClick = onDecrement)
            Text(
                    text = quantity.toString(),
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurface
            )
            QuantityButton(symbol = "+", onClick = onIncrement)
        }
    }
}

@Composable
private fun QuantityButton(symbol: String, enabled: Boolean = true, onClick: () -> Unit) {
    Surface(
            modifier = Modifier.clip(MaterialTheme.shapes.large),
            color =
                    if (enabled) MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                    else MaterialTheme.colorScheme.surfaceVariant,
            onClick = { if (enabled) onClick() }
    ) {
        Box(modifier = Modifier.size(40.dp), contentAlignment = Alignment.Center) {
            Text(
                    text = symbol,
                    style = MaterialTheme.typography.titleLarge,
                    color =
                            if (enabled) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun DetailRow(label: String, value: String) {
    Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
        )
        Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
        )
    }
}
