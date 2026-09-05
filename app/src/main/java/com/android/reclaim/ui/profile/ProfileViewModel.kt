package com.android.reclaim.ui.profile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.reclaim.data.model.Profile
import com.android.reclaim.data.repository.ProfileRepository
import com.android.reclaim.util.SoberTimeManager
import com.android.reclaim.util.TimestampParser
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ProfileViewModel(
    private val profileRepository: ProfileRepository = ProfileRepository()
) : ViewModel() {

    var profile by mutableStateOf<Profile?>(null)
    var currentStreak by mutableStateOf(0)
    var longestStreak by mutableStateOf(0)

    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)
    var isLoaded by mutableStateOf(false)

    fun load(userId: String?) {
        if (userId == null) return
        if (isLoaded) return
        viewModelScope.launch {
            loadProfile(userId)
        }
    }

    suspend fun loadProfile(userId: String) {
        isLoading = true
        errorMessage = null
        try {
            val p = profileRepository.getProfile(userId)
            profile = p
            calculateStreaks()
            isLoaded = true
        } catch (e: Exception) {
            errorMessage = "Failed to load profile."
        } finally {
            isLoading = false
        }
    }

    fun updateName(userId: String, name: String) {
        viewModelScope.launch {
            try {
                profileRepository.updateName(userId, name)
                profile = profile?.copy(fullName = name)
            } catch (e: Exception) {
                errorMessage = "Profile update failed."
            }
        }
    }

    fun updateBio(userId: String, bio: String) {
        viewModelScope.launch {
            try {
                profileRepository.updateBio(userId, bio)
                profile = profile?.copy(bio = bio)
            } catch (e: Exception) {
                errorMessage = "Profile update failed."
            }
        }
    }

    fun updateSoberStartDate(userId: String, date: Date) {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        val dateString = sdf.format(date)
        viewModelScope.launch {
            try {
                profileRepository.updateSoberStartDate(userId, dateString)
                profile = profile?.copy(soberStartDate = dateString)
                calculateStreaks()
            } catch (e: Exception) {
                errorMessage = "Profile update failed."
            }
        }
    }

    fun uploadProfilePhoto(userId: String, jpegBytes: ByteArray) {
        viewModelScope.launch {
            try {
                val url = profileRepository.uploadProfilePhoto(userId, jpegBytes)
                profile = profile?.copy(photoUrl = url)
            } catch (e: Exception) {
                errorMessage = "Profile update failed."
            }
        }
    }

    fun calculateStreaks() {
        val startString = profile?.soberStartDate ?: run {
            currentStreak = 0
            return
        }

        val startDate = TimestampParser.parse(startString) ?: run {
            currentStreak = 0
            return
        }

        val soberTime = SoberTimeManager.calculate(startDate)
        currentStreak = soberTime.totalDays
        longestStreak = maxOf(longestStreak, currentStreak)
    }

    fun clear() {
        profile = null
        currentStreak = 0
        longestStreak = 0
        isLoaded = false
    }
}
