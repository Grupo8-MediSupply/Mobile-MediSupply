package com.example.mobile_medisupply.features.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobile_medisupply.features.home.domain.model.ScheduledVisit
import com.example.mobile_medisupply.features.home.domain.repository.ScheduledVisitsRepositoryImp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val scheduledVisitsRepository: ScheduledVisitsRepositoryImp
) : ViewModel() {

    private val _visits = MutableStateFlow<List<ScheduledVisit>>(emptyList())
    val visits: StateFlow<List<ScheduledVisit>> = _visits.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    fun loadVisits(fecha: String? = null) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                scheduledVisitsRepository.getScheduledVisits(fecha).collect { visitList ->
                    _visits.value = visitList
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _error.value = e.message ?: "Error desconocido"
            } finally {
                _isLoading.value = false
            }
        }
    }
}