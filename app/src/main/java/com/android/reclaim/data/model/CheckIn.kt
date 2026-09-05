package com.android.reclaim.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CheckIn(
    val id: String,
    @SerialName("user_id") val userId: String,
    val mood: String,
    @SerialName("craving_level") val cravingLevel: Int,
    val notes: String? = null,
    @SerialName("created_at") val createdAt: String
)

@Serializable
data class NewCheckIn(
    @SerialName("user_id") val userId: String,
    @SerialName("created_at") val createdAt: String,
    @SerialName("timezone_offset") val timezoneOffset: String,
    val mood: String,
    @SerialName("craving_level") val cravingLevel: Int,
    val notes: String? = null
)
