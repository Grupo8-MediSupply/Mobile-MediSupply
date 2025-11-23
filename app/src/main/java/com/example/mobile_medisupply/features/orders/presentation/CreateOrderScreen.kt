@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.mobile_medisupply.features.orders.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material.icons.outlined.FilterList
import androidx.compose.material.icons.outlined.Inventory2
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.mobile_medisupply.features.auth.domain.model.UserRole
import com.example.mobile_medisupply.features.clients.data.ClientRepositoryProvider
import com.example.mobile_medisupply.features.clients.domain.model.ClientSummary
import com.example.mobile_medisupply.features.orders.domain.model.OrderSummaryItem
import com.example.mobile_medisupply.features.orders.domain.model.ProductSummary

@Composable
fun CreateOrderScreen(
        userRole: UserRole,
        sessionClientId: String?,
        selections: Map<String, OrderSummaryItem>,
        catalogState: ProductCatalogUiState,
        orderState: CreateOrderUiState,
        onRefreshCatalog: () -> Unit,
        onBackClick: () -> Unit,
        onSubmitOrder: (String, List<OrderSummaryItem>) -> Unit = { _, _ -> },
        onSubmitOrderByClient: (List<OrderSummaryItem>) -> Unit = {},
        onDismissError: () -> Unit = {},
        onProductDetail: (String) -> Unit = {}
) {
        val catalog = catalogState.products
        val categories =
                remember(catalog) {
                        buildList {
                                add("Todas")
                                addAll(
                                        catalog
                                                .map { summary ->
                                                        summary.sku.substringBefore("-").uppercase()
                                                }
                                                .distinct()
                                )
                        }
                }

        val clients = remember { ClientRepositoryProvider.repository.getClients() }

        var selectedCategory by remember { mutableStateOf("Todas") }
        var searchQuery by remember { mutableStateOf("") }

        val filteredProducts =
                remember(searchQuery, selectedCategory, catalog) {
                        catalog.filter { product ->
                                val productCategory = product.sku.substringBefore("-").uppercase()
                                val matchesCategory =
                                        selectedCategory == "Todas" ||
                                                productCategory == selectedCategory
                                val matchesQuery =
                                        searchQuery.isBlank() ||
                                                product.name.contains(
                                                        searchQuery,
                                                        ignoreCase = true
                                                ) ||
                                                product.description.contains(
                                                        searchQuery,
                                                        ignoreCase = true
                                                ) ||
                                                product.sku.contains(searchQuery, ignoreCase = true)
                                matchesCategory && matchesQuery
                        }
                }

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

        val selectionList = selections.values.filter { it.quantity > 0 }
        val totalUnits = selectionList.sumOf { it.quantity }
        val totalProducts = selectionList.size
        val clientIdForOrder = resolveClientIdForOrder(userRole, sessionClientId, selectedClient)

        // For clients, they can submit if they have products selected (no client selection needed)
        // For sellers/admins, they need both a client selected and products
        val canSubmitOrder =
                if (userRole == UserRole.CLIENTE) {
                        selectionList.isNotEmpty()
                } else {
                        clientIdForOrder != null && selectionList.isNotEmpty()
                }

        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.surface) {
                Column(
                        modifier =
                                Modifier.fillMaxSize()
                                        .background(
                                                MaterialTheme.colorScheme.surfaceVariant.copy(
                                                        alpha = 0.25f
                                                )
                                        )
                ) {
                        HeaderSection(onBackClick = onBackClick)
                        orderState.errorMessage?.let {
                                ErrorBanner(message = it, onDismiss = onDismissError)
                        }

                        LazyColumn(
                                modifier =
                                        Modifier.weight(1f)
                                                .fillMaxWidth()
                                                .padding(horizontal = 16.dp)
                                                .padding(bottom = 88.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                                item {
                                        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                                                Text(
                                                        text = "Nueva Orden",
                                                        style =
                                                                MaterialTheme.typography
                                                                        .headlineSmall,
                                                        color = MaterialTheme.colorScheme.primary
                                                )

                                                // Only show client selector for sellers and admins
                                                if (userRole != UserRole.CLIENTE) {
                                                        ClientSelector(
                                                                userRole = userRole,
                                                                selectedClient = selectedClient,
                                                                clients = clients,
                                                                onClientSelected = {
                                                                        selectedClient = it
                                                                }
                                                        )
                                                }

                                                SearchBar(
                                                        query = searchQuery,
                                                        onQueryChange = { searchQuery = it },
                                                        onSearch = { focusManager.clearFocus() }
                                                )

                                                CategorySelector(
                                                        categories = categories,
                                                        selectedCategory = selectedCategory,
                                                        onCategorySelected = {
                                                                selectedCategory = it
                                                        }
                                                )
                                        }
                                }

                                when {
                                        catalogState.isLoading -> item { CatalogLoadingState() }
                                        catalogState.errorMessage != null -> {
                                                item {
                                                        CatalogErrorState(
                                                                message = catalogState.errorMessage,
                                                                onRetry = onRefreshCatalog
                                                        )
                                                }
                                        }
                                        filteredProducts.isEmpty() ->
                                                item { EmptyCatalogState(onRefreshCatalog) }
                                        else ->
                                                items(filteredProducts, key = { it.id }) { product
                                                        ->
                                                        val selectedQuantity =
                                                                selections[product.id]?.quantity
                                                                        ?: 0
                                                        ProductCatalogCard(
                                                                product = product,
                                                                selectedQuantity = selectedQuantity,
                                                                onProductDetail = {
                                                                        onProductDetail(product.id)
                                                                }
                                                        )
                                                }
                                }

                                item { Spacer(modifier = Modifier.height(64.dp)) }
                        }

                        OrderFooter(
                                userRole = userRole,
                                totalProducts = totalProducts,
                                totalUnits = totalUnits,
                                canSubmit = canSubmitOrder,
                                isSubmitting = orderState.isSubmitting,
                                onSubmitClick = {
                                        if (userRole == UserRole.CLIENTE) {
                                                // Client orders don't need clientId
                                                onSubmitOrderByClient(selectionList)
                                        } else {
                                                // Seller/Admin orders need clientId
                                                val clientId = clientIdForOrder
                                                if (clientId != null) {
                                                        onSubmitOrder(clientId, selectionList)
                                                } else {
                                                        onDismissError()
                                                }
                                        }
                                },
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
                                var fieldValue by
                                        remember(selectedClient) {
                                                mutableStateOf(selectedClient?.name ?: "")
                                        }

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
                                                        ExposedDropdownMenuDefaults.TrailingIcon(
                                                                expanded = expanded
                                                        )
                                                },
                                                leadingIcon = {
                                                        Icon(
                                                                Icons.Outlined.Person,
                                                                contentDescription = null
                                                        )
                                                },
                                                colors =
                                                        TextFieldDefaults.outlinedTextFieldColors(
                                                                focusedBorderColor =
                                                                        MaterialTheme.colorScheme
                                                                                .primary,
                                                                unfocusedBorderColor =
                                                                        MaterialTheme.colorScheme
                                                                                .outline.copy(
                                                                                alpha = 0.6f
                                                                        )
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
                                                                                imageVector =
                                                                                        Icons.Outlined
                                                                                                .Inventory2,
                                                                                contentDescription =
                                                                                        null
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
                                                                MaterialTheme.colorScheme
                                                                        .surfaceVariant.copy(
                                                                        alpha = 0.45f
                                                                )
                                                ),
                                        modifier = Modifier.fillMaxWidth()
                                ) {
                                        Column(
                                                modifier = Modifier.fillMaxWidth().padding(16.dp),
                                                verticalArrangement = Arrangement.spacedBy(4.dp)
                                        ) {
                                                Text(
                                                        text = selectedClient?.name
                                                                        ?: "Cliente asignado",
                                                        style =
                                                                MaterialTheme.typography
                                                                        .titleMedium,
                                                        color = MaterialTheme.colorScheme.onSurface
                                                )
                                                Text(
                                                        text = selectedClient?.location ?: "",
                                                        style = MaterialTheme.typography.bodySmall,
                                                        color =
                                                                MaterialTheme.colorScheme
                                                                        .onSurfaceVariant
                                                )
                                        }
                                }
                        }
                }
        }
}

