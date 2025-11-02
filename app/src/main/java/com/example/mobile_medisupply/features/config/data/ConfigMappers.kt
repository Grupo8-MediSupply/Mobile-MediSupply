package com.example.mobile_medisupply.features.config.data

import com.example.mobile_medisupply.features.config.data.remote.ConfigCountryDto
import com.example.mobile_medisupply.features.config.data.remote.ConfigResponseDto
import com.example.mobile_medisupply.features.config.data.remote.IdentificationTypeDto
import com.example.mobile_medisupply.features.config.domain.model.AppConfig
import com.example.mobile_medisupply.features.config.domain.model.ConfigCountry
import com.example.mobile_medisupply.features.config.domain.model.IdentificationType

fun ConfigResponseDto.toDomain(): AppConfig? {
    val result = result ?: return null
    return AppConfig(
            country = result.country.toDomain(),
            identificationTypes = result.identificationTypes.map { it.toDomain() }
    )
}

private fun ConfigCountryDto.toDomain(): ConfigCountry =
        ConfigCountry(
                id = id,
                isoCode = isoCode,
                name = name,
                currencyName = currencyName,
                currencySymbol = currencySymbol,
                timezone = timezone,
                officialLanguage = officialLanguage,
                healthRegulator = healthRegulator,
                currencyCode = currencyCode
        )

private fun IdentificationTypeDto.toDomain(): IdentificationType =
        IdentificationType(
                id = id,
                name = name,
                abbreviation = abbreviation,
                countryId = countryId
        )
