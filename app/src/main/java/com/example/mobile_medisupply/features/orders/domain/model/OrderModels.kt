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
        val description: String,
        val presentation: String,
        val unit: String,
        val category: String,
        val provider: String,
        val country: String,
        val guidelineLabel: String?,
        val inventory: ProductInventory,
        val pricing: ProductPricing
)

data class ProductInventory(
        val total: Int,
        val reserved: Int,
        val available: Int,
        val warehouse: String
)

data class ProductPricing(
        val currency: String,
        val price: Double,
        val formattedPrice: String,
        val lastUpdated: String
)
