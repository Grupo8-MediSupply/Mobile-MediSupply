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
    NavHost(
            navController = navController,
            startDestination = Screen.Login.route,
            modifier = modifier
    ) {
        // Pantalla de Login
        composable(Screen.Login.route) {
            LoginScreen(
                    onLoginSuccess = {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    },
                    onForgotPasswordClick = { navController.navigate(Screen.Recover.route) },
                    onRegisterClick = { navController.navigate(Screen.Register.route) }
            )
        }

        // Pantalla de Registro
        composable(Screen.Register.route) {
            RegisterScreen(
                    onRegisterClick = {
                            companyName,
                            taxId,
                            institutionType,
                            personInCharge,
                            email,
                            password ->
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    },
                    onLoginClick = { navController.navigateUp() }
            )
        }

        // Pantalla de Home
        composable(Screen.Home.route) {
            HomeScreen(
                    onNavigateToInventory = { navController.navigate(Screen.Inventory.route) },
                    onNavigateToProfile = { navController.navigate(Screen.Profile.route) }
            )
        }

        // Pantalla de Recuperación (placeholder)
        composable(Screen.Recover.route) { Text("Recover Password Screen") }

        // Pantalla de Inventario (placeholder)
        composable(Screen.Inventory.route) { Text("Ordenes") }

        // Pantalla de Perfil (placeholder)
        composable(Screen.Profile.route) { Text("Clientes") }

    }
}
