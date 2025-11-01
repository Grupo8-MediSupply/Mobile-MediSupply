package com.example.mobile_medisupply.features.auth.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobile_medisupply.features.auth.data.repository.AuthRepository
import com.example.mobile_medisupply.features.auth.data.repository.UserSession
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class LoginViewModel @Inject constructor(private val authRepository: AuthRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun login(email: String, password: String) {
        if (_uiState.value.isLoading) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            authRepository.login(email, password).collect { result ->
                result.fold(
                        onSuccess = { session ->
                            _uiState.update {
                                it.copy(
                                        isLoading = false,
                                        isLoggedIn = true,
                                        userSession = session,
                                        error = null
                                )
                            }
                        },
                        onFailure = { throwable ->
                            val message = throwable.message ?: "Error al iniciar sesión"
                            _uiState.update {
                                it.copy(isLoading = false, error = message, userSession = null)
                            }
                        }
                )
            }
        }
    }

    fun onLoginNavigated() {
        _uiState.update { it.copy(isLoggedIn = false) }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }
}

data class LoginUiState(
        val isLoading: Boolean = false,
        val isLoggedIn: Boolean = false,
        val userSession: UserSession? = null,
        val error: String? = null
)