private val CLIENT_ID_OVERRIDES =
        mapOf("clinica-san-rafael" to "7a89b952-aa5b-4f16-95d5-30e845111c9f")

private fun resolveClientIdForOrder(
        userRole: UserRole,
        sessionClientId: String?,
        selectedClient: ClientSummary?
): String? {
        val rawId =
                when (userRole) {
                        UserRole.CLIENTE -> sessionClientId ?: selectedClient?.id
                        else -> selectedClient?.id
                }
                        ?: return null
        if (rawId.matches(
                        Regex(
                                "^[0-9a-fA-F-]{8}-[0-9a-fA-F-]{4}-[0-9a-fA-F-]{4}-[0-9a-fA-F-]{4}-[0-9a-fA-F-]{12}\$"
                        )
                )
        ) {
                return rawId
        }
        return CLIENT_ID_OVERRIDES[rawId] ?: rawId
}

@Composable
private fun SearchBar(query: String, onQueryChange: (String) -> Unit, onSearch: () -> Unit) {
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
                        KeyboardActions(
                                onSearch = {
                                        focusManager.clearFocus()
                                        onSearch()
                                }
                        ),
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
                        trailingIcon = {
                                Icon(Icons.Outlined.FilterList, contentDescription = null)
                        }
                )

                Surface(
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f),
                        modifier = Modifier.size(44.dp).clickable { expanded = true }
                ) {
                        Box(contentAlignment = Alignment.Center) {
                                Icon(
                                        imageVector = Icons.Outlined.ChevronRight,
                                        contentDescription = "Abrir categorías",
                                        tint = MaterialTheme.colorScheme.primary
                                )
                        }
                }
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
        product: ProductSummary,
        selectedQuantity: Int,
        onProductDetail: () -> Unit
) {
        Card(
                modifier = Modifier.fillMaxWidth().clickable(onClick = onProductDetail),
                colors =
                        CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
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
                                                text = "SKU: ${product.sku}",
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

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                                text = product.description,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                        ) {
                                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                                        Text(
                                                text = product.formattedPrice,
                                                style = MaterialTheme.typography.titleMedium,
                                                color = MaterialTheme.colorScheme.primary
                                        )
                                        Text(
                                                text =
                                                        "Categoría: ${product.sku.substringBefore('-').uppercase()}",
                                                style = MaterialTheme.typography.labelSmall,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                }

                                if (selectedQuantity > 0) {
                                        Surface(
                                                shape = CircleShape,
                                                color =
                                                        MaterialTheme.colorScheme.primary.copy(
                                                                alpha = 0.12f
                                                        )
                                        ) {
                                                Text(
                                                        text = "${selectedQuantity} u",
                                                        style =
                                                                MaterialTheme.typography
                                                                        .labelMedium,
                                                        color = MaterialTheme.colorScheme.primary,
                                                        modifier =
                                                                Modifier.padding(
                                                                        horizontal = 10.dp,
                                                                        vertical = 6.dp
                                                                )
                                                )
                                        }
                                }
                        }
                }
        }
}

