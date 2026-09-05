package com.android.reclaim.ui.strategy

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.reclaim.data.model.CopingStrategy
import com.android.reclaim.data.model.RecentStrategyUse
import com.android.reclaim.data.repository.StrategyRepository
import com.android.reclaim.util.TimestampParser
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Date
import kotlin.math.abs

class StrategyViewModel(
    private val repository: StrategyRepository = StrategyRepository()
) : ViewModel() {

    var strategies by mutableStateOf<List<CopingStrategy>>(emptyList())
    var linkedStrategies by mutableStateOf<Map<String, List<String>>>(emptyMap())
    var recentStrategyUses by mutableStateOf<List<RecentStrategyUse>>(emptyList())

    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)
    var isLoaded by mutableStateOf(false)

    fun load(userId: String?) {
        if (userId == null) return
        if (isLoaded) return
        viewModelScope.launch {
            loadStrategies(userId)
            loadTriggerLinks(userId)
            loadRecentStrategyUses(userId)
            isLoaded = true
        }
    }

    suspend fun loadStrategies(userId: String) {
        isLoading = true
        try {
            strategies = repository.loadStrategies(userId)
        } catch (e: Exception) {
            errorMessage = "Failed to load strategies."
        } finally {
            isLoading = false
        }
    }

    suspend fun loadTriggerLinks(userId: String) {
        try {
            linkedStrategies = repository.loadTriggerLinks(userId)
        } catch (e: Exception) {
            errorMessage = "Failed to load trigger links."
        }
    }

    suspend fun loadRecentStrategyUses(userId: String) {
        try {
            recentStrategyUses = repository.loadRecentStrategyUses(userId)
        } catch (e: Exception) {
            errorMessage = "Failed to load recent strategy uses."
        }
    }

    fun addStrategy(userId: String, strategy: CopingStrategy) {
        viewModelScope.launch {
            try {
                repository.addStrategy(userId, strategy)
                strategies = listOf(strategy) + strategies
            } catch (e: Exception) {
                errorMessage = "Failed to add strategy."
            }
        }
    }

    fun updateStrategy(strategy: CopingStrategy) {
        viewModelScope.launch {
            try {
                repository.updateStrategy(strategy)
                strategies = strategies.map { if (it.id == strategy.id) strategy else it }
            } catch (e: Exception) {
                errorMessage = "Failed to update strategy."
            }
        }
    }

    fun deleteStrategy(strategy: CopingStrategy) {
        viewModelScope.launch {
            try {
                repository.deleteStrategy(strategy.id)
                strategies = strategies.filter { it.id != strategy.id }
            } catch (e: Exception) {
                errorMessage = "Failed to delete strategy."
            }
        }
    }

    fun linkStrategy(userId: String, strategy: CopingStrategy, triggers: List<String>) {
        viewModelScope.launch {
            try {
                repository.linkStrategy(userId, strategy.id, triggers)
                val newMap = linkedStrategies.toMutableMap()
                newMap[strategy.id] = triggers
                linkedStrategies = newMap
            } catch (e: Exception) {
                errorMessage = "Failed to link triggers."
            }
        }
    }

    fun logStrategyUse(userId: String, strategy: CopingStrategy) {
        viewModelScope.launch {
            try {
                repository.logStrategyUse(userId, strategy.id)
                loadRecentStrategyUses(userId)
            } catch (e: Exception) {
                errorMessage = "Failed to log strategy use."
            }
        }
    }

    fun logEffectiveness(userId: String, strategy: CopingStrategy, trigger: String, score: Int) {
        viewModelScope.launch {
            try {
                repository.logEffectiveness(userId, strategy.id, trigger, score)
            } catch (e: Exception) {
                errorMessage = "Failed to log effectiveness."
            }
        }
    }

    fun strategiesForTrigger(trigger: String): List<CopingStrategy> {
        return strategies.filter { strategy ->
            linkedStrategies[strategy.id]?.contains(trigger) == true
        }
    }

    fun recommendedForTrigger(trigger: String, date: Date = Date()): List<CopingStrategy> {
        val cal = Calendar.getInstance().apply { time = date }
        val hour = cal.get(Calendar.HOUR_OF_DAY)

        val linked = strategiesForTrigger(trigger)

        val timeMatchedIds = recentStrategyUses.mapNotNull { use ->
            val useDate = TimestampParser.parse(use.usedAt) ?: return@mapNotNull null
            val useCal = Calendar.getInstance().apply { time = useDate }
            val useHour = useCal.get(Calendar.HOUR_OF_DAY)
            if (abs(useHour - hour) <= 2) use.strategyId else null
        }.toSet()

        return linked.sortedWith { a, b ->
            val aBoost = if (timeMatchedIds.contains(a.id)) 1 else 0
            val bBoost = if (timeMatchedIds.contains(b.id)) 1 else 0

            if (aBoost != bBoost) {
                bBoost.compareTo(aBoost)
            } else {
                b.rating.compareTo(a.rating)
            }
        }
    }

    suspend fun recommendedSmart(userId: String, trigger: String): List<CopingStrategy> {
        val linked = strategiesForTrigger(trigger)
        val scoreMap = try {
            repository.loadStrategyEffectivenessScores(userId, trigger)
        } catch (e: Exception) {
            emptyMap()
        }

        return linked.sortedWith { a, b ->
            val aScore = scoreMap[a.id] ?: 0
            val bScore = scoreMap[b.id] ?: 0

            if (aScore != bScore) {
                bScore.compareTo(aScore)
            } else {
                b.rating.compareTo(a.rating)
            }
        }
    }
}
