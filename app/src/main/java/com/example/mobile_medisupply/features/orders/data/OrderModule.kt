package com.example.mobile_medisupply.features.orders.data

import com.example.mobile_medisupply.features.orders.data.repository.ProductCatalogRepository
import com.example.mobile_medisupply.features.orders.domain.repository.ProductCatalogRepositoryImp
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class OrderModule {

    @Binds
    @Singleton
    abstract fun bindProductCatalogRepository(
        impl: ProductCatalogRepository
    ): ProductCatalogRepositoryImp
}