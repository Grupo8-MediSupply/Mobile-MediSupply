package com.example.mobile_medisupply.features.config.domain.model

data class AppConfig(
        val country: ConfigCountry,
        val identificationTypes: List<IdentificationType>
)

data class ConfigCountry(
        val id: String,
        val isoCode: String,
        val name: String,
        val currencyName: String,
        val currencySymbol: String,
        val timezone: String,
        val officialLanguage: String,
        val healthRegulator: String,
        val currencyCode: String
)

data class IdentificationType(
        val id: String,
        val name: String,
        val abbreviation: String,
        val countryId: String
)
