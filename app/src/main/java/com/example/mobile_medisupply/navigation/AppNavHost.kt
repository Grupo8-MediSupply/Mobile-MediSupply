package com.example.mobile_medisupply.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
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
import com.example.mobile_medisupply.features.clients.presentation.ClientsViewModel
import com.example.mobile_medisupply.features.clients.presentation.VisitDetailScreen
import com.example.mobile_medisupply.features.clients.presentation.VisitDetailViewModel
import com.example.mobile_medisupply.features.config.domain.model.AppConfig
import com.example.mobile_medisupply.features.home.presentation.CreateVisitScreen
import com.example.mobile_medisupply.features.home.presentation.HomeScreen
import com.example.mobile_medisupply.features.home.presentation.HomeViewModel
import com.example.mobile_medisupply.features.orders.data.remote.OrderCreatedResult
import com.example.mobile_medisupply.features.orders.domain.model.OrderSummaryItem
import com.example.mobile_medisupply.features.orders.presentation.CreateOrderScreen
import com.example.mobile_medisupply.features.orders.presentation.CreateOrderViewModel
import com.example.mobile_medisupply.features.orders.presentation.OrderSummaryScreen
import com.example.mobile_medisupply.features.orders.presentation.OrdersScreen
import com.example.mobile_medisupply.features.orders.presentation.OrdersViewModel
import com.example.mobile_medisupply.features.orders.presentation.ProductCatalogViewModel
import com.example.mobile_medisupply.features.orders.presentation.ProductDetailScreen
import com.example.mobile_medisupply.features.orders.presentation.ProductDetailViewModel
import java.text.NumberFormat
import java.util.Currency
import java.util.Locale

