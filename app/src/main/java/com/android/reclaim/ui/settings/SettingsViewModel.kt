package com.android.reclaim.ui.settings

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.reclaim.data.repository.AuthRepository
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val authRepository: AuthRepository = AuthRepository()
) : ViewModel() {

    var userEmail by mutableStateOf(authRepository.currentUserEmail ?: "")
    var errorMessage by mutableStateOf<String?>(null)

    fun loadUser() {
        userEmail = authRepository.currentUserEmail ?: ""
    }

    fun signOut(onSignedOut: () -> Unit) {
        viewModelScope.launch {
            try {
                authRepository.signOut()
                onSignedOut()
            } catch (e: Exception) {
                errorMessage = "Sign out failed."
            }
        }
    }
}
