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
import javax.inject.Inject

@HiltViewModel
class VisitDetailViewModel @Inject constructor(
    private val repository: ScheduledVisitsRepositoryImp
) : ViewModel() {

    private val _visitDetail = MutableStateFlow<VisitDetail?>(null)
    val visitDetail: StateFlow<VisitDetail?> = _visitDetail.asStateFlow()

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
}