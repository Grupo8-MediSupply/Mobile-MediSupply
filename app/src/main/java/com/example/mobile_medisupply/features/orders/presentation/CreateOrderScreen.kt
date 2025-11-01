@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)

package com.example.mobile_medisupply.features.orders.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Category
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material.icons.outlined.FilterList
import androidx.compose.material.icons.outlined.Inventory2
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.example.mobile_medisupply.features.auth.domain.model.UserRole
import com.example.mobile_medisupply.features.clients.data.ClientRepositoryProvider
import com.example.mobile_medisupply.features.clients.domain.model.ClientSummary
import com.example.mobile_medisupply.features.orders.data.ProductCatalogRepositoryProvider
import com.example.mobile_medisupply.features.orders.domain.model.ProductAvailability
import com.example.mobile_medisupply.features.orders.domain.model.ProductCatalogItem
import com.example.mobile_medisupply.features.orders.domain.model.ProductLot

data class SelectedLotKey(
        val productId: String,
        val warehouseId: String,
        val lotId: String
)

@Composable
fun CreateOrderScreen(
        userRole: UserRole,
        sessionClientId: String?,
        onBackClick: () -> Unit,
        onOrderSubmit: (List<SelectedLotKey>) -> Unit = {},
        onProductDetail: (ProductCatalogItem) -> Unit = {}
) {
    val catalog = remember { ProductCatalogRepositoryProvider.repository.getCatalog() }
    val categories =
            remember(catalog) {
                buildList {
                    add("Todas")
                    addAll(catalog.map { it.category }.distinct())
                }
            }

    val clients = remember { ClientRepositoryProvider.repository.getClients() }

    var selectedCategory by remember { mutableStateOf("Todas") }
    var searchQuery by remember { mutableStateOf("") }

    val filteredProducts =
            remember(searchQuery, selectedCategory, catalog) {
                catalog.filter { product ->
                    val matchesCategory =
                            selectedCategory == "Todas" || product.category == selectedCategory
                    val matchesQuery =
                            searchQuery.isBlank() ||
                                    product.name.contains(searchQuery, ignoreCase = true) ||
                                    product.description.contains(searchQuery, ignoreCase = true)
                    matchesCategory && matchesQuery
                }
            }

    val selectedLots = remember { mutableStateListOf<SelectedLotKey>() }
    val focusManager = LocalFocusManager.current

    val initialClient =
            remember(userRole, sessionClientId, clients) {
                when (userRole) {
                    UserRole.CLIENTE -> clients.find { it.id == sessionClientId }
                    else -> clients.firstOrNull()
                }
            }

    var selectedClient by remember { mutableStateOf(initialClient) }
    LaunchedEffect(initialClient) {
        if (selectedClient == null && initialClient != null) {
            selectedClient = initialClient
        }
    }

    Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.surface
    ) {
        Column(
                modifier =
                        Modifier.fillMaxSize()
                                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.25f))
        ) {
            HeaderSection(onBackClick = onBackClick)

            LazyColumn(
                    modifier =
                            Modifier.fillMaxSize()
                                    .padding(horizontal = 16.dp)
                                    .padding(bottom = 88.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        Text(
                                text = "Nueva Orden",
                                style = MaterialTheme.typography.headlineSmall,
                                color = MaterialTheme.colorScheme.primary
                        )

                        ClientSelector(
                                userRole = userRole,
                                selectedClient = selectedClient,
                                clients = clients,
                                onClientSelected = {
                                    selectedClient = it
                                }
                        )

                        SearchBar(
                                query = searchQuery,
                                onQueryChange = { searchQuery = it },
                                onSearch = { focusManager.clearFocus() }
                        )

                        CategorySelector(
                                categories = categories,
                                selectedCategory = selectedCategory,
                                onCategorySelected = { selectedCategory = it }
                        )
                    }
                }

                if (filteredProducts.isEmpty()) {
                    item {
                        EmptyCatalogState()
                    }
                } else {
                    items(filteredProducts, key = { it.id }) { product ->
                        ProductCatalogCard(
                                product = product,
                                isSelected = selectedLots.any { it.productId == product.id },
                                selectedLots = selectedLots,
                                onLotToggle = { lotKey ->
                                    if (selectedLots.contains(lotKey)) {
                                        selectedLots.remove(lotKey)
                                    } else {
                                        selectedLots.add(lotKey)
                                    }
                                },
                                onProductDetail = { onProductDetail(product) }
                        )
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(64.dp))
                }
            }

            OrderFooter(
                    selectedCount = selectedLots.size,
                    onSubmitClick = { onOrderSubmit(selectedLots.toList()) },
                    modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }
}

