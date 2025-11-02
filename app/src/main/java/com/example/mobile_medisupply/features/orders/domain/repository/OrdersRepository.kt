package com.example.mobile_medisupply.features.orders.domain.repository

import com.example.mobile_medisupply.features.orders.domain.model.OrderSummary

interface OrdersRepository {
    fun getProcessedOrders(): List<OrderSummary>
}
