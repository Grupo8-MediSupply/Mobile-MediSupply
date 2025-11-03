package com.example.mobile_medisupply

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.mobile_medisupply.features.auth.domain.model.UserRole
import com.example.mobile_medisupply.navigation.AppNavHost
import com.example.mobile_medisupply.navigation.Screen
import com.example.mobile_medisupply.ui.theme.MobileMediSupplyTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MobileMediSupplyTheme(dynamicColor = false) { MainApp(mainViewModel = mainViewModel) }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainApp(mainViewModel: MainViewModel) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val session by mainViewModel.session.collectAsStateWithLifecycle()
    val config by mainViewModel.config.collectAsStateWithLifecycle()
    val canViewVisits =
            remember(session) {
                when (session?.role) {
                    UserRole.ADMIN, UserRole.VENDEDOR -> true
                    else -> false
                }
            }

    val canViewClients =
            remember(session) {
                when (session?.role) {
                    UserRole.ADMIN, UserRole.VENDEDOR -> true
                    else -> false
                }
            }

    // Define las pantallas principales que mostrarán la barra de navegación
    val bottomBarRoutes =
            remember(canViewClients, canViewVisits) {
                buildSet {
                    if (canViewVisits) add(Screen.Home.route)
                    add(Screen.Inventory.route)
                    if (canViewClients) add(Screen.Clients.route)
                }
            }
    val showBottomBar = currentDestination?.route in bottomBarRoutes

    // Define las pantallas que mostrarán la barra superior
    val showTopBar =
            remember(currentDestination) {
                when (currentDestination?.route) {
                    Screen.Login.route, Screen.Register.route, Screen.Recover.route -> false
                    else -> true
                }
            }

    // Estados para el menú de perfil y diálogo de logout
    var showProfileMenu by remember { mutableStateOf(false) }
    var showLogoutDialog by remember { mutableStateOf(false) }

    Scaffold(
            topBar = {
                if (showTopBar) {
                    TopAppBar(
                            title = {
                                Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.Center,
                                        modifier = Modifier.fillMaxWidth()
                                ) {
                                    Image(
                                            painter =
                                                    painterResource(
                                                            id = R.drawable.logo_medisupply
                                                    ),
                                            contentDescription =
                                                    stringResource(R.string.cd_medisupply_logo),
                                            modifier = Modifier.height(40.dp)
                                    )
                                }
                            },
                            colors =
                                    TopAppBarDefaults.topAppBarColors(
                                            containerColor = Color.Transparent,
                                            titleContentColor = MaterialTheme.colorScheme.primary
                                    ),
                            actions = {
                                // Icono de perfil con menú desplegable
                                Box {
                                    IconButton(onClick = { showProfileMenu = true }) {
                                        Icon(
                                                Icons.Default.AccountCircle,
                                                contentDescription = "Perfil",
                                                tint = MaterialTheme.colorScheme.primary
                                        )
                                    }

                                    // Menú desplegable
                                    DropdownMenu(
                                            expanded = showProfileMenu,
                                            onDismissRequest = { showProfileMenu = false }
                                    ) {
                                        DropdownMenuItem(
                                                text = { Text("Cerrar sesión") },
                                                leadingIcon = {
                                                    Icon(
                                                            Icons.Default.Logout,
                                                            contentDescription = null
                                                    )
                                                },
                                                onClick = {
                                                    showProfileMenu = false
                                                    showLogoutDialog = true
                                                }
                                        )
                                    }
                                }
                            }
                    )
                }
            },
            bottomBar = {
                if (showBottomBar) {
                    NavigationBar {
                        val items =
                                buildList {
                                    if (canViewVisits) {
                                        add(Screen.Home to Icons.Default.CalendarMonth)
                                    }
                                    add(Screen.Inventory to Icons.Default.Inventory)
                                    if (canViewClients) {
                                        add(Screen.Clients to Icons.Default.Person)
                                    }
                                }

                        items.forEach { (screen, icon) ->
                            val selected =
                                    currentDestination?.hierarchy?.any {
                                        it.route == screen.route
                                    } == true

                            NavigationBarItem(
                                    icon = { Icon(icon, contentDescription = null) },
                                    label = {
                                        Text(screen.route.replaceFirstChar { it.uppercase() })
                                    },
                                    selected = selected,
                                    onClick = {
                                        navController.navigate(screen.route) {
                                            // Evita múltiples copias de la misma ruta en la pila
                                            popUpTo(navController.graph.findStartDestination().id) {
                                                saveState = true
                                            }
                                            // Evita recrear la pantalla si ya está seleccionada
                                            launchSingleTop = true
                                            // Restaura el estado al regresar
                                            restoreState = true
                                        }
                                    }
                            )
                        }
                    }
                }
            },
            content = { padding ->
                Box(modifier = Modifier.fillMaxSize().padding(padding)) {
                    AppNavHost(
                            navController = navController,
                            canViewClients = canViewClients,
                            canViewVisits = canViewVisits,
                            onLoginSuccess = {
                                mainViewModel.refreshSession()
                                mainViewModel.refreshConfig()
                            },
                            session = session,
                            config = config
                    )

                    // Diálogo de confirmación de logout
                    if (showLogoutDialog) {
                        AlertDialog(
                                onDismissRequest = { showLogoutDialog = false },
                                title = { Text("Cerrar sesión") },
                                text = { Text("¿Estás seguro que deseas cerrar la sesión?") },
                                confirmButton = {
                                    TextButton(
                                            onClick = {
                                                showLogoutDialog = false
                                                mainViewModel.clearSession()
                                                navController.navigate(Screen.Login.route) {
                                                    popUpTo(
                                                            navController.graph
                                                                    .findStartDestination()
                                                                    .id
                                                    ) { inclusive = true }
                                                }
                                            }
                                    ) { Text("Confirmar") }
                                },
                                dismissButton = {
                                    TextButton(onClick = { showLogoutDialog = false }) {
                                        Text("Cancelar")
                                    }
                                }
                        )
                    }
                }
            }
    )
}
