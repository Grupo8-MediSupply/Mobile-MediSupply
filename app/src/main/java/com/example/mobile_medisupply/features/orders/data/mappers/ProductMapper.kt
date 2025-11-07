package com.example.mobile_medisupply.features.orders.data.mappers

import com.example.mobile_medisupply.features.orders.data.remote.ProductDetailResult
import com.example.mobile_medisupply.features.orders.data.remote.ProductDto
import com.example.mobile_medisupply.features.orders.domain.model.ProductCatalogItem
import com.example.mobile_medisupply.features.orders.domain.model.ProductInventory
import com.example.mobile_medisupply.features.orders.domain.model.ProductPricing
import com.example.mobile_medisupply.features.orders.domain.model.ProductSummary
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

fun ProductDetailResult.toDomain(): ProductCatalogItem {
    val totalCantidad = bodegas.orEmpty().sumOf { bodega ->
        bodega.lotes.orEmpty().sumOf { it.cantidad }
    }

    val firstWarehouseLabel =
            bodegas.orEmpty()
                    .takeIf { it.isNotEmpty() }
                    ?.joinToString { it.bodegaNombre }
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
