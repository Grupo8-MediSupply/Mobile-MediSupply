package com.example.mobile_medisupply.features.auth.data.remote

import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST("login") suspend fun login(@Body request: LoginRequest): LoginResponse
}

data class LoginRequest(val email: String, val password: String)

data class LoginResponse(val success: Boolean, val result: LoginResult, val timestamp: String)

data class LoginResult(val access_token: String)
