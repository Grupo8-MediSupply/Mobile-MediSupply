package com.example.mobile_medisupply.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.mobile_medisupply.features.auth.presentation.login.LoginScreen
import com.example.mobile_medisupply.features.auth.presentation.register.RegisterScreen
import com.example.mobile_medisupply.features.home.presentation.HomeScreen

@Composable
fun AppNavHost(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(navController = navController, startDestination = "login", modifier = modifier) {

        // Pantalla de Login
        composable("login") {
            LoginScreen(
                    onLoginClick = { email, password ->
                        // Navegar al home en lugar de dashboard
                        navController.navigate("home") { popUpTo("login") { inclusive = true } }
                    },
                    onForgotPasswordClick = { navController.navigate("recover") },
                    onRegisterClick = {
                        navController.navigate("register")
                    } // Nuevo navegación a registro
            )
        }

        // Pantalla de Registro
        composable("register") {
            RegisterScreen(
                    onRegisterClick = {
                            companyName,
                            taxId,
                            institutionType,
                            personInCharge,
                            email,
                            password ->
                        // Después del registro exitoso, navegar al home
                        navController.navigate("home") { popUpTo("login") { inclusive = true } }
                    },
                    onLoginClick = {
                        navController.navigateUp() // Regresar a login
                    }
            )
        }

        // Pantalla de Home
        composable("home") {
            HomeScreen(
                    onNavigateToInventory = { navController.navigate("inventory") },
                    onNavigateToProfile = { navController.navigate("profile") }
            )
        }

        // Pantalla de Dashboard (placeholder) - mantengamos por ahora
        composable("dashboard") { Text("Dashboard - Login exitoso!") }

        // Pantalla de Recuperación (placeholder)
        composable("recover") { Text("Recover Password Screen") }

        // Placeholders para nuevas rutas
        composable("inventory") { Text("Inventario - En desarrollo") }
        composable("profile") { Text("Perfil - En desarrollo") }
    }
}
