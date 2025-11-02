package com.example.mobile_medisupply.features.config.data.remote

import com.google.gson.annotations.SerializedName

data class ConfigResponseDto(
        @SerializedName("success") val success: Boolean,
        @SerializedName("result") val result: ConfigResultDto?,
        @SerializedName("timestamp") val timestamp: String?
)

data class ConfigResultDto(
        @SerializedName("pais") val country: ConfigCountryDto,
        @SerializedName("tiposIdentificacion")
        val identificationTypes: List<IdentificationTypeDto>
)

data class ConfigCountryDto(
        @SerializedName("id") val id: String,
        @SerializedName("codigoIso") val isoCode: String,
        @SerializedName("nombre") val name: String,
        @SerializedName("moneda") val currencyName: String,
        @SerializedName("simboloMoneda") val currencySymbol: String,
        @SerializedName("zonaHoraria") val timezone: String,
        @SerializedName("idiomaOficial") val officialLanguage: String,
        @SerializedName("reguladorSanitario") val healthRegulator: String,
        @SerializedName("sigla_moneda") val currencyCode: String
)

data class IdentificationTypeDto(
        @SerializedName("id") val id: String,
        @SerializedName("nombre") val name: String,
        @SerializedName("abreviatura") val abbreviation: String,
        @SerializedName("paisId") val countryId: String
)
