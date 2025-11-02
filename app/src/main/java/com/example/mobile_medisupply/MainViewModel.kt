package com.example.mobile_medisupply

import androidx.lifecycle.ViewModel
import com.example.mobile_medisupply.features.auth.data.repository.AuthRepository
import com.example.mobile_medisupply.features.auth.data.repository.UserSession
import com.example.mobile_medisupply.features.config.data.ConfigRepository
import com.example.mobile_medisupply.features.config.domain.model.AppConfig
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

@HiltViewModel
class MainViewModel
@Inject
constructor(
        private val authRepository: AuthRepository,
        private val configRepository: ConfigRepository
) : ViewModel() {

    private val _session = MutableStateFlow(authRepository.getSession())
    val session: StateFlow<UserSession?> = _session.asStateFlow()

    private val _config = MutableStateFlow(configRepository.getCachedConfig())
    val config: StateFlow<AppConfig?> = _config.asStateFlow()

    fun refreshSession() {
        _session.value = authRepository.getSession()
    }

    fun refreshConfig() {
        _config.value = configRepository.getCachedConfig()
    }

    fun clearSession() {
        authRepository.clearSession()
        _session.value = null
        _config.value = null
    }
}
