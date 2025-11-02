package com.example.mobile_medisupply.navigation

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Register : Screen("register")
    object Recover : Screen("recover")
    object Home : Screen("visitas")
    object CreateVisit : Screen("visitas/nueva")
    object Inventory : Screen("ordenes")
    object CreateOrder : Screen("ordenes/nueva")
    object ProductDetail : Screen("ordenes/producto/{productId}") {
        private const val PARAM = "productId"
        fun createRoute(productId: String) = "ordenes/producto/$productId"
    }
    object OrderSummary : Screen("ordenes/resumen")
    object Clients : Screen("clientes")
    object ClientDetail : Screen("clientes/detail/{clientId}") {
        private const val PARAM = "clientId"
        fun createRoute(clientId: String) = "clientes/detail/$clientId"
    }
    object VisitDetail : Screen("clientes/visita/{clientId}/{visitId}") {
        fun createRoute(clientId: String, visitId: String) = "clientes/visita/$clientId/$visitId"
    }
}
