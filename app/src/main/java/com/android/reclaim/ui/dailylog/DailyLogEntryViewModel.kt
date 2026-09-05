package com.android.reclaim.ui.dailylog

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.reclaim.data.model.NewDailyLog
import com.android.reclaim.data.repository.DailyLogRepository
import com.android.reclaim.util.MoodOptions
import kotlinx.coroutines.launch

class DailyLogEntryViewModel(
    private val repository: DailyLogRepository = DailyLogRepository()
) : ViewModel() {

    var mood by mutableStateOf("")
    var trigger by mutableStateOf("")
    var cravingIntensity by mutableStateOf(0f)
    var reflectionNotes by mutableStateOf("")

    var isSubmitting by mutableStateOf(false)
    var showSuccess by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)

    val canSubmit: Boolean
        get() = mood.isNotEmpty() && !isSubmitting

    fun submitLog(userId: String, onFinished: (Boolean) -> Unit) {
        if (!canSubmit) return
        viewModelScope.launch {
            isSubmitting = true
            errorMessage = null
            try {
                val moodLabel = MoodOptions.all.firstOrNull { it.emoji == mood }?.label ?: ""
                val fullMood = if (moodLabel.isNotEmpty()) "$mood $moodLabel" else mood

                val payload = NewDailyLog(
                    userId = userId,
                    mood = fullMood,
                    trigger = trigger,
                    cravingIntensity = cravingIntensity.toInt(),
                    reflectionNotes = reflectionNotes.ifBlank { null }
                )

                repository.createLog(payload)
                showSuccess = true
                onFinished(true)
            } catch (e: Exception) {
                errorMessage = "Failed to submit daily log."
                onFinished(false)
            } finally {
                isSubmitting = false
            }
        }
    }
}
