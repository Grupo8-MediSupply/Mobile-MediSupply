package com.example.mobile_medisupply.features.clients.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobile_medisupply.features.home.domain.model.VisitDetail
import com.example.mobile_medisupply.features.home.domain.repository.ScheduledVisitsRepositoryImp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class VisitDetailViewModel @Inject constructor(
    private val repository: ScheduledVisitsRepositoryImp
) : ViewModel() {

    private val _visitDetail = MutableStateFlow<VisitDetail?>(null)
    val visitDetail: StateFlow<VisitDetail?> = _visitDetail.asStateFlow()

    private val _uploading = MutableStateFlow(false)
    val uploading: StateFlow<Boolean> = _uploading.asStateFlow()

    private val _uploadSuccess = MutableStateFlow<Boolean?>(null)
    val uploadSuccess: StateFlow<Boolean?> = _uploadSuccess.asStateFlow()


    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    fun loadVisitDetail(visitId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            repository.getVisitDetail(visitId).collect { detail ->
                _visitDetail.value = detail
                _isLoading.value = false
            }
        }
    }

    fun uploadVideo(visitId: String, videoFile: File) {
        viewModelScope.launch {
            _uploading.value = true
            _uploadSuccess.value = null

            repository.uploadVisitVideo(visitId, videoFile)
                .collect { success ->
                    _uploadSuccess.value = success
                    _uploading.value = false
                }
        }
    }

}