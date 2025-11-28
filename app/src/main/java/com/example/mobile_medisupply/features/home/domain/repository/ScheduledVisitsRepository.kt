package com.example.mobile_medisupply.features.home.domain.repository

import com.example.mobile_medisupply.features.home.domain.model.ScheduledVisit
import com.example.mobile_medisupply.features.home.domain.model.VisitDetail
import kotlinx.coroutines.flow.Flow
import java.io.File

interface ScheduledVisitsRepository {
    fun getScheduledVisits(): List<ScheduledVisit>
}


interface ScheduledVisitsRepositoryImp {
    suspend fun getScheduledVisits(fecha: String? = null): Flow<List<ScheduledVisit>>

    suspend fun getVisitDetail(visitaId: String): Flow<VisitDetail?>

    suspend fun uploadVisitVideo(
        visitaId: String,
        file: File
    ): Flow<Boolean>

}
