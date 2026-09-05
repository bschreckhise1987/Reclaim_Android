package com.android.reclaim.data.repository

import com.android.reclaim.config.SupabaseConfig
import com.android.reclaim.data.model.CopingStrategy
import com.android.reclaim.data.model.RecentStrategyUse
import com.android.reclaim.data.model.TriggerStrategyLink
import com.android.reclaim.util.TimestampParser
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Order
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import java.util.Date
import java.util.UUID

@Serializable
private data class StrategyEffectivenessRow(
    val strategy_id: String,
    val score: Int
)

class StrategyRepository {
    private val postgrest = SupabaseConfig.client.postgrest

    suspend fun loadStrategies(userId: String): List<CopingStrategy> {
        val list = postgrest["coping_strategies"]
            .select {
                filter { eq("user_id", userId) }
                order("date_added", order = Order.DESCENDING)
            }
            .decodeList<CopingStrategy>()

        return list.sortedByDescending {
            TimestampParser.parse(it.dateAdded) ?: Date(0)
        }
    }

    suspend fun addStrategy(userId: String, strategy: CopingStrategy) {
        val payload = buildJsonObject {
            put("id", strategy.id)
            put("user_id", userId)
            put("name", strategy.name)
            put("type", strategy.type.value)
            put("instructions", strategy.instructions)
            put("rating", strategy.rating.toString())
            put("date_added", TimestampParser.formatIso8601())
        }
        postgrest["coping_strategies"].insert(payload)
    }

    suspend fun updateStrategy(strategy: CopingStrategy) {
        val payload = buildJsonObject {
            put("name", strategy.name)
            put("type", strategy.type.value)
            put("instructions", strategy.instructions)
            put("rating", strategy.rating.toString())
        }
        postgrest["coping_strategies"].update(payload) {
            filter { eq("id", strategy.id) }
        }
    }

    suspend fun deleteStrategy(strategyId: String) {
        postgrest["coping_strategies"].delete {
            filter { eq("id", strategyId) }
        }
    }

    suspend fun loadTriggerLinks(userId: String): Map<String, List<String>> {
        val links = postgrest["trigger_strategies"]
            .select {
                filter { eq("user_id", userId) }
            }
            .decodeList<TriggerStrategyLink>()

        val map = mutableMapOf<String, MutableList<String>>()
        for (link in links) {
            map.getOrPut(link.strategyId) { mutableListOf() }.add(link.triggerName)
        }
        return map
    }

    suspend fun linkStrategy(userId: String, strategyId: String, triggers: List<String>) {
        postgrest["trigger_strategies"].delete {
            filter {
                eq("user_id", userId)
                eq("strategy_id", strategyId)
            }
        }

        if (triggers.isNotEmpty()) {
            val rows = triggers.map { trigger ->
                buildJsonObject {
                    put("id", UUID.randomUUID().toString())
                    put("user_id", userId)
                    put("trigger_name", trigger)
                    put("strategy_id", strategyId)
                }
            }
            postgrest["trigger_strategies"].insert(rows)
        }
    }

    suspend fun logStrategyUse(userId: String, strategyId: String) {
        val payload = buildJsonObject {
            put("user_id", userId)
            put("strategy_id", strategyId)
        }
        postgrest["strategy_usage"].insert(payload)
    }

    suspend fun logEffectiveness(userId: String, strategyId: String, trigger: String, score: Int) {
        val payload = buildJsonObject {
            put("id", UUID.randomUUID().toString())
            put("user_id", userId)
            put("strategy_id", strategyId)
            put("trigger_name", trigger)
            put("score", score.toString())
        }
        postgrest["strategy_effectiveness"].insert(payload)
    }

    suspend fun loadRecentStrategyUses(userId: String): List<RecentStrategyUse> {
        return postgrest["strategy_usage"]
            .select {
                filter { eq("user_id", userId) }
                order("used_at", order = Order.DESCENDING)
                range(0, 49)
            }
            .decodeList<RecentStrategyUse>()
    }

    suspend fun loadStrategyEffectivenessScores(userId: String, trigger: String): Map<String, Int> {
        val rows = postgrest["strategy_effectiveness"]
            .select {
                filter {
                    eq("user_id", userId)
                    eq("trigger_name", trigger)
                }
            }
            .decodeList<StrategyEffectivenessRow>()

        val grouped = rows.groupBy { it.strategy_id }
        return grouped.mapValues { (_, scoreList) ->
            scoreList.map { it.score }.average().toInt()
        }
    }
}
