package com.android.reclaim.ui.history

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.reclaim.data.model.HistoryDetailItem
import com.android.reclaim.data.model.TimelineEvent
import com.android.reclaim.data.model.TimelineEventType
import com.android.reclaim.data.repository.CheckInRepository
import com.android.reclaim.data.repository.DailyLogRepository
import com.android.reclaim.data.repository.StreakRepository
import com.android.reclaim.util.TimestampParser
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class HistoryViewModel(
    private val checkInRepository: CheckInRepository = CheckInRepository(),
    private val dailyLogRepository: DailyLogRepository = DailyLogRepository(),
    private val streakRepository: StreakRepository = StreakRepository()
) : ViewModel() {

    var events by mutableStateOf<List<TimelineEvent>>(emptyList())
    var isLoading by mutableStateOf(false)
    var isPaginating by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)
    var isLoaded by mutableStateOf(false)

    private val pageSize = 25L
    private var checkInOffset = 0L
    private var logOffset = 0L
    private var streakOffset = 0L
    private var reachedEnd = false

    fun load(userId: String?) {
        if (userId == null) return
        if (isLoaded) return
        isLoaded = true
        viewModelScope.launch {
            loadHistory(userId, reset = true)
        }
    }

    suspend fun refresh(userId: String) {
        if (isPaginating) return
        loadHistory(userId, reset = true)
    }

    suspend fun loadMore(userId: String) {
        if (isPaginating || reachedEnd) return
        isPaginating = true
        loadHistory(userId, reset = false)
        isPaginating = false
    }

    private suspend fun loadHistory(userId: String, reset: Boolean) {
        if (reset) {
            checkInOffset = 0L
            logOffset = 0L
            streakOffset = 0L
            reachedEnd = false
        }

        isLoading = true
        errorMessage = null

        try {
            coroutineScope {
                val checkInsTask = async { checkInRepository.getCheckInsHistory(userId, checkInOffset, pageSize) }
                val logsTask = async { dailyLogRepository.fetchLogsHistory(userId, logOffset, pageSize) }
                val streaksTask = async { streakRepository.getStreaksHistory(userId, streakOffset, pageSize) }

                val checkIns = checkInsTask.await()
                val logs = logsTask.await()
                val streaks = streaksTask.await()

                if (checkIns.size < pageSize && logs.size < pageSize && streaks.size < pageSize) {
                    reachedEnd = true
                }

                checkInOffset += checkIns.size
                logOffset += logs.size
                streakOffset += streaks.size

                val newEvents = mutableListOf<TimelineEvent>()

                for (c in checkIns) {
                    val date = TimestampParser.parse(c.createdAt) ?: continue
                    val details = listOf(
                        HistoryDetailItem("face", "Mood", c.mood),
                        HistoryDetailItem("local_fire_department", "Craving", "${c.cravingLevel} / 10"),
                        HistoryDetailItem("notes", "Notes", c.notes ?: "None")
                    )
                    newEvents.add(
                        TimelineEvent(
                            id = c.id,
                            date = date,
                            type = TimelineEventType.CHECK_IN,
                            title = "Check‑In",
                            subtitle = c.mood,
                            details = details
                        )
                    )
                }

                for (log in logs) {
                    val date = TimestampParser.parse(log.createdAt) ?: continue
                    val details = listOf(
                        HistoryDetailItem("face", "Mood", log.mood),
                        HistoryDetailItem("bolt", "Trigger", log.trigger),
                        HistoryDetailItem("local_fire_department", "Craving", "${log.cravingIntensity} / 10"),
                        HistoryDetailItem("notes", "Reflection", log.reflectionNotes ?: "None")
                    )
                    newEvents.add(
                        TimelineEvent(
                            id = log.id,
                            date = date,
                            type = TimelineEventType.DAILY_LOG,
                            title = "Daily Log",
                            subtitle = log.mood,
                            details = details
                        )
                    )
                }

                for (s in streaks) {
                    val date = TimestampParser.parse(s.updatedAt) ?: continue
                    newEvents.add(
                        TimelineEvent(
                            id = s.id,
                            date = date,
                            type = TimelineEventType.STREAK,
                            title = "Streak Milestone",
                            subtitle = "🔥 Current: ${s.currentStreak} days • Longest: ${s.longestStreak} days",
                            details = emptyList()
                        )
                    )
                }

                val currentList = if (reset) emptyList() else events
                events = (currentList + newEvents).sortedByDescending { it.date }
            }
        } catch (e: Exception) {
            errorMessage = "Failed to load history."
        } finally {
            isLoading = false
        }
    }
}
