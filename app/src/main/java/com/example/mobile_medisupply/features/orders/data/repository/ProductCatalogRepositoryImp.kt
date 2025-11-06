package com.example.mobile_medisupply.features.orders.data.repository

import com.example.mobile_medisupply.features.home.data.mappers.toDomainList
import com.example.mobile_medisupply.features.orders.data.mappers.toDomainList
import com.example.mobile_medisupply.features.orders.data.remote.ProductApi
import com.example.mobile_medisupply.features.orders.data.remote.ProductDto
import com.example.mobile_medisupply.features.orders.data.remote.ProductItemRequest
import com.example.mobile_medisupply.features.orders.domain.model.ProductCatalogItem
import com.example.mobile_medisupply.features.orders.domain.repository.ProductCatalogRepositoryImp
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ProductCatalogRepository @Inject constructor(
    private val api: ProductApi
) : ProductCatalogRepositoryImp {

    override suspend fun createOrder(
        clienteId: String,
        products: List<ProductItemRequest>
    ): Flow<Result<Unit>> = flow {
        try {
            val response = api.crearPedidoDesdeVendedor(clienteId, products)

            if (response.success) {
                emit(Result.success(Unit))
            } else {
                emit(Result.failure(Exception("Error creando pedido: ${response.message ?: "desconocido"}")))
            }

        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    override suspend fun getCatalog(): Flow<List<ProductDto>> = flow {
        try {
            val response = api.obtenerProductos()
            if (response.success && response.result != null) {
                emit(response.result)
            } else {
                emit(emptyList())
            }
        } catch (e: Exception) {
            emit(emptyList())
        }
    }

    override suspend fun getProductById(productId: String): Flow<ProductCatalogItem?> = flow {

    }
}