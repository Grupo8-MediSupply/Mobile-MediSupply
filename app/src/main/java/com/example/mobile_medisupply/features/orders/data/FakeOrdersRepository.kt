package com.example.mobile_medisupply.features.orders.data

import com.example.mobile_medisupply.features.orders.domain.model.OrderStatus
import com.example.mobile_medisupply.features.orders.domain.model.OrderSummary
import com.example.mobile_medisupply.features.orders.domain.repository.OrdersRepository

class FakeOrdersRepository : OrdersRepository {

    private val orders =
            listOf(
                    OrderSummary(
                            id = "order-1001",
                            orderNumber = "1001",
                            clientName = "Clínica Santa María",
                            status = OrderStatus.PROCESSED,
                            createdAt = "12 Mar 2024"
                    ),
                    OrderSummary(
                            id = "order-1002",
                            orderNumber = "1002",
                            clientName = "Hospital Vida Plena",
                            status = OrderStatus.PROCESSED,
                            createdAt = "10 Mar 2024"
                    ),
                    OrderSummary(
                            id = "order-1003",
                            orderNumber = "1003",
                            clientName = "Centro Médico Los Álamos",
                            status = OrderStatus.PROCESSED,
                            createdAt = "08 Mar 2024"
                    ),
                    OrderSummary(
                            id = "order-1004",
                            orderNumber = "1004",
                            clientName = "Salud Integral Norte",
                            status = OrderStatus.PROCESSED,
                            createdAt = "05 Mar 2024"
                    ),
                    OrderSummary(
                            id = "order-1005",
                            orderNumber = "1005",
                            clientName = "Hospital Ángeles del Valle",
                            status = OrderStatus.PROCESSED,
                            createdAt = "02 Mar 2024"
                    ),
                    OrderSummary(
                            id = "order-1006",
                            orderNumber = "1006",
                            clientName = "Clínica Esperanza Azul",
                            status = OrderStatus.PROCESSED,
                            createdAt = "28 Feb 2024"
                    )
            )

    override fun getProcessedOrders(): List<OrderSummary> =
            orders.filter { it.status == OrderStatus.PROCESSED }
}
