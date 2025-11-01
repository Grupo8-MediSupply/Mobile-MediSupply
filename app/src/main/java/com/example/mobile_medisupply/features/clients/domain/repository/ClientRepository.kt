package com.example.mobile_medisupply.features.clients.domain.repository

import com.example.mobile_medisupply.features.clients.domain.model.ClientDetail
import com.example.mobile_medisupply.features.clients.domain.model.ClientSummary

interface ClientRepository {
    fun getClients(): List<ClientSummary>
    fun getClientDetail(clientId: String): ClientDetail?
}
