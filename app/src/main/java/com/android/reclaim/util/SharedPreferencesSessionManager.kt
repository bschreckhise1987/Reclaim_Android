package com.android.reclaim.util

import android.content.Context
import android.content.SharedPreferences
import io.github.jan.supabase.auth.SessionManager
import io.github.jan.supabase.auth.user.UserSession
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class SharedPreferencesSessionManager(context: Context) : SessionManager {
    private val prefs: SharedPreferences =
        context.applicationContext.getSharedPreferences("supabase_session", Context.MODE_PRIVATE)

    private val json = Json {
        encodeDefaults = true
        ignoreUnknownKeys = true
    }

    override suspend fun saveSession(session: UserSession) {
        val serialized = json.encodeToString(session)
        prefs.edit().putString("session", serialized).apply()
    }

    override suspend fun loadSession(): UserSession? {
        val sessionStr = prefs.getString("session", null) ?: return null
        return try {
            json.decodeFromString<UserSession>(sessionStr)
        } catch (_: Exception) {
            null
        }
    }

    override suspend fun deleteSession() {
        prefs.edit().remove("session").apply()
    }
}
