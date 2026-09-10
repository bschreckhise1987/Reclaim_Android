package com.android.reclaim.ui.checkin

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.reclaim.data.model.NewCheckIn
import com.android.reclaim.data.repository.CheckInRepository
import com.android.reclaim.util.TimestampParser
import kotlinx.coroutines.launch

class CheckInViewModel(
    private val checkInRepository: CheckInRepository = CheckInRepository()
) : ViewModel() {

    // -------------------------------------------------------------
    // User Input
    // -------------------------------------------------------------

    var mood by mutableStateOf("")

    var cravingLevel by mutableStateOf(5f)

    var notes by mutableStateOf("")

    // -------------------------------------------------------------
    // UI State
    // -------------------------------------------------------------

    var isSubmitting by mutableStateOf(false)

    var showSuccess by mutableStateOf(false)

    var errorMessage by mutableStateOf<String?>(null)

    // -------------------------------------------------------------
    // Summary State
    // -------------------------------------------------------------

    var hasCheckedInToday by mutableStateOf(false)

    var daysCheckedInThisWeek by mutableStateOf(0)

    var checkedInDays by mutableStateOf<List<Int>>(emptyList())

    // -------------------------------------------------------------
    // Validation
    // -------------------------------------------------------------

    val canSubmit: Boolean
        get() = mood.isNotEmpty() && !isSubmitting

    // -------------------------------------------------------------
    // Load
    // -------------------------------------------------------------

    fun load(userId: String?) {
        if (userId == null) return

        viewModelScope.launch {
            loadTodayStatus(userId)
            loadWeeklySummary(userId)
        }
    }

    // -------------------------------------------------------------
    // Today's Status
    // -------------------------------------------------------------

    suspend fun loadTodayStatus(userId: String) {

        try {

            hasCheckedInToday =
                checkInRepository.checkIfCheckedInToday(userId)

        } catch (e: Exception) {

            errorMessage =
                "Failed to load check-in data."
        }
    }

    // -------------------------------------------------------------
    // Weekly Summary
    // -------------------------------------------------------------

    suspend fun loadWeeklySummary(userId: String) {

        try {

            val days =
                checkInRepository.getWeeklyCheckedInDays(userId)

            checkedInDays = days

            daysCheckedInThisWeek = days.size

        } catch (e: Exception) {

            errorMessage =
                "Failed to load weekly summary."
        }
    }

    // -------------------------------------------------------------
    // Submit Check-In
    // -------------------------------------------------------------

    fun submitCheckIn(
        userId: String,
        onFinished: (Boolean) -> Unit
    ) {

        if (!canSubmit) return

        viewModelScope.launch {

            isSubmitting = true

            errorMessage = null

            try {

                // Prevent duplicate check-ins for today.

                if (
                    checkInRepository
                        .checkIfCheckedInToday(userId)
                ) {

                    hasCheckedInToday = true

                    isSubmitting = false

                    onFinished(false)

                    return@launch
                }

                // The mood is already stored in the same
                // format used by iOS:
                //
                // "😊 Happy"

                val payload = NewCheckIn(
                    userId = userId,
                    createdAt =
                        TimestampParser.formatIso8601(),
                    timezoneOffset =
                        TimestampParser.timezoneOffsetString(),
                    mood = mood,
                    cravingLevel =
                        cravingLevel.toInt(),
                    notes =
                        notes.ifBlank { null }
                )

                checkInRepository.submitCheckIn(payload)

                hasCheckedInToday = true

                isSubmitting = false

                showSuccess = true

                loadWeeklySummary(userId)

                onFinished(true)

            } catch (e: Exception) {

                errorMessage =
                    "Failed to submit check-in."

                isSubmitting = false

                onFinished(false)
            }
        }
    }
}

