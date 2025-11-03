package com.example.mobile_medisupply.features.config.data.local

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.example.mobile_medisupply.features.config.domain.model.AppConfig
import com.google.gson.Gson
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ConfigManager
@Inject
constructor(@ApplicationContext context: Context, private val gson: Gson) {

    private val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
    private val prefs =
            EncryptedSharedPreferences.create(
                    "config_prefs",
                    masterKeyAlias,
                    context,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            )

    fun saveConfig(config: AppConfig) {
        prefs.edit().putString(KEY_CONFIG, gson.toJson(config)).apply()
    }

    fun getConfig(): AppConfig? {
        val configJson = prefs.getString(KEY_CONFIG, null) ?: return null
        return runCatching { gson.fromJson(configJson, AppConfig::class.java) }.getOrNull()
    }

    fun clear() {
        prefs.edit().remove(KEY_CONFIG).apply()
    }

    companion object {
        private const val KEY_CONFIG = "app_config"
    }
}
