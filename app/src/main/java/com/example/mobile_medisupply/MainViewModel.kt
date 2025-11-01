package com.example.mobile_medisupply

import androidx.lifecycle.ViewModel
import com.example.mobile_medisupply.features.auth.data.repository.AuthRepository
import com.example.mobile_medisupply.features.auth.data.repository.UserSession
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

@HiltViewModel
class MainViewModel @Inject constructor(private val authRepository: AuthRepository) : ViewModel() {

    private val _session = MutableStateFlow(authRepository.getSession())
    val session: StateFlow<UserSession?> = _session.asStateFlow()

    fun refreshSession() {
        _session.value = authRepository.getSession()
    }

    fun clearSession() {
        authRepository.clearSession()
        _session.value = null
    }
}
