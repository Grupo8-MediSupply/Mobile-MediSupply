package com.example.mobile_medisupply.features.auth.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobile_medisupply.features.auth.data.repository.AuthRepository
import com.example.mobile_medisupply.features.auth.data.repository.UserSession
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class LoginViewModel @Inject constructor(private val authRepository: AuthRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            authRepository.login(email, password).collect { result ->
                result.fold(
                        onSuccess = { session ->
                            _uiState.update {
                                it.copy(isLoading = false, isLoggedIn = true, userSession = session)
                            }
                        },
                        onFailure = { exception ->
                            _uiState.update {
                                it.copy(isLoading = false, error = exception.message)
                            }
                        }
                )
            }
        }
    }
}

data class LoginUiState(
        val isLoading: Boolean = false,
        val isLoggedIn: Boolean = false,
        val userSession: UserSession? = null,
        val error: String? = null
)