@Composable
fun AppNavHost(
        navController: NavHostController,
        canViewClients: Boolean,
        canViewVisits: Boolean,
        onLoginSuccess: () -> Unit,
        session: UserSession?,
        config: AppConfig?,
        modifier: Modifier = Modifier
) {
    val orderSelections = remember { mutableStateMapOf<String, OrderSummaryItem>() }
    val lastOrderResult = remember { mutableStateOf<OrderCreatedResult?>(null) }

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

        // Pantalla de Home (Visitas)
        composable(Screen.Home.route) {
            if (canViewVisits) {
                val viewModel: HomeViewModel = hiltViewModel()
                HomeScreen(
                        viewModel = viewModel,
                        onScheduleVisitClick = { navController.navigate(Screen.CreateVisit.route) },
                        onVisitClick = { visit ->
                            if (canViewClients) {
                                navController.navigate(
                                        Screen.VisitDetail.createRoute(visit.clientId, visit.id)
                                )
                            }
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
                                    Modifier.fillMaxSize()
                                            .padding(horizontal = 24.dp)
                                            .wrapContentSize(Alignment.Center)
                    )
                }
                LaunchedEffect(Unit) {
                    navController.navigate(Screen.Inventory.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                }
            }
        }

        // Pantalla de Recuperación (placeholder)
        composable(Screen.Recover.route) { Text("Recover Password Screen") }

        // Pantalla de Crear Visita
        composable(Screen.CreateVisit.route) {
            if (canViewVisits) {
                CreateVisitScreen(
                        clients = ClientRepositoryProvider.repository.getClients(),
                        onBackClick = { navController.navigateUp() },
                        onSubmit = { navController.popBackStack() }
                )
            } else {
                Surface {
                    Text(
                            text = "No tienes permisos para ver esta sección.",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.error,
                            textAlign = TextAlign.Center,
                            modifier =
                                    Modifier.fillMaxSize()
                                            .padding(horizontal = 24.dp)
                                            .wrapContentSize(Alignment.Center)
                    )
                }
                LaunchedEffect(Unit) {
                    navController.navigate(Screen.Inventory.route) {
                        popUpTo(Screen.CreateVisit.route) { inclusive = true }
                    }
                }
            }
        }

        // Pantalla de Órdenes
        composable(Screen.Inventory.route) {
            val userRole = session?.role ?: UserRole.VENDEDOR
            
            if (userRole == UserRole.CLIENTE) {
                // For clients, use ViewModel with real API data
                val ordersViewModel: OrdersViewModel = hiltViewModel()
                val ordersState by ordersViewModel.uiState.collectAsState()
                
                LaunchedEffect(Unit) {
                    ordersViewModel.loadClientOrders(state = "enviado", limit = 10)
                }
                
                OrdersScreen(
                    uiState = ordersState,
                    onCreateOrderClick = { navController.navigate(Screen.CreateOrder.route) },
                    onRetry = { ordersViewModel.loadClientOrders(state = "enviado", limit = 10) }
                )
            } else {
                // For sellers/admins, use fake data (for now)
                OrdersScreen(onCreateOrderClick = { navController.navigate(Screen.CreateOrder.route) })
            }
        }

        composable(Screen.CreateOrder.route) {
            val catalogViewModel: ProductCatalogViewModel = hiltViewModel()
            val catalogState by catalogViewModel.uiState.collectAsState()
            val createOrderViewModel: CreateOrderViewModel = hiltViewModel()
            val orderState by createOrderViewModel.uiState.collectAsState()

            LaunchedEffect(orderState.orderResult) {
                val result = orderState.orderResult
                if (result != null) {
                    lastOrderResult.value = result
                    createOrderViewModel.clearOrderResult()
                    // Navigate to OrderSummary and clear the back stack to CreateOrder
                    navController.navigate(Screen.OrderSummary.route) {
                        popUpTo(Screen.CreateOrder.route) { inclusive = true }
                    }
                }
            }

            CreateOrderScreen(
                    userRole = session?.role ?: UserRole.VENDEDOR,
                    sessionClientId = session?.userId,
                    selections = orderSelections,
                    catalogState = catalogState,
                    orderState = orderState,
                    onRefreshCatalog = { catalogViewModel.loadCatalog() },
                    onBackClick = { navController.navigateUp() },
                    onSubmitOrder = { clientId, items ->
                        createOrderViewModel.submitOrder(clientId, items)
                    },
                    onSubmitOrderByClient = { items ->
                        createOrderViewModel.submitOrderByClient(items)
                    },
                    onDismissError = { createOrderViewModel.clearError() },
                    onProductDetail = { productId ->
                        navController.navigate(Screen.ProductDetail.createRoute(productId))
                    }
            )
        }

        composable(
                route = Screen.ProductDetail.route,
                arguments = listOf(navArgument("productId") { type = NavType.StringType })
        ) { backStackEntry ->
            val productId = backStackEntry.arguments?.getString("productId")
            val viewModel: ProductDetailViewModel = hiltViewModel()
            if (productId == null) {
                Surface {
                    Text(
                            text = "No encontramos la información del producto.",
                            style = MaterialTheme.typography.bodyLarge,
                            textAlign = TextAlign.Center,
                            modifier =
                                    Modifier.fillMaxSize()
                                            .padding(horizontal = 24.dp)
                                            .wrapContentSize(Alignment.Center)
                    )
                }
            } else {
                LaunchedEffect(productId) { viewModel.loadProduct(productId) }
                val detailState by viewModel.uiState.collectAsState()
                when {
                    detailState.isLoading -> {
                        Surface {
                            CircularProgressIndicator(
                                    modifier =
                                            Modifier.fillMaxSize().wrapContentSize(Alignment.Center)
                            )
                        }
                    }
                    detailState.error != null -> {
                        val errorMessage = detailState.error ?: ""
                        Surface {
                            Column(
                                    modifier =
                                            Modifier.fillMaxSize()
                                                    .padding(horizontal = 24.dp)
                                                    .wrapContentSize(Alignment.Center),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Text(
                                        text = errorMessage,
                                        style = MaterialTheme.typography.bodyLarge,
                                        textAlign = TextAlign.Center
                                )
                                TextButton(onClick = { viewModel.loadProduct(productId) }) {
                                    Text("Reintentar")
                                }
                            }
                        }
                    }
                    detailState.product != null -> {
                        val product = detailState.product ?: return@composable
                        ProductDetailScreen(
                                product = product,
                                currentQuantity = orderSelections[product.id]?.quantity ?: 0,
                                onBackClick = { navController.navigateUp() },
                                onQuantityConfirmed = {
                                        quantity,
                                        warehouseId,
                                        warehouseName,
                                        lotId,
                                        lotLabel ->
                                    if (quantity <= 0) {
                                        orderSelections.remove(product.id)
                                    } else {
                                        orderSelections[product.id] =
                                                OrderSummaryItem(
                                                        productId = product.id,
                                                        name = product.name,
                                                        quantity = quantity,
                                                        unitPrice = product.pricing.price,
                                                        currency = product.pricing.currency,
                                                        warehouseId = warehouseId,
                                                        warehouseName = warehouseName,
                                                        lotId = lotId,
                                                        lotName = lotLabel
                                                )
                                    }
                                    navController.navigateUp()
                                }
                        )
                    }
                }
            }
        }

        composable(Screen.OrderSummary.route) {
            val summaryItems = orderSelections.values.filter { it.quantity > 0 }
            val currencyCode = summaryItems.firstOrNull()?.currency ?: "COP"
            val totalAmount = summaryItems.sumOf { it.lineTotal }
            val orderResult = lastOrderResult.value
            OrderSummaryScreen(
                    orderId = orderResult?.id ?: "Orden pendiente",
                    status = orderResult?.estado ?: "En progreso",
                    totalAmountFormatted =
                            if (summaryItems.isEmpty()) "-"
                            else formatCurrency(currencyCode, totalAmount),
                    items = summaryItems,
                    currencyCode = config?.country?.currencyCode ?: currencyCode,
                    onBackClick = {
                        // Navigate to orders list and clear selections
                        orderSelections.clear()
                        navController.navigate(Screen.Inventory.route) {
                            popUpTo(Screen.Home.route) { inclusive = false }
                        }
                    }
            )
        }

        // Pantalla de Clientes
        composable(Screen.Clients.route) {
            if (canViewClients) {
                val viewModel: ClientsViewModel = hiltViewModel()

                ClientsScreen(
                        viewModel = viewModel,
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
                                    Modifier.fillMaxSize()
                                            .padding(horizontal = 24.dp)
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
                                    Modifier.fillMaxSize()
                                            .padding(horizontal = 24.dp)
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
                            onBackClick = { navController.navigateUp() },
                            onVisitSelected = { visit ->
                                clientId?.let {
                                    navController.navigate(
                                            Screen.VisitDetail.createRoute(it, visit.id)
                                    )
                                }
                            }
                    )
                } else {
                    Surface {
                        Text(
                                text = "No encontramos la información del cliente.",
                                style = MaterialTheme.typography.bodyLarge,
                                textAlign = TextAlign.Center,
                                modifier =
                                        Modifier.fillMaxSize()
                                                .padding(horizontal = 24.dp)
                                                .wrapContentSize(Alignment.Center)
                        )
                    }
                }
            }
        }
        composable(
                route = Screen.VisitDetail.route,
                arguments =
                        listOf(
                                navArgument(Screen.VisitDetail.CLIENT_ID_ARG) {
                                    type = NavType.StringType
                                },
                                navArgument(Screen.VisitDetail.VISIT_ID_ARG) {
                                    type = NavType.StringType
                                }
                        )
        ) { backStackEntry ->
            if (!canViewClients) {
                Surface {
                    Text(
                            text = "No tienes permisos para ver esta sección.",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.error,
                            textAlign = TextAlign.Center,
                            modifier =
                                    Modifier.fillMaxSize()
                                            .padding(horizontal = 24.dp)
                                            .wrapContentSize(Alignment.Center)
                    )
                }
            } else {
                val clientId = backStackEntry.arguments?.getString(Screen.VisitDetail.CLIENT_ID_ARG)
                val visitId = backStackEntry.arguments?.getString(Screen.VisitDetail.VISIT_ID_ARG)
                val viewModel: VisitDetailViewModel = hiltViewModel()
                if (clientId != null && visitId != null) {
                    VisitDetailScreen(
                            visitId = visitId,
                            onBackClick = { navController.navigateUp() },
                            onSaveClick = { _, _ -> navController.navigateUp() },
                            viewModel = viewModel
                    )
                } else {
                    Surface {
                        Text(
                                text = "No encontramos la información de la visita.",
                                style = MaterialTheme.typography.bodyLarge,
                                textAlign = TextAlign.Center,
                                modifier =
                                        Modifier.fillMaxSize()
                                                .padding(horizontal = 24.dp)
                                                .wrapContentSize(Alignment.Center)
                        )
                    }
                }
            }
        }
    }
}

private fun formatCurrency(currencyCode: String, amount: Double): String {
    val formatter = NumberFormat.getCurrencyInstance(Locale.getDefault())
    formatter.currency = Currency.getInstance(currencyCode)
    return formatter.format(amount)
}
