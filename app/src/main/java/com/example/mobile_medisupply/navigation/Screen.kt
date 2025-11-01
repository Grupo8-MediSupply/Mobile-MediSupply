package com.example.mobile_medisupply.navigation

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Register : Screen("register")
    object Recover : Screen("recover")
    object Home : Screen("visitas")
    object Inventory : Screen("ordenes")
    object CreateOrder : Screen("ordenes/nueva")
    object Clients : Screen("clientes")
    object ClientDetail : Screen("clientes/detail/{clientId}") {
        private const val PARAM = "clientId"
        fun createRoute(clientId: String) = "clientes/detail/$clientId"
    }
}
