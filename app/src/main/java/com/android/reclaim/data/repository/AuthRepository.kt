package com.android.reclaim.data.repository

import com.android.reclaim.config.SupabaseConfig
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import io.ktor.client.HttpClient
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

class AuthRepository {
    private val auth = SupabaseConfig.client.auth

    val currentUserId: String?
        get() = auth.currentSessionOrNull()?.user?.id

    val currentUserEmail: String?
        get() = auth.currentSessionOrNull()?.user?.email

    val isAuthenticated: Boolean
        get() = auth.currentSessionOrNull() != null

    suspend fun signIn(emailInput: String, passwordInput: String) {
        auth.signInWith(Email) {
            email = emailInput
            password = passwordInput
        }
    }

    suspend fun signUp(emailInput: String, passwordInput: String) {
        auth.signUpWith(Email) {
            email = emailInput
            password = passwordInput
        }
    }

    suspend fun signOut() {
        auth.signOut()
    }

    suspend fun reauthenticate(passwordInput: String) {
        val email = currentUserEmail ?: throw IllegalStateException("User not logged in")
        signIn(email, passwordInput)
    }

    suspend fun updateEmail(newEmail: String) {
        auth.updateUser {
            email = newEmail
        }
    }

    suspend fun updatePassword(newPassword: String) {
        auth.updateUser {
            password = newPassword
        }
    }

    suspend fun deleteAccount() {
        val userId = currentUserId ?: throw IllegalStateException("No user logged in")
        val client = HttpClient()
        val url = "${SupabaseConfig.URL}/functions/v1/delete-user"

        val body = buildJsonObject {
            put("userId", userId)
        }

        val response = client.post(url) {
            contentType(ContentType.Application.Json)
            header("Authorization", "Bearer ${SupabaseConfig.ANON_KEY}")
            setBody(body.toString())
        }

        if (response.status.value != 200) {
            throw IllegalStateException("Failed to delete account: status ${response.status.value}")
        }
    }
}
