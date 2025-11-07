package com.example.mobile_medisupply.features.home.data.mappers

import com.example.mobile_medisupply.features.home.data.remote.ClienteDetalle
import com.example.mobile_medisupply.features.home.data.remote.Ubicacion
import com.example.mobile_medisupply.features.home.data.remote.VisitDetailDto
import com.example.mobile_medisupply.features.home.data.remote.VisitItem
import com.example.mobile_medisupply.features.home.domain.model.ClienteDetail
import com.example.mobile_medisupply.features.home.domain.model.ScheduledVisit
import com.example.mobile_medisupply.features.home.domain.model.VisitDetail

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

fun ClienteDetalle.toDomain(): ClienteDetail = ClienteDetail(
    id = id,
    name = nombre,
    Location = ubicacion ?: Ubicacion(lat = 0.0, lng = 0.0)
)
fun VisitDetailDto.toDomain(): VisitDetail = VisitDetail(
    id = id,
    client = cliente.toDomain(),
    vendorId = vendedorId,
    status = estado,
    date = fechaVisita,
    comments = comentarios
)

