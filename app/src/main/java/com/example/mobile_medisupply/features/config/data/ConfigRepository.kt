package com.example.mobile_medisupply.features.config.data

import android.util.Log
import com.example.mobile_medisupply.features.config.data.local.ConfigManager
import com.example.mobile_medisupply.features.config.data.remote.ConfigApi
import com.example.mobile_medisupply.features.config.data.toDomain
import com.example.mobile_medisupply.features.config.domain.model.AppConfig
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ConfigRepository
@Inject
constructor(
        private val configApi: ConfigApi,
        private val configManager: ConfigManager
) {

    suspend fun fetchAndCacheConfig(token: String): AppConfig? {
        return try {
            val response = configApi.getConfiguration("Bearer $token")
            val config = if (response.success) response.toDomain() else null
            if (config != null) {
                configManager.saveConfig(config)
            }
            config
        } catch (e: Exception) {
            Log.w(TAG, "Error fetching configuration", e)
            null
        }
    }

    fun getCachedConfig(): AppConfig? = configManager.getConfig()

    fun clearCachedConfig() {
        configManager.clear()
    }

    companion object {
        private const val TAG = "ConfigRepository"
    }
}
