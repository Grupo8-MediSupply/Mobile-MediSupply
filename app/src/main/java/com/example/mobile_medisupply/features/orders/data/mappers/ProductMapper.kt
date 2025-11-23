package com.example.mobile_medisupply.features.orders.data.mappers

import com.example.mobile_medisupply.features.orders.data.remote.ProductDetailResult
import com.example.mobile_medisupply.features.orders.data.remote.ProductDto
import com.example.mobile_medisupply.features.orders.domain.model.ProductCatalogItem
import com.example.mobile_medisupply.features.orders.domain.model.ProductInventory
import com.example.mobile_medisupply.features.orders.domain.model.ProductPricing
import com.example.mobile_medisupply.features.orders.domain.model.ProductSummary
import com.example.mobile_medisupply.features.orders.domain.model.ProductWarehouse
import com.example.mobile_medisupply.features.orders.domain.model.ProductBatch
import java.text.NumberFormat
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

private val pesoFormatter: NumberFormat =
        NumberFormat.getCurrencyInstance(Locale("es", "CO")).apply {
            maximumFractionDigits = 0
        }

fun ProductDto.toSummary(): ProductSummary {
    val priceAsDouble = precio.toDouble()
    return ProductSummary(
            id = productoRegionalId,
            sku = sku,
            name = nombre,
            description = descripcion,
            price = priceAsDouble,
            formattedPrice = pesoFormatter.format(priceAsDouble)
    )
}

private fun com.example.mobile_medisupply.features.orders.data.remote.Batch.toDomain(): ProductBatch =
        ProductBatch(id = loteId, quantity = cantidad)

private fun com.example.mobile_medisupply.features.orders.data.remote.Warehouse.toDomain(): ProductWarehouse =
        ProductWarehouse(
                id = bodegaId,
                name = bodegaNombre,
                batches = lotes.orEmpty().map { it.toDomain() }
        )

fun ProductDetailResult.toDomain(): ProductCatalogItem {
    val warehouses = bodegas.orEmpty().map { it.toDomain() }
    val totalCantidad = warehouses.sumOf { warehouse ->
        warehouse.batches.sumOf { it.quantity }
    }

    val firstWarehouseLabel =
            warehouses
                    .takeIf { it.isNotEmpty() }
                    ?.joinToString { it.name }
                    ?: "Sin inventario registrado"

    val now = DateTimeFormatter.ISO_INSTANT.format(Instant.now().atZone(ZoneId.of("UTC")))
    val priceAsDouble = precio

    return ProductCatalogItem(
            id = producto_info.id,
            name = producto_info.nombre,
            description = producto_info.descripcion,
            presentation = resolvePresentation(producto_info, tipo),
            unit = "Unidad",
            category = tipo,
            provider = proveedor?.nombre ?: "Sin proveedor",
            country = proveedor?.pais ?: "-",
            guidelineLabel = null,
            warehouses = warehouses,
            inventory = ProductInventory(
                    total = totalCantidad,
                    reserved = 0,
                    available = totalCantidad,
                    warehouse = firstWarehouseLabel
            ),
            pricing = ProductPricing(
                    currency = "COP",
                    price = priceAsDouble,
                    formattedPrice = pesoFormatter.format(priceAsDouble),
                    lastUpdated = now
            )
    )
}

private fun resolvePresentation(
        info: com.example.mobile_medisupply.features.orders.data.remote.ProductInfo,
        tipo: String
): String {
    return when (tipo.uppercase(Locale.getDefault())) {
        "MEDICAMENTO" -> listOfNotNull(info.concentracion).joinToString(" • ")
        "EQUIPO" -> listOfNotNull(info.marca, info.modelo).joinToString(" • ")
        "INSUMO" -> buildString {
            info.material?.let { append(it) }
            info.esteril?.let { isSterile ->
                if (isNotEmpty()) append(" • ")
                append(if (isSterile) "Esteril" else "No esteril")
            }
        }.ifBlank { "General" }
        else -> info.sku
    }.ifBlank { info.sku }
}

fun List<ProductDetailResult>.toDomainList(): List<ProductCatalogItem> = map { it.toDomain() }

fun com.example.mobile_medisupply.features.orders.data.remote.OrderHistoryDto.toDomain(): com.example.mobile_medisupply.features.orders.domain.model.OrderSummary {
    // Parse created_at date
    val formatter = DateTimeFormatter.ISO_DATE_TIME
    val instant = Instant.from(formatter.parse(created_at))
    val localDate = instant.atZone(ZoneId.systemDefault()).toLocalDate()
    val displayDateFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy", Locale("es", "CO"))
    val formattedDate = localDate.format(displayDateFormatter)
    
    return com.example.mobile_medisupply.features.orders.domain.model.OrderSummary(
        id = id,
        orderNumber = id.take(8).uppercase(), // Use first 8 chars of ID as order number
        clientName = "", // Not provided in DTO for client orders
        status = com.example.mobile_medisupply.features.orders.domain.model.OrderStatus.fromApiValue(estado),
        createdAt = formattedDate,
        total = total,
        formattedTotal = pesoFormatter.format(total)
    )
}
