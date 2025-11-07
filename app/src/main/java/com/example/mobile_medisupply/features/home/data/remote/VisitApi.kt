package com.example.mobile_medisupply.features.home.data.remote

import com.example.mobile_medisupply.core.network.ApiResponse
import okhttp3.MultipartBody
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface VisitApi {
    @GET("visitas/ruta")
    suspend fun obtenerVisitasDelDia(
        @Query("fecha") fecha: String? = null
    ): ApiResponse<VisitResult>

    @GET("visitas/{visitaId}/detalle")
    suspend fun obtenerDetalleVisita(
        @Path("visitaId") visitaId: String
    ): ApiResponse<VisitDetailDto>

    @Multipart
    @POST("visitas/{visitaId}/cargar-video")
    suspend fun subirVideoVisita(
        @Path("visitaId") visitaId: String,
        @Part video: MultipartBody.Part
    ): ApiResponse<Unit>
}

data class VisitDetailDto(
    val id: String,
    val createdAt: String,
    val updatedAt: String,
    val estado: String,
    val cliente: ClienteDetalle,
    val vendedorId: String,
    val fechaVisita: String,
    val comentarios: String?
)

data class VisitResult(
    val fecha: String,
    val totalVisitas: Int,
    val visitas: List<VisitItem>
)

data class ClienteDetalle(
    val id: String,
    val nombre: String,
    val ubicacion: Ubicacion?
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