package com.example.mobile_medisupply.features.auth.data.local

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.example.mobile_medisupply.features.auth.data.repository.UserSession
import com.google.gson.Gson
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionManager
@Inject
constructor(@ApplicationContext private val context: Context, private val gson: Gson) {
    private val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
    private val prefs =
            EncryptedSharedPreferences.create(
                    "secure_prefs",
                    masterKeyAlias,
                    context,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            )

    fun saveSession(session: UserSession) {
        prefs.edit().putString(KEY_SESSION, gson.toJson(session)).apply()
    }

    fun getSession(): UserSession? {
        val sessionJson = prefs.getString(KEY_SESSION, null)
        return sessionJson?.let { gson.fromJson(it, UserSession::class.java) }
    }

    fun clearSession() {
        prefs.edit().clear().apply()
    }

    companion object {
        private const val KEY_SESSION = "user_session"
    }
}
