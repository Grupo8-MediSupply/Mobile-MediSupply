package com.example.mobile_medisupply.features.home.domain.model

data class CreateVisitRequest(
        val clientId: String,
        val clientName: String,
        val address: String,
        val scheduledDate: String
)
