package com.example.mobile_medisupply.features.home.data.mappers

import com.example.mobile_medisupply.features.home.data.remote.VisitItem
import com.example.mobile_medisupply.features.home.domain.model.ScheduledVisit

fun VisitItem.toDomain(): ScheduledVisit {
    return ScheduledVisit(
        id = visitaId,
        clientId = clienteId,
        clientName = nombreCliente,
        scheduledDate = fechaVisita,
        location = direccion,
    )
}


fun List<VisitItem>.toDomainList(): List<ScheduledVisit> = map { it.toDomain() }
