package com.example.mobile_medisupply.features.clients.domain.repository

import com.example.mobile_medisupply.features.clients.domain.model.ClientDetail
import com.example.mobile_medisupply.features.clients.domain.model.ClientSummary
import com.example.mobile_medisupply.features.clients.domain.model.ClientVisitDetail

interface ClientRepository {
    fun getClients(): List<ClientSummary>
    fun getClientDetail(clientId: String): ClientDetail?
    fun getClientVisitDetail(clientId: String, visitId: String): ClientVisitDetail?
}
