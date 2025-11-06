package com.example.mobile_medisupply.features.home.data.repository

import com.example.mobile_medisupply.features.home.data.mappers.toDomainList
import com.example.mobile_medisupply.features.home.data.remote.VisitApi
import com.example.mobile_medisupply.features.home.domain.model.ScheduledVisit
import com.example.mobile_medisupply.features.home.domain.repository.ScheduledVisitsRepository
import com.example.mobile_medisupply.features.home.domain.repository.ScheduledVisitsRepositoryImp
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import java.time.LocalDate
import javax.inject.Inject

class ScheduledVisitRepositoryImp @Inject constructor(
    private val visitApi: VisitApi
) : ScheduledVisitsRepositoryImp{

    override suspend fun getScheduledVisits(fecha: String?): Flow<List<ScheduledVisit>> = flow {
        val dateParam = fecha ?: LocalDate.now().toString()

        val response = visitApi.obtenerVisitasDelDia(dateParam)

        val visits = response.result?.visitas?.toDomainList().orEmpty()

        emit(visits)
    }.catch { e ->
        e.printStackTrace()
        emit(emptyList())
    }


}