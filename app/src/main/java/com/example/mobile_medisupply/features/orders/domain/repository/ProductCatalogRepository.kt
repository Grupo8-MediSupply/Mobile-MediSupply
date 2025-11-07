package com.example.mobile_medisupply.features.orders.domain.repository

import com.example.mobile_medisupply.features.orders.data.remote.ProductItemRequest
import com.example.mobile_medisupply.features.orders.data.remote.OrderCreatedResult
import com.example.mobile_medisupply.features.orders.domain.model.ProductCatalogItem
import com.example.mobile_medisupply.features.orders.domain.model.ProductSummary
import kotlinx.coroutines.flow.Flow

interface ProductCatalogRepository {
    fun getCatalog(): List<ProductCatalogItem>
    fun getCategories(): List<String> = getCatalog().map { it.category }.distinct()
    fun getProductById(productId: String): ProductCatalogItem? =
            getCatalog().find { it.id == productId }
}

interface ProductCatalogRepositoryImp {

    suspend fun createOrder(
        clienteId: String,
        products: List<ProductItemRequest>
    ): Flow<Result<OrderCreatedResult>>

    suspend fun getCatalog(): Flow<Result<List<ProductSummary>>>

    suspend fun getProductDetail(productId: String): Flow<Result<ProductCatalogItem>>
}
