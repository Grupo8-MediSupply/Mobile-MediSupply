package com.example.mobile_medisupply.features.clients.domain.model

data class ClientSummary(
        val id: String,
        val name: String,
        val contact: String,
        val location: String,
        val phone: String,
        val institutionType: String
)

data class ClientOrderSummary(
        val id: String,
        val title: String,
        val description: String
)

data class ClientDetail(
        val id: String,
        val name: String,
        val city: String,
        val phone: String,
        val institutionType: String,
        val contact: String,
        val orders: List<ClientOrderSummary>
)
