package com.example.mobile_medisupply.features.home.domain.model

import com.example.mobile_medisupply.features.home.data.remote.Ubicacion

data class ScheduledVisit(
        val id: String,
        val clientId: String,
        val clientName: String,
        val scheduledDate: String,
        val location: String? = null,
        val notes: String? = null
)


data class VisitDetail(
    val id: String,
    val client: ClienteDetail,
    val vendorId: String,
    val status: String,
    val date: String,
    val comments: String?
)

data class ClienteDetail(
    val id: String,
    val name: String,
    val Location: Ubicacion
)
