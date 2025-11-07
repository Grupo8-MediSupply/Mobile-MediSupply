package com.example.mobile_medisupply.features.orders.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobile_medisupply.features.orders.domain.model.ProductSummary
import com.example.mobile_medisupply.features.orders.domain.repository.ProductCatalogRepositoryImp
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ProductCatalogUiState(
        val isLoading: Boolean = true,
        val products: List<ProductSummary> = emptyList(),
        val errorMessage: String? = null
)

@HiltViewModel
class ProductCatalogViewModel @Inject constructor(
        private val repository: ProductCatalogRepositoryImp
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProductCatalogUiState())
    val uiState: StateFlow<ProductCatalogUiState> = _uiState.asStateFlow()

    init {
        loadCatalog()
    }

    fun loadCatalog() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            repository.getCatalog().collect { result ->
                _uiState.value =
                        result.fold(
                                onSuccess = { list ->
                                    ProductCatalogUiState(
                                            isLoading = false,
                                            products = list
                                    )
                                },
                                onFailure = { error ->
                                    ProductCatalogUiState(
                                            isLoading = false,
                                            errorMessage = error.message ?: "Error al cargar catálogo"
                                    )
                                }
                        )
            }
        }
    }

    fun findSummary(productId: String): ProductSummary? =
            _uiState.value.products.firstOrNull { it.id == productId }
}
