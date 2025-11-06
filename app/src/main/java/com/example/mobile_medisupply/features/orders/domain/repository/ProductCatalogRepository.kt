package com.example.mobile_medisupply.features.orders.domain.repository

import com.example.mobile_medisupply.features.orders.data.remote.ProductDto
import com.example.mobile_medisupply.features.orders.data.remote.ProductItemRequest
import com.example.mobile_medisupply.features.orders.domain.model.ProductCatalogItem
import kotlinx.coroutines.flow.Flow

interface ProductCatalogRepository {
    fun getCatalog(): List<ProductCatalogItem>
    fun getCategories(): List<String> = getCatalog().map { it.category }.distinct()
    fun getProductById(productId: String): ProductCatalogItem? =
            getCatalog().find { it.id == productId }
}

interface ProductCatalogRepositoryImp {

    suspend fun createOrder(clienteId: String,products: List<ProductItemRequest>): Flow<Result<Unit>>

    suspend fun getCatalog(): Flow<List<ProductDto>>

    suspend fun getProductById(productId: String): Flow<ProductCatalogItem?>
}
