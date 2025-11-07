package com.example.mobile_medisupply.features.orders.data.repository

import com.example.mobile_medisupply.features.orders.data.mappers.toDomain
import com.example.mobile_medisupply.features.orders.data.mappers.toSummary
import com.example.mobile_medisupply.features.orders.data.remote.ProductApi
import com.example.mobile_medisupply.features.orders.data.remote.ProductItemRequest
import com.example.mobile_medisupply.features.orders.domain.model.ProductCatalogItem
import com.example.mobile_medisupply.features.orders.domain.model.ProductSummary
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

    override suspend fun getCatalog(): Flow<Result<List<ProductSummary>>> = flow {
        try {
            val response = api.obtenerProductos()
            if (response.success && response.result != null) {
                emit(Result.success(response.result.map { it.toSummary() }))
            } else {
                emit(
                        Result.failure(
                                IllegalStateException(
                                        response.message ?: "Error al obtener catálogo"
                                )
                        )
                )
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    override suspend fun getProductDetail(productId: String): Flow<Result<ProductCatalogItem>> = flow {
        try {
            val response = api.obtenerDetalleProducto(productId)
            if (response.success && response.result != null) {
                emit(Result.success(response.result.toDomain()))
            } else {
                emit(
                        Result.failure(
                                IllegalStateException(
                                        response.message ?: "Producto no disponible"
                                )
                        )
                )
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }
}
