package com.android.reclaim.ui.insights

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.reclaim.data.model.CravingTrendPoint
import com.android.reclaim.data.model.DailyUsagePoint
import com.android.reclaim.data.model.EffectiveStrategyItem
import com.android.reclaim.data.model.MoodDistributionPoint
import com.android.reclaim.data.model.MoodTrendPoint
import com.android.reclaim.data.model.StrategyUsageCount
import com.android.reclaim.data.model.TopStrategyItem
import com.android.reclaim.data.model.TriggerFrequencyPoint
import com.android.reclaim.data.model.TriggerTrendItem
import com.android.reclaim.data.repository.CheckInRepository
import com.android.reclaim.data.repository.DailyLogRepository
import com.android.reclaim.data.repository.InsightsRepository
import com.android.reclaim.util.MoodOptions
import com.android.reclaim.util.TimestampParser
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.util.Calendar

class InsightsViewModel(
    private val checkInRepository: CheckInRepository = CheckInRepository(),
    private val dailyLogRepository: DailyLogRepository = DailyLogRepository(),
    private val insightsRepository: InsightsRepository = InsightsRepository()
) : ViewModel() {

    var moodTrend by mutableStateOf<List<MoodTrendPoint>>(emptyList())
    var cravingTrend by mutableStateOf<List<CravingTrendPoint>>(emptyList())
    var triggerFrequency by mutableStateOf<List<TriggerFrequencyPoint>>(emptyList())
    var moodDistribution by mutableStateOf<List<MoodDistributionPoint>>(emptyList())

    var weeklyCheckIns by mutableStateOf(0)
    var weeklyMoodAverage by mutableStateOf(0)
    var weeklyCravingAverage by mutableStateOf(0)
    var weeklyTopTrigger by mutableStateOf("None")

    var topStrategies by mutableStateOf<List<StrategyUsageCount>>(emptyList())
    var dailyStrategyUsage by mutableStateOf<List<DailyUsagePoint>>(emptyList())
    var effectiveStrategies by mutableStateOf<List<EffectiveStrategyItem>>(emptyList())
    var triggerTrends by mutableStateOf<List<TriggerTrendItem>>(emptyList())
    var topStrategiesByTrigger by mutableStateOf<Map<String, List<TopStrategyItem>>>(emptyMap())

    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)
    var isLoaded by mutableStateOf(false)

    fun load(userId: String?) {
        if (userId == null) return
        if (isLoaded) return
        isLoaded = true
        viewModelScope.launch {
            loadInsights(userId)
        }
    }

    suspend fun loadInsights(userId: String) {
        isLoading = true
        errorMessage = null
        try {
            coroutineScope {
                val checkInsTask = async { checkInRepository.getCheckInsHistory(userId, 0, 100) }
                val logsTask = async { dailyLogRepository.fetchLogs(userId) }
                val topStratTask = async { insightsRepository.loadTopStrategies(userId) }
                val dailyStratTask = async { insightsRepository.loadDailyStrategyUsage(userId) }
                val effectiveTask = async { insightsRepository.loadStrategyEffectiveness(userId) }
                val trendsTask = async { insightsRepository.loadTriggerTrends(userId) }

                val checkIns = checkInsTask.await()
                val logs = logsTask.await()
                topStrategies = try { topStratTask.await() } catch (_: Exception) { emptyList() }
                dailyStrategyUsage = try { dailyStratTask.await() } catch (_: Exception) { emptyList() }
                effectiveStrategies = try { effectiveTask.await() } catch (_: Exception) { emptyList() }
                triggerTrends = try { trendsTask.await() } catch (_: Exception) { emptyList() }

                buildMoodTrend(checkIns)
                buildCravingTrend(logs)
                buildTriggerFrequency(logs)
                buildMoodDistribution(logs)
                buildWeeklySummary(checkIns, logs)

                val map = mutableMapOf<String, List<TopStrategyItem>>()
                for (item in triggerTrends) {
                    val list = try {
                        insightsRepository.loadTopStrategiesForTrigger(userId, item.triggerName)
                    } catch (_: Exception) { emptyList() }
                    map[item.triggerName] = list
                }
                topStrategiesByTrigger = map
            }
        } catch (e: Exception) {
            errorMessage = "Failed to load insights"
        } finally {
            isLoading = false
        }
    }

    private fun buildMoodTrend(checkIns: List<com.android.reclaim.data.model.CheckIn>) {
        moodTrend = checkIns.mapNotNull { c ->
            val date = TimestampParser.parse(c.createdAt) ?: return@mapNotNull null
            val emoji = c.mood.split(" ").firstOrNull() ?: ""
            val score = MoodOptions.score(emoji)
            MoodTrendPoint(date, score)
        }.sortedBy { it.date }
    }

    private fun buildCravingTrend(logs: List<com.android.reclaim.data.model.DailyLog>) {
        cravingTrend = logs.mapNotNull { log ->
            val date = TimestampParser.parse(log.createdAt) ?: return@mapNotNull null
            CravingTrendPoint(date, log.cravingIntensity)
        }.sortedBy { it.date }
    }

    private fun buildTriggerFrequency(logs: List<com.android.reclaim.data.model.DailyLog>) {
        val counts = logs.groupBy { it.trigger }
            .map { (trigger, list) -> TriggerFrequencyPoint(trigger, list.size) }
            .sortedByDescending { it.count }
        triggerFrequency = counts
    }

    private fun buildMoodDistribution(logs: List<com.android.reclaim.data.model.DailyLog>) {
        val emojis = logs.mapNotNull { log -> log.mood.split(" ").firstOrNull() }
        val grouped = emojis.groupBy { it }.map { (emoji, list) ->
            val label = MoodOptions.all.firstOrNull { it.emoji == emoji }?.label ?: "Unknown"
            MoodDistributionPoint(emoji, label, list.size)
        }.sortedByDescending { it.count }
        moodDistribution = grouped
    }

    private fun buildWeeklySummary(
        checkIns: List<com.android.reclaim.data.model.CheckIn>,
        logs: List<com.android.reclaim.data.model.DailyLog>
    ) {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.DAY_OF_WEEK, calendar.firstDayOfWeek)
        val startOfWeek = calendar.time

        val weeklyCheckInsList = checkIns.filter {
            val date = TimestampParser.parse(it.createdAt) ?: return@filter false
            date >= startOfWeek
        }

        weeklyCheckIns = weeklyCheckInsList.size

        val moodScores = weeklyCheckInsList.map { c ->
            val emoji = c.mood.split(" ").firstOrNull() ?: ""
            MoodOptions.score(emoji)
        }
        weeklyMoodAverage = if (moodScores.isNotEmpty()) moodScores.average().toInt() else 0

        val weeklyLogs = logs.filter {
            val date = TimestampParser.parse(it.createdAt) ?: return@filter false
            date >= startOfWeek
        }

        val cravings = weeklyLogs.map { it.cravingIntensity }
        weeklyCravingAverage = if (cravings.isNotEmpty()) cravings.average().toInt() else 0

        val topTrig = weeklyLogs.groupBy { it.trigger }
            .maxByOrNull { it.value.size }?.key
        weeklyTopTrigger = topTrig ?: "None"
    }
}