@Composable
private fun EmptyCatalogState(onRefresh: () -> Unit) {
        Column(
                modifier = Modifier.fillMaxWidth().padding(vertical = 64.dp),
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
                TextButton(onClick = onRefresh) { Text(text = "Recargar catálogo") }
        }
}

@Composable
private fun CatalogLoadingState() {
        Column(
                modifier = Modifier.fillMaxWidth().padding(vertical = 48.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
                CircularProgressIndicator()
                Text(
                        text = "Cargando catálogo...",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                )
        }
}

@Composable
private fun CatalogErrorState(message: String, onRetry: () -> Unit) {
        Column(
                modifier = Modifier.fillMaxWidth().padding(vertical = 48.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
                Text(
                        text = "No pudimos cargar los productos.",
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.error,
                        textAlign = TextAlign.Center
                )
                Text(
                        text = message,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth(0.8f)
                )
                TextButton(onClick = onRetry) { Text(text = "Intentar de nuevo") }
        }
}

@Composable
private fun ErrorBanner(message: String, onDismiss: () -> Unit) {
        Surface(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
                color = MaterialTheme.colorScheme.errorContainer,
                shape = MaterialTheme.shapes.medium
        ) {
                Row(
                        modifier = Modifier.fillMaxWidth().padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                ) {
                        Text(
                                text = message,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onErrorContainer,
                                modifier = Modifier.weight(1f),
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                        )
                        TextButton(onClick = onDismiss) { Text(text = "Cerrar") }
                }
        }
}

@Composable
private fun OrderFooter(
        userRole: UserRole,
        totalProducts: Int,
        totalUnits: Int,
        canSubmit: Boolean,
        isSubmitting: Boolean,
        onSubmitClick: () -> Unit,
        modifier: Modifier = Modifier
) {
        Surface(
                modifier = modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 16.dp),
                tonalElevation = 3.dp,
                shape = MaterialTheme.shapes.large
        ) {
                Column(
                        modifier =
                                Modifier.fillMaxWidth()
                                        .padding(horizontal = 16.dp, vertical = 12.dp)
                ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                                Surface(
                                        shape = CircleShape,
                                        color =
                                                MaterialTheme.colorScheme.primary.copy(
                                                        alpha = 0.12f
                                                ),
                                        modifier = Modifier.size(32.dp)
                                ) {
                                        Box(contentAlignment = Alignment.Center) {
                                                Icon(
                                                        imageVector = Icons.Outlined.Inventory2,
                                                        contentDescription = null,
                                                        tint = MaterialTheme.colorScheme.primary
                                                )
                                        }
                                }
                                Spacer(modifier = Modifier.size(12.dp))
                                Column {
                                        Text(
                                                text = "Productos seleccionados: $totalProducts",
                                                style = MaterialTheme.typography.bodyMedium,
                                                color = MaterialTheme.colorScheme.onSurface
                                        )
                                        Text(
                                                text = "Unidades totales: $totalUnits",
                                                style = MaterialTheme.typography.labelSmall,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                }
                        }

                        if (!canSubmit) {
                                val message =
                                        if (userRole == UserRole.CLIENTE) {
                                                "Agrega productos para continuar."
                                        } else {
                                                "Selecciona un cliente y agrega productos para continuar."
                                        }
                                Text(
                                        text = message,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        modifier = Modifier.padding(top = 8.dp)
                                )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Button(
                                onClick = onSubmitClick,
                                enabled = canSubmit && !isSubmitting,
                                modifier = Modifier.fillMaxWidth()
                        ) {
                                if (isSubmitting) {
                                        CircularProgressIndicator(
                                                modifier = Modifier.size(18.dp),
                                                strokeWidth = 2.dp,
                                                color = MaterialTheme.colorScheme.onPrimary
                                        )
                                } else {
                                        Text("Crear orden")
                                }
                        }
                }
        }
}
