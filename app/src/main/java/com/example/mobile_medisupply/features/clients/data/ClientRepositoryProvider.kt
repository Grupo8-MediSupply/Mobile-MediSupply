package com.example.mobile_medisupply.features.clients.data

import com.example.mobile_medisupply.features.clients.domain.repository.ClientRepository

object ClientRepositoryProvider {
    val repository: ClientRepository by lazy { FakeClientRepository() }
}
