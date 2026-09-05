package com.android.reclaim.ui.dailylog

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.reclaim.data.model.DailyLog
import com.android.reclaim.data.repository.DailyLogRepository
import kotlinx.coroutines.launch

class DailyLogViewModel(
    private val repository: DailyLogRepository = DailyLogRepository()
) : ViewModel() {

    var logs by mutableStateOf<List<DailyLog>>(emptyList())
    var allTriggers by mutableStateOf<List<String>>(emptyList())
    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)
    var isLoaded by mutableStateOf(false)

    fun load(userId: String?) {
        if (userId == null) return
        if (isLoaded) return
        viewModelScope.launch {
            loadLogs(userId)
        }
    }

    suspend fun loadLogs(userId: String) {
        isLoading = true
        errorMessage = null
        try {
            val list = repository.fetchLogs(userId)
            logs = list
            extractTriggers(list)
            isLoaded = true
        } catch (e: Exception) {
            errorMessage = "Failed to load daily logs."
        } finally {
            isLoading = false
        }
    }

    private fun extractTriggers(logsList: List<DailyLog>) {
        val unique = logsList.map { it.trigger }.filter { it.isNotBlank() }.toSet()
        allTriggers = unique.sorted()
    }
}
