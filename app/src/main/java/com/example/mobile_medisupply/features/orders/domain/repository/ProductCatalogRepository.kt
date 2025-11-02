package com.example.mobile_medisupply.features.orders.domain.repository

import com.example.mobile_medisupply.features.orders.domain.model.ProductCatalogItem

interface ProductCatalogRepository {
    fun getCatalog(): List<ProductCatalogItem>
    fun getCategories(): List<String> = getCatalog().map { it.category }.distinct()
    fun getProductById(productId: String): ProductCatalogItem? =
            getCatalog().find { it.id == productId }
}
