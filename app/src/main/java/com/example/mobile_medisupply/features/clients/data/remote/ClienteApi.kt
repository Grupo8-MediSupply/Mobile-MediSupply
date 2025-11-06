package com.example.mobile_medisupply.features.clients.data.remote

import com.example.mobile_medisupply.core.network.ApiResponse
import com.example.mobile_medisupply.features.clients.domain.model.ClientSummary
import retrofit2.http.GET

interface ClienteApi {
    @GET("clientes")
    suspend fun obtenerClientes(): ApiResponse<List<ClienteResumen>>
}

data class ClienteResumen(
    val id: String,
    val nombre: String,
    val tipoInstitucion: String,
    val clasificacion: String,
    val responsableContacto: String
)


fun ClienteResumen.toDomain(): ClientSummary = ClientSummary(
    id = id,
    name = nombre,
    institutionType = tipoInstitucion,
    location = clasificacion,
    contact = responsableContacto,
    phone = responsableContacto
)
