package com.example.mobile_medisupply.features.orders.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobile_medisupply.features.orders.domain.model.OrderSummary
import com.example.mobile_medisupply.features.orders.domain.repository.ProductCatalogRepositoryImp
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class OrdersUiState(
        val isLoading: Boolean = false,
        val orders: List<OrderSummary> = emptyList(),
        val errorMessage: String? = null
)

@HiltViewModel
class OrdersViewModel @Inject constructor(
        private val repository: ProductCatalogRepositoryImp
) : ViewModel() {

    private val _uiState = MutableStateFlow(OrdersUiState())
    val uiState: StateFlow<OrdersUiState> = _uiState.asStateFlow()

    fun loadClientOrders(state: String? = "enviado", limit: Int? = 10) {
        viewModelScope.launch {
            _uiState.value = OrdersUiState(isLoading = true)
            repository.getOrderHistoryByClient(state = state, limit = limit).collect { result ->
                _uiState.value = result.fold(
                        onSuccess = { orders ->
                            println("DEBUG OrdersViewModel: Loaded ${orders.size} orders")
                            orders.forEach { order ->
                                println("DEBUG Order: id=${order.id}, number=${order.orderNumber}, total=${order.formattedTotal}")
                            }
                            OrdersUiState(orders = orders)
                        },
                        onFailure = { error ->
                            println("DEBUG OrdersViewModel: Error loading orders: ${error.message}")
                            OrdersUiState(errorMessage = error.message ?: "Error al cargar pedidos")
                        }
                )
            }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }
}
