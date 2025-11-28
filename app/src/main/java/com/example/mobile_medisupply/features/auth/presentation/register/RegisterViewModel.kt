package com.example.mobile_medisupply.features.auth.presentation.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobile_medisupply.features.auth.data.remote.CrearClienteRequest
import com.example.mobile_medisupply.features.auth.data.repository.AuthRepository
import com.example.mobile_medisupply.features.auth.data.repository.LoginErrorType
import com.example.mobile_medisupply.features.auth.data.repository.LoginException
import com.example.mobile_medisupply.features.auth.data.repository.Pais
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class RegisterViewModel @Inject constructor(private val authRepository: AuthRepository) :
        ViewModel() {

    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

    init {
        loadPaises()
    }

    private fun loadPaises() {
        // Países estáticos hardcoded (el endpoint /paises requiere autenticación)
        val paisesEstaticos = listOf(
            Pais(
                id = 10,
                codigoIso = "COL",
                nombre = "Colombia",
                moneda = "Peso colombiano",
                simboloMoneda = "$",
                zonaHoraria = "America/Bogota",
                idiomaOficial = "Español",
                reguladorSanitario = "INVIMA",
                siglaMoneda = "COP"
            ),
            Pais(
                id = 20,
                codigoIso = "MEX",
                nombre = "México",
                moneda = "Peso mexicano",
                simboloMoneda = "$",
                zonaHoraria = "America/Mexico_City",
                idiomaOficial = "Español",
                reguladorSanitario = null,
                siglaMoneda = "MXN"
            )
        )
        
        _uiState.value = _uiState.value.copy(paises = paisesEstaticos)
    }

    fun registrarCliente(
            nombre: String,
            identificacion: String,
            tipoIdentificacion: Int,
            tipoInstitucion: String,
            clasificacion: String,
            responsableContacto: String,
            paisId: Int,
            email: String,
            password: String
    ) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isRegistering = true, error = null)

            val request =
                    CrearClienteRequest(
                            nombre = nombre,
                            identificacion = identificacion,
                            tipoIdentificacion = tipoIdentificacion,
                            tipoInstitucion = tipoInstitucion,
                            clasificacion = clasificacion,
                            responsableContacto = responsableContacto,
                            pais = paisId,
                            email = email,
                            password = password
                    )

            authRepository.registrarCliente(request).collect { result ->
                result
                        .onSuccess { clienteResult ->
                            _uiState.value =
                                    _uiState.value.copy(
                                            isRegistering = false,
                                            registrationSuccess = true,
                                            registeredClientId = clienteResult.id
                                    )
                        }
                        .onFailure { throwable ->
                            val (errorMessage, errorType) =
                                    when (throwable) {
                                        is LoginException -> throwable.message to throwable.type
                                        else -> (throwable.message
                                                        ?: "Error desconocido") to
                                                        LoginErrorType.UNKNOWN
                                    }
                            _uiState.value =
                                    _uiState.value.copy(
                                            isRegistering = false,
                                            error = errorMessage,
                                            errorType = errorType
                                    )
                        }
            }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }

    fun onRegistrationNavigated() {
        _uiState.value = _uiState.value.copy(registrationSuccess = false)
    }
}

data class RegisterUiState(
        val paises: List<Pais> = emptyList(),
        val isRegistering: Boolean = false,
        val registrationSuccess: Boolean = false,
        val registeredClientId: String? = null,
        val error: String? = null,
        val errorType: LoginErrorType? = null
)
