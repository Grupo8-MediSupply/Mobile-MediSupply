package com.example.mobile_medisupply.navigation

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Register : Screen("register")
    object Recover : Screen("recover")
    object Home : Screen("visitas")
    object Inventory : Screen("ordenes")
    object Profile : Screen("clientes")

    // Para pantallas con parámetros
    object Detail : Screen("detail/{itemId}") {
        fun createRoute(itemId: String) = "detail/$itemId"
    }
}
