package com.example.mobile_medisupply.features.config.data.remote

import retrofit2.http.GET
import retrofit2.http.Header

interface ConfigApi {
    @GET("configuracion")
    suspend fun getConfiguration(
            @Header("Authorization") authorization: String
    ): ConfigResponseDto
}
