package com.example.mobile_medisupply.features.auth.domain.model

enum class Region(val id: Int, val countryName: String) {
    COLOMBIA(10, "Colombia"),
    MEXICO(20, "México");

    companion object {
        fun fromId(id: Int): Region =
                values().find { it.id == id }
                        ?: throw IllegalArgumentException("Invalid region id: $id")
    }
}
