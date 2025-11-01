package com.example.mobile_medisupply.features.orders.domain.model

enum class OrderStatus(val displayName: String) {
    PROCESSED("Procesada"),
    IN_PROGRESS("En proceso"),
    PENDING("Pendiente")
}

data class OrderSummary(
        val id: String,
        val orderNumber: String,
        val clientName: String,
        val status: OrderStatus,
        val createdAt: String
)

data class ProductCatalogItem(
        val id: String,
        val name: String,
        val category: String,
        val description: String,
        val availability: List<ProductAvailability>
)

data class ProductAvailability(
        val warehouseId: String,
        val warehouseName: String,
        val lots: List<ProductLot>
)

data class ProductLot(
        val lotId: String,
        val lotCode: String,
        val expiresAt: String,
        val availableUnits: Int
)
