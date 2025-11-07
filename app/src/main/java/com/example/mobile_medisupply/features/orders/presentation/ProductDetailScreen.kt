package com.example.mobile_medisupply.features.orders.presentation

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import com.example.mobile_medisupply.features.orders.domain.model.ProductWarehouse

@Composable
fun ProductDetailScreen(
        product: ProductCatalogItem,
        currentQuantity: Int,
        onBackClick: () -> Unit,
        onQuantityConfirmed: (
                quantity: Int,
                warehouseId: String,
                warehouseName: String,
                lotId: String,
                lotLabel: String?
        ) -> Unit
) {
    var quantity by remember(product.id) { mutableStateOf(currentQuantity.coerceAtLeast(0)) }
    val warehouseOptions = product.warehouses
    var selectedWarehouseId by remember(product.id) { mutableStateOf(warehouseOptions.firstOrNull()?.id) }
    val selectedWarehouse = warehouseOptions.firstOrNull { it.id == selectedWarehouseId }
    var selectedLotId by remember(selectedWarehouseId) {
        mutableStateOf(selectedWarehouse?.batches?.firstOrNull()?.id)
    }
    val selectedLot = selectedWarehouse?.batches?.firstOrNull { it.id == selectedLotId }
    val maxQuantity = selectedLot?.quantity ?: 0
    val scrollState = rememberScrollState()

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
                    modifier =
                            Modifier.fillMaxSize()
                                    .verticalScroll(scrollState)
                                    .padding(horizontal = 16.dp)
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                DetailSummaryCard(product = product)

                Spacer(modifier = Modifier.height(16.dp))

                InventoryCard(product)

                Spacer(modifier = Modifier.height(12.dp))

                StockSelectorCard(
                        warehouses = warehouseOptions,
                        selectedWarehouseId = selectedWarehouseId,
                        onWarehouseSelected = { id ->
                            selectedWarehouseId = id
                            val warehouse = warehouseOptions.firstOrNull { it.id == id }
                            selectedLotId = warehouse?.batches?.firstOrNull()?.id
                            quantity = 0
                        },
                        selectedLotId = selectedLotId,
                        onLotSelected = { lotId ->
                            selectedLotId = lotId
                            quantity = 0
                        }
                )

                Spacer(modifier = Modifier.height(12.dp))

                PricingCard(product)

                Spacer(modifier = Modifier.height(24.dp))

                QuantitySection(
                        quantity = quantity,
                        maxQuantity = maxQuantity,
                        onIncrement = {
                            if (selectedLot != null && quantity < maxQuantity) {
                                quantity += 1
                            }
                        },
                        onDecrement = { quantity = (quantity - 1).coerceAtLeast(0) },
                        canConfirm = selectedLot != null && selectedWarehouse != null && quantity in 1..maxQuantity
                ) {
                    val warehouse = selectedWarehouse
                    val lot = selectedLot
                    if (warehouse != null && lot != null && quantity in 1..maxQuantity) {
                        onQuantityConfirmed(
                                quantity,
                                warehouse.id,
                                warehouse.name,
                                lot.id,
                                lot.id
                        )
                    }
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
private fun StockSelectorCard(
        warehouses: List<ProductWarehouse>,
        selectedWarehouseId: String?,
        onWarehouseSelected: (String) -> Unit,
        selectedLotId: String?,
        onLotSelected: (String) -> Unit
) {
    Card(
            modifier = Modifier.fillMaxWidth(),
            colors =
                    CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                    ),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text(
                    text = "Selecciona inventario",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
            )

            if (warehouses.isEmpty()) {
                Text(
                        text = "Este producto no cuenta con stock disponible.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                var warehouseExpanded by remember { mutableStateOf(false) }
                val currentWarehouse = warehouses.firstOrNull { it.id == selectedWarehouseId } ?: warehouses.first()
                var lotExpanded by remember(currentWarehouse.id) { mutableStateOf(false) }
                val lots = currentWarehouse.batches
                val currentLot =
                        lots.firstOrNull { it.id == selectedLotId }
                                ?: lots.firstOrNull()

                Box {
                    SelectorField(
                            label = "Bodega",
                            value = currentWarehouse.name,
                            onClick = { warehouseExpanded = true }
                    )
                    DropdownMenu(expanded = warehouseExpanded, onDismissRequest = { warehouseExpanded = false }) {
                        warehouses.forEach { warehouse ->
                            DropdownMenuItem(
                                    text = { Text(warehouse.name) },
                                    onClick = {
                                        warehouseExpanded = false
                                        onWarehouseSelected(warehouse.id)
                                        lotExpanded = false
                                    }
                            )
                        }
                    }
                }

                Box {
                    SelectorField(
                            label = "Lote",
                            value = currentLot?.id ?: "Sin lotes",
                            enabled = lots.isNotEmpty(),
                            onClick = { if (lots.isNotEmpty()) lotExpanded = true }
                    )
                    DropdownMenu(expanded = lotExpanded, onDismissRequest = { lotExpanded = false }) {
                        lots.forEach { lot ->
                            DropdownMenuItem(
                                    text = { Text("${lot.id} • ${lot.quantity} u") },
                                    onClick = {
                                        lotExpanded = false
                                        onLotSelected(lot.id)
                                    }
                            )
                        }
                    }
                }

                Text(
                        text = "Disponible en lote: ${currentLot?.quantity ?: 0} unidades",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
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
        maxQuantity: Int,
        onIncrement: () -> Unit,
        onDecrement: () -> Unit,
        canConfirm: Boolean,
        onConfirm: () -> Unit
) {
    Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Button(onClick = onConfirm, enabled = canConfirm) {
            Text("Agregar")
        }

        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            QuantityButton(symbol = "-", enabled = quantity > 0, onClick = onDecrement)
            Text(
                    text = quantity.toString(),
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurface
            )
            val incrementEnabled = maxQuantity > 0 && quantity < maxQuantity
            QuantityButton(symbol = "+", enabled = incrementEnabled, onClick = onIncrement)
        }
    }
    val availabilityText =
            if (maxQuantity > 0) "Máximo disponible: $maxQuantity"
            else "Sin stock disponible para el lote seleccionado"
    Text(
            text = availabilityText,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 8.dp)
    )
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

@Composable
private fun SelectorField(
        label: String,
        value: String,
        enabled: Boolean = true,
        onClick: () -> Unit
) {
    Column {
        Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Surface(
                modifier =
                        Modifier.fillMaxWidth()
                                .padding(top = 4.dp)
                                .clip(MaterialTheme.shapes.medium)
                                .clickable(enabled = enabled, onClick = onClick),
                color =
                        if (enabled) MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
                        else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
        ) {
            Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                        text = value,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                )
                Icon(
                        imageVector = Icons.Outlined.ChevronRight,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
