package com.example.mobile_medisupply.features.clients.data

import com.example.mobile_medisupply.features.clients.data.repository.ClientesRepository
import com.example.mobile_medisupply.features.clients.domain.repository.ClientRepository
import com.example.mobile_medisupply.features.clients.domain.repository.ClientRepositoryImp
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ClientsModule {

    @Binds
    @Singleton
    abstract fun bindClientRepository(
        impl: ClientesRepository
    ): ClientRepositoryImp
}