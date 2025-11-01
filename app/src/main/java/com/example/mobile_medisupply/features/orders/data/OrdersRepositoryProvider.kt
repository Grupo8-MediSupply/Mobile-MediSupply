package com.example.mobile_medisupply.features.orders.data

import com.example.mobile_medisupply.features.orders.domain.repository.OrdersRepository

object OrdersRepositoryProvider {
    val repository: OrdersRepository by lazy { FakeOrdersRepository() }
}
