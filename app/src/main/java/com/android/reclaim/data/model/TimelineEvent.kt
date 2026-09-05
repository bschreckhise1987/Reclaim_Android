package com.android.reclaim.data.model

import java.util.Date

enum class TimelineEventType {
    CHECK_IN,
    DAILY_LOG,
    STREAK
}

data class HistoryDetailItem(
    val icon: String,
    val label: String,
    val value: String
)

data class TimelineEvent(
    val id: String,
    val date: Date,
    val type: TimelineEventType,
    val title: String,
    val subtitle: String?,
    val details: List<HistoryDetailItem>
)
