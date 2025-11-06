package com.example.mobile_medisupply.features.home.data.remote

import com.example.mobile_medisupply.core.network.ApiResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface VisitApi {
    @GET("visitas/ruta")
    suspend fun obtenerVisitasDelDia(
        @Query("fecha") fecha: String? = null
    ): ApiResponse<VisitResult>
}

data class VisitResult(
    val fecha: String,
    val totalVisitas: Int,
    val visitas: List<VisitItem>
)

data class VisitItem(
    val visitaId: String,
    val clienteId: String,
    val nombreCliente: String,
    val fechaVisita: String,
    val estado: String,
    val ubicacion: Ubicacion?,
    val direccion: String?
)

data class Ubicacion(
    val lat: Double,
    val lng: Double
)