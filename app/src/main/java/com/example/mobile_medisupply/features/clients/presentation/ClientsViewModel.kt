package com.example.mobile_medisupply.features.clients.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobile_medisupply.features.clients.domain.model.ClientSummary
import com.example.mobile_medisupply.features.clients.domain.repository.ClientRepository
import com.example.mobile_medisupply.features.clients.domain.repository.ClientRepositoryImp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ClientsViewModel @Inject constructor(
    private val repository: ClientRepositoryImp
) : ViewModel() {

    private val _state = MutableStateFlow<Result<List<ClientSummary>>?>(null)
    val state: StateFlow<Result<List<ClientSummary>>?> = _state

    init {
        loadClients()
    }

    private fun loadClients() {
        viewModelScope.launch {
            repository.getClients().collect { result ->
                _state.value = result
            }
        }
    }

    // helpers
    fun retry() { loadClients() }
}