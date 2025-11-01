package com.example.mobile_medisupply.navigation

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Register : Screen("register")
    object Recover : Screen("recover")
    object Home : Screen("visitas")
    object Inventory : Screen("ordenes")
    object Clients : Screen("clientes")
}
