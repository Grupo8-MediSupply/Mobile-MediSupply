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
