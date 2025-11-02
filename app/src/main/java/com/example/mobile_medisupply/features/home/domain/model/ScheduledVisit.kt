package com.example.mobile_medisupply.features.home.domain.model

data class ScheduledVisit(
        val id: String,
        val clientId: String,
        val clientName: String,
        val scheduledDate: String,
        val location: String? = null,
        val notes: String? = null
)
