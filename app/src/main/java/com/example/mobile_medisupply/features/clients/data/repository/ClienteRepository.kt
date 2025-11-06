package com.example.mobile_medisupply.features.clients.data.repository

import com.example.mobile_medisupply.features.auth.data.local.SessionManager
import com.example.mobile_medisupply.features.clients.data.remote.ClienteResumen
import com.example.mobile_medisupply.features.clients.data.remote.ClienteApi
import com.example.mobile_medisupply.features.clients.data.remote.toDomain
import com.example.mobile_medisupply.features.clients.domain.model.ClientDetail
import com.example.mobile_medisupply.features.clients.domain.model.ClientSummary
import com.example.mobile_medisupply.features.clients.domain.model.ClientVisitDetail
import com.example.mobile_medisupply.features.clients.domain.repository.ClientRepository
import com.example.mobile_medisupply.features.clients.domain.repository.ClientRepositoryImp
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject


class ClientesRepository @Inject constructor(
    private val clientesApi: ClienteApi,
    private val sessionManager: SessionManager
) : ClientRepositoryImp {

    suspend override fun getClients(): Flow<Result<List<ClientSummary>>> = flow  {
        try {
            val token = sessionManager.getSession()?.token
                ?: throw IllegalStateException("No hay sesión activa")

            val response = clientesApi.obtenerClientes()

            if (response.success) {
                val clientesDto: List<ClienteResumen> = response.result ?: emptyList()
                val clientesDomain = clientesDto.map { it.toDomain() }
                emit(Result.success(clientesDomain))
            } else {
                emit(Result.failure(Exception(response.message ?: "Error al obtener clientes")))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    override fun getClientDetail(clientId: String): ClientDetail? {
        TODO("Not yet implemented")
    }

    override fun getClientVisitDetail(
        clientId: String,
        visitId: String
    ): ClientVisitDetail? {
        TODO("Not yet implemented")
    }
}