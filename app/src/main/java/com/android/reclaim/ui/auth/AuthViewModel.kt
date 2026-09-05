package com.android.reclaim.ui.auth

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.reclaim.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    val authRepository: AuthRepository = AuthRepository()
) : ViewModel() {

    var email by mutableStateOf("")
    var password by mutableStateOf("")

    private val _isAuthenticated = MutableStateFlow(authRepository.isAuthenticated)
    val isAuthenticated: StateFlow<Boolean> = _isAuthenticated.asStateFlow()

    var errorMessage by mutableStateOf<String?>(null)
    var isLoading by mutableStateOf(false)

    val currentUserId: String?
        get() = authRepository.currentUserId

    init {
        checkSession()
    }

    fun checkSession() {
        _isAuthenticated.value = authRepository.isAuthenticated
        if (_isAuthenticated.value) {
            email = authRepository.currentUserEmail ?: ""
        }
    }

    fun signIn() {
        if (email.isBlank() || password.isBlank()) {
            errorMessage = "Please enter email and password."
            return
        }
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            try {
                authRepository.signIn(email, password)
                _isAuthenticated.value = true
            } catch (e: Exception) {
                errorMessage = "Invalid email or password."
            } finally {
                isLoading = false
            }
        }
    }

    fun signUp() {
        if (email.isBlank() || password.isBlank()) {
            errorMessage = "Please enter email and password."
            return
        }
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            try {
                authRepository.signUp(email, password)
                _isAuthenticated.value = true
            } catch (e: Exception) {
                errorMessage = "Failed to create account."
            } finally {
                isLoading = false
            }
        }
    }

    fun signOut() {
        viewModelScope.launch {
            try {
                authRepository.signOut()
                _isAuthenticated.value = false
                email = ""
                password = ""
            } catch (e: Exception) {
                errorMessage = "Failed to sign out."
            }
        }
    }

    suspend fun reauthenticate(pwd: String) {
        authRepository.reauthenticate(pwd)
    }

    suspend fun updateEmail(newEmail: String) {
        authRepository.updateEmail(newEmail)
        email = newEmail
    }

    suspend fun updatePassword(newPassword: String) {
        authRepository.updatePassword(newPassword)
    }

    suspend fun deleteAccount() {
        authRepository.deleteAccount()
        _isAuthenticated.value = false
        email = ""
        password = ""
    }
}
