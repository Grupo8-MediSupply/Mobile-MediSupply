package com.example.mobile_medisupply.features.auth.domain.model

enum class UserRole(val id: Int) {
    ADMIN(1),
    VENDEDOR(20),
    CLIENTE(30),
    PROVEEDOR(40);

    companion object {
        fun fromId(id: Int): UserRole =
                values().find { it.id == id }
                        ?: throw IllegalArgumentException("Invalid role id: $id")
    }
}
