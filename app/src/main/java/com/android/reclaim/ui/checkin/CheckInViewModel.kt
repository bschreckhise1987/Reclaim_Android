package com.android.reclaim.ui.checkin

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.reclaim.data.model.NewCheckIn
import com.android.reclaim.data.repository.CheckInRepository
import com.android.reclaim.util.MoodOptions
import com.android.reclaim.util.TimestampParser
import kotlinx.coroutines.launch

class CheckInViewModel(
    private val checkInRepository: CheckInRepository = CheckInRepository()
) : ViewModel() {

    var mood by mutableStateOf("")
    var cravingLevel by mutableStateOf(5f)
    var notes by mutableStateOf("")

    var isSubmitting by mutableStateOf(false)
    var showSuccess by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)

    var hasCheckedInToday by mutableStateOf(false)
    var daysCheckedInThisWeek by mutableStateOf(0)
    var checkedInDays by mutableStateOf<List<Int>>(emptyList())

    val canSubmit: Boolean
        get() = mood.isNotEmpty() && !isSubmitting

    fun load(userId: String?) {
        if (userId == null) return
        viewModelScope.launch {
            loadTodayStatus(userId)
            loadWeeklySummary(userId)
        }
    }

    suspend fun loadTodayStatus(userId: String) {
        try {
            hasCheckedInToday = checkInRepository.checkIfCheckedInToday(userId)
        } catch (e: Exception) {
            errorMessage = "Failed to load check-in data."
        }
    }

    suspend fun loadWeeklySummary(userId: String) {
        try {
            val days = checkInRepository.getWeeklyCheckedInDays(userId)
            checkedInDays = days
            daysCheckedInThisWeek = days.size
        } catch (e: Exception) {
            errorMessage = "Failed to load weekly summary."
        }
    }

    fun submitCheckIn(userId: String, onFinished: (Boolean) -> Unit) {
        if (!canSubmit) return
        viewModelScope.launch {
            isSubmitting = true
            errorMessage = null
            try {
                if (checkInRepository.checkIfCheckedInToday(userId)) {
                    hasCheckedInToday = true
                    isSubmitting = false
                    onFinished(false)
                    return@launch
                }

                val moodLabel = MoodOptions.all.firstOrNull { it.emoji == mood }?.label ?: ""
                val fullMood = if (moodLabel.isNotEmpty()) "$mood $moodLabel" else mood

                val payload = NewCheckIn(
                    userId = userId,
                    createdAt = TimestampParser.formatIso8601(),
                    timezoneOffset = TimestampParser.timezoneOffsetString(),
                    mood = fullMood,
                    cravingLevel = cravingLevel.toInt(),
                    notes = notes.ifBlank { null }
                )

                checkInRepository.submitCheckIn(payload)
                hasCheckedInToday = true
                showSuccess = true
                loadWeeklySummary(userId)
                onFinished(true)
            } catch (e: Exception) {
                errorMessage = "Failed to submit check-in."
                onFinished(false)
            } finally {
                isSubmitting = false
            }
        }
    }
}
