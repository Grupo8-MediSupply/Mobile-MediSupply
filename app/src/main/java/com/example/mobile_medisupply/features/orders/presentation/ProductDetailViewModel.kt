package com.example.mobile_medisupply.features.orders.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobile_medisupply.features.orders.domain.model.ProductCatalogItem
import com.example.mobile_medisupply.features.orders.domain.repository.ProductCatalogRepositoryImp
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ProductDetailUiState(
        val isLoading: Boolean = false,
        val product: ProductCatalogItem? = null,
        val error: String? = null
)

@HiltViewModel
class ProductDetailViewModel @Inject constructor(
        private val repository: ProductCatalogRepositoryImp
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProductDetailUiState())
    val uiState: StateFlow<ProductDetailUiState> = _uiState.asStateFlow()

    fun loadProduct(productId: String) {
        viewModelScope.launch {
            _uiState.value = ProductDetailUiState(isLoading = true)
            repository.getProductDetail(productId).collect { result ->
                _uiState.value =
                        result.fold(
                                onSuccess = { detail ->
                                    ProductDetailUiState(product = detail)
                                },
                                onFailure = { error ->
                                    ProductDetailUiState(error = error.message ?: "Error al cargar el producto")
                                }
                        )
            }
        }
    }
}
