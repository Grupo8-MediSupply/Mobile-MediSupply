package com.example.mobile_medisupply.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.mobile_medisupply.features.auth.data.repository.UserSession
import com.example.mobile_medisupply.features.auth.domain.model.UserRole
import com.example.mobile_medisupply.features.auth.presentation.login.LoginScreen
import com.example.mobile_medisupply.features.auth.presentation.register.RegisterScreen
import com.example.mobile_medisupply.features.clients.data.ClientRepositoryProvider
import com.example.mobile_medisupply.features.clients.presentation.ClientDetailScreen
import com.example.mobile_medisupply.features.clients.presentation.ClientsScreen
import com.example.mobile_medisupply.features.home.presentation.HomeScreen
import com.example.mobile_medisupply.features.orders.presentation.CreateOrderScreen
import com.example.mobile_medisupply.features.orders.presentation.OrdersScreen

@Composable
fun AppNavHost(
        navController: NavHostController,
        canViewClients: Boolean,
        onLoginSuccess: () -> Unit,
        session: UserSession?,
        modifier: Modifier = Modifier
) {
    NavHost(
            navController = navController,
            startDestination = Screen.Login.route,
            modifier = modifier
    ) {
        // Pantalla de Login
        composable(Screen.Login.route) {
            LoginScreen(
                    onLoginSuccess = {
                        onLoginSuccess()
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
                    onNavigateToClients = { navController.navigate(Screen.Clients.route) }
            )
        }

        // Pantalla de Recuperación (placeholder)
        composable(Screen.Recover.route) { Text("Recover Password Screen") }

        // Pantalla de Órdenes
        composable(Screen.Inventory.route) {
            OrdersScreen(onCreateOrderClick = { navController.navigate(Screen.CreateOrder.route) })
        }

        composable(Screen.CreateOrder.route) {
            CreateOrderScreen(
                    userRole = session?.role ?: UserRole.VENDEDOR,
                    sessionClientId = session?.userId,
                    onBackClick = { navController.navigateUp() },
                    onOrderSubmit = { navController.navigateUp() }
            )
        }

        // Pantalla de Clientes
        composable(Screen.Clients.route) {
            if (canViewClients) {
                ClientsScreen(
                        onClientSelected = { client ->
                            navController.navigate(Screen.ClientDetail.createRoute(client.id))
                    }
                )
            } else {
                Surface {
                    Text(
                            text = "No tienes permisos para ver esta sección.",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.error,
                            textAlign = TextAlign.Center,
                            modifier =
                                    Modifier.fillMaxSize().padding(horizontal = 24.dp)
                                            .wrapContentSize(Alignment.Center)
                    )
                }
            }
        }

        composable(
                route = Screen.ClientDetail.route,
                arguments = listOf(navArgument("clientId") { type = NavType.StringType })
        ) { backStackEntry ->
            if (!canViewClients) {
                Surface {
                    Text(
                            text = "No tienes permisos para ver esta sección.",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.error,
                            textAlign = TextAlign.Center,
                            modifier =
                                    Modifier.fillMaxSize().padding(horizontal = 24.dp)
                                            .wrapContentSize(Alignment.Center)
                    )
                }
            } else {
                val clientId = backStackEntry.arguments?.getString("clientId")
                val detail =
                        clientId?.let { ClientRepositoryProvider.repository.getClientDetail(it) }
                if (detail != null) {
                    ClientDetailScreen(
                            clientDetail = detail,
                            onBackClick = { navController.navigateUp() }
                    )
                } else {
                    Surface {
                        Text(
                                text = "No encontramos la información del cliente.",
                                style = MaterialTheme.typography.bodyLarge,
                                textAlign = TextAlign.Center,
                                modifier =
                                        Modifier.fillMaxSize().padding(horizontal = 24.dp)
                                                .wrapContentSize(Alignment.Center)
                        )
                    }
                }
            }
        }

    }
}
