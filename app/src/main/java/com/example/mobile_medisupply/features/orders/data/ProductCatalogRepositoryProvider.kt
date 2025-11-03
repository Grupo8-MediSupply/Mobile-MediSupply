package com.example.mobile_medisupply.features.orders.data

import com.example.mobile_medisupply.features.orders.domain.repository.ProductCatalogRepository

object ProductCatalogRepositoryProvider {
    val repository: ProductCatalogRepository by lazy { FakeProductCatalogRepository() }
}
