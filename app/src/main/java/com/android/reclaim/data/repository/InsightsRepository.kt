package com.android.reclaim.data.repository

import com.android.reclaim.config.SupabaseConfig
import com.android.reclaim.data.model.DailyUsagePoint
import com.android.reclaim.data.model.EffectiveStrategyItem
import com.android.reclaim.data.model.StrategyUsageCount
import com.android.reclaim.data.model.TopStrategyItem
import com.android.reclaim.data.model.TriggerTrendItem
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Order
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

@Serializable
private data class StrategyUsageCountRow(
    val strategy_id: String,
    val usage_count: Int,
    val coping_strategies: CopingStrategyName? = null
)

@Serializable
private data class CopingStrategyName(
    val id: String,
    val name: String
)

class InsightsRepository {
    private val postgrest = SupabaseConfig.client.postgrest

    suspend fun loadTopStrategies(userId: String): List<StrategyUsageCount> {
        val rows = postgrest["strategy_usage_counts"]
            .select {
                filter { eq("user_id", userId) }
                order("usage_count", order = Order.DESCENDING)
                range(0, 4)
            }
            .decodeList<StrategyUsageCountRow>()

        return rows.map {
            StrategyUsageCount(
                id = it.coping_strategies?.id ?: it.strategy_id,
                strategyId = it.strategy_id,
                name = it.coping_strategies?.name ?: "Strategy",
                usageCount = it.usage_count
            )
        }
    }

    suspend fun loadDailyStrategyUsage(userId: String): List<DailyUsagePoint> {
        return postgrest["strategy_usage_daily"]
            .select {
                filter { eq("user_id", userId) }
                order("day", order = Order.ASCENDING)
                range(0, 13)
            }
            .decodeList<DailyUsagePoint>()
    }

    suspend fun loadStrategyEffectiveness(userId: String): List<EffectiveStrategyItem> {
        return postgrest.rpc(
            "get_strategy_effectiveness_summary",
            buildJsonObject { put("uid", userId) }
        ).decodeList<EffectiveStrategyItem>()
    }

    suspend fun loadTriggerTrends(userId: String): List<TriggerTrendItem> {
        return postgrest.rpc(
            "get_trigger_trend",
            buildJsonObject { put("uid", userId) }
        ).decodeList<TriggerTrendItem>()
    }

    suspend fun loadTopStrategiesForTrigger(userId: String, trigger: String): List<TopStrategyItem> {
        return postgrest.rpc(
            "get_top_strategies_for_trigger",
            buildJsonObject {
                put("uid", userId)
                put("trig", trigger)
            }
        ).decodeList<TopStrategyItem>()
    }
}
