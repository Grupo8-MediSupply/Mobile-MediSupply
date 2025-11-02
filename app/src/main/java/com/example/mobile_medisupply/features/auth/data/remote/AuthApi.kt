package com.example.mobile_medisupply.features.auth.data.remote

import com.example.mobile_medisupply.core.network.ApiResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST("login") suspend fun login(@Body request: LoginRequest): ApiResponse<LoginResult>
    @POST("clientes") suspend fun registrarCliente(@Body request: CrearClienteRequest): ApiResponse<CrearClienteResult>
}

data class CrearClienteRequest(
    val nombre: String,
    val identificacion: String,
    val tipoIdentificacion: Int,
    val tipoInstitucion: String,
    val clasificacion: String,
    val responsableContacto: String,
    val pais: Int,
    val email: String,
    val password: String
)

data class CrearClienteResult(
    val id: String,
    val nombre: String,
    val tipoInstitucion: String,
    val clasificacion: String,
    val responsableContacto: String
)
data class LoginRequest(val email: String, val password: String)


data class LoginResult(val access_token: String)
