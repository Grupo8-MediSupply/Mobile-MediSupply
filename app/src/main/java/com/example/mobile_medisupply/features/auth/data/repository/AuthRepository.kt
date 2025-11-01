package com.example.mobile_medisupply.features.auth.data.repository

import android.util.Base64
import com.example.mobile_medisupply.features.auth.data.local.SessionManager
import com.example.mobile_medisupply.features.auth.data.remote.AuthApi
import com.example.mobile_medisupply.features.auth.data.remote.LoginRequest
import com.example.mobile_medisupply.features.auth.domain.model.Region
import com.example.mobile_medisupply.features.auth.domain.model.UserRole
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.json.JSONObject

class AuthRepository
@Inject
constructor(private val authApi: AuthApi, private val sessionManager: SessionManager) {
    suspend fun login(email: String, password: String): Flow<Result<UserSession>> = flow {
        try {
            val response = authApi.login(LoginRequest(email, password))
            if (response.success) {
                val token = response.result.access_token
                val userSession = decodeToken(token)
                sessionManager.saveSession(userSession)
                emit(Result.success(userSession))
            } else {
                emit(Result.failure(Exception("Login failed")))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    fun getSession(): UserSession? = sessionManager.getSession()

    fun clearSession() = sessionManager.clearSession()

    private fun decodeToken(token: String): UserSession {
        val parts = token.split(".")
        require(parts.size >= 2) { "Invalid token format" }
        val payload = String(Base64.decode(parts[1], Base64.DEFAULT))
        val json = JSONObject(payload)

        return UserSession(
                token = token,
                userId = json.getString("sub"),
                email = json.getString("email"),
                role = UserRole.fromId(json.getString("role").toInt()),
                region = Region.fromId(json.getString("pais").toInt())
        )
    }
}

data class UserSession(
        val token: String,
        val userId: String,
        val email: String,
        val role: UserRole,
        val region: Region
)
