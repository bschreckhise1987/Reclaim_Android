package com.android.reclaim.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TriggerStrategyLink(
    val id: String,
    @SerialName("user_id") val userId: String,
    @SerialName("trigger_name") val triggerName: String,
    @SerialName("strategy_id") val strategyId: String
)
