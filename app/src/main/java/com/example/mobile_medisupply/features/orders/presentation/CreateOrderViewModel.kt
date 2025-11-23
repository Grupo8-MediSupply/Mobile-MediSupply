package com.example.mobile_medisupply.features.orders.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobile_medisupply.features.orders.data.remote.OrderCreatedResult
import com.example.mobile_medisupply.features.orders.data.remote.ProductItemRequest
import com.example.mobile_medisupply.features.orders.domain.model.OrderSummaryItem
import com.example.mobile_medisupply.features.orders.domain.repository.ProductCatalogRepositoryImp
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class CreateOrderUiState(
        val isSubmitting: Boolean = false,
        val errorMessage: String? = null,
        val orderResult: OrderCreatedResult? = null
)

@HiltViewModel
class CreateOrderViewModel
@Inject
constructor(private val repository: ProductCatalogRepositoryImp) : ViewModel() {

    private val _uiState = MutableStateFlow(CreateOrderUiState())
    val uiState: StateFlow<CreateOrderUiState> = _uiState.asStateFlow()

    fun submitOrder(clientId: String, items: List<OrderSummaryItem>) {
        if (clientId.isBlank()) {
            _uiState.value =
                    _uiState.value.copy(errorMessage = "Selecciona un cliente para continuar.")
            return
        }
        if (items.isEmpty()) {
            _uiState.value = _uiState.value.copy(errorMessage = "Agrega al menos un producto.")
            return
        }
        val productRequests =
                items.map { item ->
                    ProductItemRequest(
                            lote = item.lotId,
                            bodega = item.warehouseId,
                            cantidad = item.quantity
                    )
                }

        viewModelScope.launch {
            _uiState.value = CreateOrderUiState(isSubmitting = true)
            repository.createOrder(clientId, productRequests).collect { result ->
                _uiState.value =
                        result.fold(
                                onSuccess = { order -> CreateOrderUiState(orderResult = order) },
                                onFailure = { error ->
                                    CreateOrderUiState(
                                            errorMessage = error.message
                                                            ?: "Error al crear la orden"
                                    )
                                }
                        )
            }
        }
    }

    fun submitOrderByClient(items: List<OrderSummaryItem>) {
        if (items.isEmpty()) {
            _uiState.value = _uiState.value.copy(errorMessage = "Agrega al menos un producto.")
            return
        }
        val productRequests =
                items.map { item ->
                    ProductItemRequest(
                            lote = item.lotId,
                            bodega = item.warehouseId,
                            cantidad = item.quantity
                    )
                }

        viewModelScope.launch {
            _uiState.value = CreateOrderUiState(isSubmitting = true)
            repository.createOrderByClient(productRequests).collect { result ->
                _uiState.value =
                        result.fold(
                                onSuccess = { order -> CreateOrderUiState(orderResult = order) },
                                onFailure = { error ->
                                    CreateOrderUiState(
                                            errorMessage = error.message
                                                            ?: "Error al crear la orden"
                                    )
                                }
                        )
            }
        }
    }

    fun clearOrderResult() {
        _uiState.value = _uiState.value.copy(orderResult = null)
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }
}
