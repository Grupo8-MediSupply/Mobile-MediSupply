package com.example.mobile_medisupply.features.home.domain.repository

import com.example.mobile_medisupply.features.home.domain.model.ScheduledVisit
import kotlinx.coroutines.flow.Flow

interface ScheduledVisitsRepository {
    fun getScheduledVisits(): List<ScheduledVisit>
}


interface ScheduledVisitsRepositoryImp {
    suspend fun getScheduledVisits(fecha: String? = null): Flow<List<ScheduledVisit>>
}
