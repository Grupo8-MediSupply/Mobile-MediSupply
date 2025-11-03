package com.example.mobile_medisupply.features.clients.domain.repository

import com.example.mobile_medisupply.features.clients.data.remote.ClienteResumen
import com.example.mobile_medisupply.features.clients.domain.model.ClientDetail
import com.example.mobile_medisupply.features.clients.domain.model.ClientSummary
import com.example.mobile_medisupply.features.clients.domain.model.ClientVisitDetail
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface ClientRepository {
    fun getClients(): List<ClientSummary>
    fun getClientDetail(clientId: String): ClientDetail?
    fun getClientVisitDetail(clientId: String, visitId: String): ClientVisitDetail?
}

interface ClientRepositoryImp {
    suspend fun getClients(): Flow<Result<List<ClientSummary>>>
    fun getClientDetail(clientId: String): ClientDetail?
    fun getClientVisitDetail(clientId: String, visitId: String): ClientVisitDetail?
}
