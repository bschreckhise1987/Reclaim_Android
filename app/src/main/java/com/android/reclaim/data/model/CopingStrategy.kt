package com.android.reclaim.data.model

import androidx.compose.ui.graphics.Color
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class StrategyType(val value: String) {
    @SerialName("breathing") BREATHING("breathing"),
    @SerialName("distraction") DISTRACTION("distraction"),
    @SerialName("mindfulness") MINDFULNESS("mindfulness"),
    @SerialName("physical") PHYSICAL("physical"),
    @SerialName("social") SOCIAL("social"),
    @SerialName("creative") CREATIVE("creative"),
    @SerialName("emotional") EMOTIONAL("emotional"),
    @SerialName("spiritual") SPIRITUAL("spiritual");

    val color: Color
        get() = when (this) {
            BREATHING -> Color(0xFF2196F3)
            DISTRACTION -> Color(0xFFFF9800)
            MINDFULNESS -> Color(0xFF009688)
            PHYSICAL -> Color(0xFFF44336)
            SOCIAL -> Color(0xFFE91E63)
            CREATIVE -> Color(0xFF9C27B0)
            EMOTIONAL -> Color(0xFFFFC107)
            SPIRITUAL -> Color(0xFF4CAF50)
        }

    companion object {
        fun fromString(type: String): StrategyType {
            return entries.firstOrNull { it.value.equals(type, ignoreCase = true) } ?: BREATHING
        }
    }
}

@Serializable
data class CopingStrategy(
    val id: String,
    @SerialName("user_id") val userId: String,
    var name: String,
    var type: StrategyType,
    var instructions: String,
    var rating: Int,
    @SerialName("date_added") val dateAdded: String,
    @SerialName("is_shared") val isShared: Boolean = false,
    @SerialName("linked_trigger") val linkedTrigger: String? = null
)
