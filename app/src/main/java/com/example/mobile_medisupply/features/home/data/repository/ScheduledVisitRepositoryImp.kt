package com.example.mobile_medisupply.features.home.data.repository

import com.example.mobile_medisupply.features.home.data.mappers.toDomain
import com.example.mobile_medisupply.features.home.data.mappers.toDomainList
import com.example.mobile_medisupply.features.home.data.remote.VisitApi
import com.example.mobile_medisupply.features.home.domain.model.ScheduledVisit
import com.example.mobile_medisupply.features.home.domain.model.VisitDetail
import com.example.mobile_medisupply.features.home.domain.repository.ScheduledVisitsRepository
import com.example.mobile_medisupply.features.home.domain.repository.ScheduledVisitsRepositoryImp
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
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

    override suspend fun getVisitDetail(visitaId: String): Flow<VisitDetail?> = flow {
        try {
            val response = visitApi.obtenerDetalleVisita(visitaId)
            if (response.success && response.result != null) {
                emit(response.result.toDomain())
            } else {
                emit(null)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emit(null)
        }
    }

    override suspend fun uploadVisitVideo(
        visitaId: String,
        file: File
    ): Flow<Boolean> = flow {

        // crea el request body multipart
        val requestBody = file
            .asRequestBody("video/mp4".toMediaType())

        val multipart = MultipartBody.Part.createFormData(
            "video",
            file.name,
            requestBody
        )

        val response = visitApi.subirVideoVisita(visitaId, multipart)

        emit(response.success)
    }.catch { e ->
        e.printStackTrace()
        emit(false)
    }




}