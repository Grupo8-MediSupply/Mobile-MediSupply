package com.example.mobile_medisupply.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.mobile_medisupply.features.auth.presentation.login.LoginScreen

@Composable
fun AppNavHost(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(navController = navController, startDestination = "login", modifier = modifier) {

        // Pantalla de Login
        composable("login") {
            LoginScreen(
                    onLoginClick = { email, password ->
                        // Por ahora solo navegar a dashboard
                        navController.navigate("dashboard") {
                            popUpTo("login") { inclusive = true }
                        }
                    },
                    onForgotPasswordClick = { navController.navigate("recover") }
            )
        }

        // Pantalla de Dashboard (placeholder)
        composable("dashboard") { Text("Dashboard - Login exitoso!") }

        // Pantalla de Recuperación (placeholder)
        composable("recover") { Text("Recover Password Screen") }
    }
}