@Composable
private fun HeaderSection(onBackClick: () -> Unit) {
    Row(
            modifier =
                    Modifier.fillMaxWidth()
                            .background(MaterialTheme.colorScheme.surface)
                            .padding(horizontal = 8.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onBackClick) {
            Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Regresar",
                    tint = MaterialTheme.colorScheme.primary
            )
        }

        Spacer(modifier = Modifier.size(8.dp))

        Text(
                text = "MediSupply",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
private fun ClientSelector(
        userRole: UserRole,
        selectedClient: ClientSummary?,
        clients: List<ClientSummary>,
        onClientSelected: (ClientSummary) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
                text = "Cliente",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        when (userRole) {
            UserRole.ADMIN, UserRole.VENDEDOR -> {
                var expanded by remember { mutableStateOf(false) }
                var fieldValue by remember(selectedClient) { mutableStateOf(selectedClient?.name ?: "") }

                ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = { expanded = it }
                ) {
                    OutlinedTextField(
                            value = fieldValue,
                            onValueChange = {},
                            modifier = Modifier.fillMaxWidth(),
                            readOnly = true,
                            label = { Text("Selecciona un cliente") },
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                            },
                            leadingIcon = { Icon(Icons.Outlined.Person, contentDescription = null) },
                            colors =
                                    TextFieldDefaults.outlinedTextFieldColors(
                                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                                            unfocusedBorderColor =
                                                    MaterialTheme.colorScheme.outline.copy(alpha = 0.6f)
                                    )
                    )

                    DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                    ) {
                        clients.forEach { client ->
                            DropdownMenuItem(
                                    text = { Text(client.name) },
                                    onClick = {
                                        fieldValue = client.name
                                        onClientSelected(client)
                                        expanded = false
                                    },
                                    leadingIcon = {
                                        Icon(
                                                imageVector = Icons.Outlined.Inventory2,
                                                contentDescription = null
                                        )
                                    }
                            )
                        }
                    }
                }
            }
            else -> {
                ElevatedCard(
                        shape = MaterialTheme.shapes.medium,
                        colors =
                                CardDefaults.elevatedCardColors(
                                        containerColor =
                                                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f)
                                ),
                        modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                            modifier = Modifier.fillMaxWidth().padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                                text = selectedClient?.name ?: "Cliente asignado",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                                text = selectedClient?.location ?: "",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SearchBar(
        query: String,
        onQueryChange: (String) -> Unit,
        onSearch: () -> Unit
) {
    val focusManager = LocalFocusManager.current
    OutlinedTextField(
            value = query,
            onValueChange = onQueryChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Buscar producto") },
            leadingIcon = { Icon(Icons.Outlined.Search, contentDescription = null) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions =
                    KeyboardActions(onSearch = {
                        focusManager.clearFocus()
                        onSearch()
                    }),
            colors =
                    TextFieldDefaults.outlinedTextFieldColors(
                            containerColor =
                                    MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
                    )
    )
}

@Composable
private fun CategorySelector(
        categories: List<String>,
        selectedCategory: String,
        onCategorySelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        OutlinedTextField(
                value = selectedCategory,
                onValueChange = {},
                modifier = Modifier.weight(1f).clickable { expanded = true },
                readOnly = true,
                label = { Text("Categorías") },
                trailingIcon = { Icon(Icons.Outlined.Category, contentDescription = null) }
        )

        Surface(
                shape = CircleShape,
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f),
                modifier =
                        Modifier.size(44.dp)
                                .clickable { expanded = true },
                content = {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                                imageVector = Icons.Outlined.FilterList,
                                contentDescription = "Filtrar",
                                tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
        )
    }

    DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
        categories.forEach { category ->
            DropdownMenuItem(
                    text = { Text(category) },
                    onClick = {
                        onCategorySelected(category)
                        expanded = false
                    }
            )
        }
    }
}

@Composable
private fun ProductCatalogCard(
        product: ProductCatalogItem,
        isSelected: Boolean,
        selectedLots: List<SelectedLotKey>,
        onLotToggle: (SelectedLotKey) -> Unit,
        onProductDetail: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Card(
            modifier =
                    Modifier.fillMaxWidth()
                            .clickable { expanded = !expanded },
            colors =
                    CardDefaults.cardColors(
                            containerColor =
                                    if (expanded)
                                            MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                                    else MaterialTheme.colorScheme.surface
                    ),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
            Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                            text = product.name,
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                            text = product.category,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                IconButton(onClick = onProductDetail) {
                    Icon(
                            imageVector = Icons.Outlined.ChevronRight,
                            contentDescription = "Ver detalle",
                            tint = MaterialTheme.colorScheme.primary
                    )
                }
            }

            if (expanded) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                        text = product.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(12.dp))
                product.availability.forEach { availability ->
                    WarehouseSection(
                            availability = availability,
                            selectedLots = selectedLots,
                            productId = product.id,
                            onLotToggle = onLotToggle
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
private fun WarehouseSection(
        availability: ProductAvailability,
        selectedLots: List<SelectedLotKey>,
        productId: String,
        onLotToggle: (SelectedLotKey) -> Unit
) {
    Column(
            modifier =
                    Modifier.fillMaxWidth()
                            .background(
                                    MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.25f),
                                    shape = MaterialTheme.shapes.medium
                            )
                            .padding(12.dp)
    ) {
        Text(
                text = availability.warehouseName,
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
                color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(8.dp))

        FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            availability.lots.forEach { lot ->
                val key = SelectedLotKey(productId, availability.warehouseId, lot.lotId)
                val isSelected = selectedLots.contains(key)

                AssistChip(
                        onClick = { onLotToggle(key) },
                        label = { Text("Lote ${lot.lotCode}") },
                        trailingIcon = {
                            Text(
                                    text = "${lot.availableUnits} u",
                                    style = MaterialTheme.typography.labelSmall
                            )
                        },
                        colors =
                                AssistChipDefaults.assistChipColors(
                                        containerColor =
                                                if (isSelected)
                                                        MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
                                                else MaterialTheme.colorScheme.surface,
                                        labelColor =
                                                if (isSelected)
                                                        MaterialTheme.colorScheme.primary
                                                else MaterialTheme.colorScheme.onSurface
                                )
                )
            }
        }
    }
}

@Composable
private fun EmptyCatalogState() {
    Column(
            modifier =
                    Modifier.fillMaxWidth()
                            .padding(vertical = 64.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(
                imageVector = Icons.Outlined.Inventory2,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(48.dp)
        )
        Text(
                text = "No encontramos productos con tu búsqueda.",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurface
        )
        Text(
                text = "Ajusta los filtros o intenta con otro término.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun OrderFooter(
        selectedCount: Int,
        onSubmitClick: () -> Unit,
        modifier: Modifier = Modifier
) {
    Surface(
            modifier =
                    modifier.fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 16.dp),
            tonalElevation = 3.dp,
            shape = MaterialTheme.shapes.large
    ) {
        Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (selectedCount > 0) {
                    Surface(
                            shape = CircleShape,
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.size(28.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                    text = selectedCount.toString(),
                                    color = MaterialTheme.colorScheme.onError,
                                    style = MaterialTheme.typography.labelLarge
                            )
                        }
                    }
                    Spacer(modifier = Modifier.size(8.dp))
                }
                Text(
                        text = "Ordenar",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                )
            }

            TextButton(
                    onClick = onSubmitClick,
                    enabled = selectedCount > 0
            ) {
                Text("Continuar")
            }
        }
    }
}
