package com.android.reclaim.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DailyLog(
    val id: String,
    @SerialName("user_id") val userId: String,
    val mood: String,
    val trigger: String,
    @SerialName("craving_intensity") val cravingIntensity: Int,
    @SerialName("reflection_notes") val reflectionNotes: String? = null,
    @SerialName("created_at") val createdAt: String
)

@Serializable
data class NewDailyLog(
    @SerialName("user_id") val userId: String,
    val mood: String,
    val trigger: String,
    @SerialName("craving_intensity") val cravingIntensity: Int,
    @SerialName("reflection_notes") val reflectionNotes: String? = null
)
