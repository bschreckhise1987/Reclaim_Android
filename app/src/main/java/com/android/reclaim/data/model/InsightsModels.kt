package com.android.reclaim.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.Date

data class MoodTrendPoint(
    val date: Date,
    val score: Int
)

data class CravingTrendPoint(
    val date: Date,
    val intensity: Int
)

data class TriggerFrequencyPoint(
    val trigger: String,
    val count: Int
)

data class MoodDistributionPoint(
    val emoji: String,
    val label: String,
    val count: Int
)

@Serializable
data class StrategyUsageCount(
    val id: String? = null,
    @SerialName("strategy_id") val strategyId: String,
    val name: String = "",
    @SerialName("usage_count") val usageCount: Int
)

@Serializable
data class DailyUsagePoint(
    val day: String,
    @SerialName("usage_count") val usageCount: Int
)

@Serializable
data class RecentStrategyUse(
    @SerialName("strategy_id") val strategyId: String,
    @SerialName("used_at") val usedAt: String
)

@Serializable
data class EffectiveStrategyItem(
    @SerialName("strategy_id") val strategyId: String,
    val name: String,
    @SerialName("avg_score") val avgScore: Double,
    val uses: Int
)

@Serializable
data class TriggerTrendItem(
    @SerialName("trigger_name") val triggerName: String,
    @SerialName("last_week") val lastWeek: Int,
    @SerialName("this_week") val thisWeek: Int,
    val change: Int
)

@Serializable
data class TopStrategyItem(
    @SerialName("strategy_id") val strategyId: String,
    val name: String,
    @SerialName("avg_score") val avgScore: Double,
    val uses: Int,
    val rating: Int
)

@Serializable
data class CheckInRow(
    @SerialName("created_at") val createdAt: String,
    val mood: String? = null
)
