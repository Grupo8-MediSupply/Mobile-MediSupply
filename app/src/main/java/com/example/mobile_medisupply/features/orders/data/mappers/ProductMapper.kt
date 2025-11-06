package com.example.mobile_medisupply.features.orders.data.mappers

import com.example.mobile_medisupply.features.home.data.mappers.toDomain
import com.example.mobile_medisupply.features.home.data.remote.VisitItem
import com.example.mobile_medisupply.features.home.domain.model.ScheduledVisit
import com.example.mobile_medisupply.features.orders.data.remote.ProductDetailResult
import com.example.mobile_medisupply.features.orders.domain.model.ProductCatalogItem
import com.example.mobile_medisupply.features.orders.domain.model.ProductInventory
import com.example.mobile_medisupply.features.orders.domain.model.ProductPricing
import java.text.NumberFormat
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

fun ProductDetailResult.toDomain(): ProductCatalogItem {
    val totalCantidad = bodegas.sumOf { bodega ->
        bodega.lotes.sumOf { it.cantidad }
    }

    val currency = "COP"
    val formatter = NumberFormat.getCurrencyInstance(Locale("es", "CO"))
    val formattedPrice = formatter.format(precio)

    val now = DateTimeFormatter.ISO_INSTANT.format(Instant.now().atZone(ZoneId.of("UTC")))

    return ProductCatalogItem(
        id = producto_info.id,
        name = producto_info.nombre,
        description = producto_info.descripcion,
        presentation = producto_info.concentracion,
        unit = "Unidad",
        category = tipo,
        provider = proveedor.nombre,
        country = proveedor.pais,
        guidelineLabel = null,
        inventory = ProductInventory(
            total = totalCantidad,
            reserved = 0,
            available = totalCantidad,
            warehouse = bodegas.joinToString { it.bodegaNombre }
        ),
        pricing = ProductPricing(
            currency = currency,
            price = precio,
            formattedPrice = formattedPrice,
            lastUpdated = now
        )
    )
}

fun List<ProductDetailResult>.toDomainList(): List<ProductCatalogItem> = map { it.toDomain() }
