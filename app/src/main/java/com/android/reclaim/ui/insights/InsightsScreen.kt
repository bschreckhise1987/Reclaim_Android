package com.android.reclaim.ui.insights

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.android.reclaim.ui.components.CravingTrendChart
import com.android.reclaim.ui.components.DailyUsageBarChart
import com.android.reclaim.ui.components.MoodDonutChart
import com.android.reclaim.ui.components.MoodTrendChart
import com.android.reclaim.ui.components.TriggerBarChart
import java.util.Locale

@Composable
fun InsightsScreen(
    userId: String?,
    viewModel: InsightsViewModel,
    modifier: Modifier = Modifier
) {
    LaunchedEffect(userId) {
        if (userId != null) {
            viewModel.load(userId)
        }
    }

    var moodDays by remember { mutableIntStateOf(7) }
    var cravingDays by remember { mutableIntStateOf(7) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 20.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Text(
            text = "Insights",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        if (viewModel.isLoading) {
            Text(
                text = "Loading your insights...",
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        viewModel.errorMessage?.let { message ->
            Text(
                text = message,
                color = MaterialTheme.colorScheme.error
            )
        }

        InsightsCard(title = "Weekly Summary") {
            SummaryRow("Check-Ins", "${viewModel.weeklyCheckIns} / 7")
            SummaryRow("Average Mood", viewModel.weeklyMoodAverage.toString())
            SummaryRow("Average Craving", viewModel.weeklyCravingAverage.toString())
            SummaryRow("Top Trigger", viewModel.weeklyTopTrigger)
        }

        InsightsCard(title = "Mood Trend") {
            val points = viewModel.moodTrend.takeLast(moodDays)

            if (points.isEmpty()) {
                EmptyStateText(
                    "No mood data yet.\nComplete a few check-ins to see your emotional trend."
                )
            } else {
                MoodTrendChart(points = points)
            }
        }

        InsightsCard(title = "Craving Intensity") {
            val points = viewModel.cravingTrend.takeLast(cravingDays)

            if (points.isEmpty()) {
                EmptyStateText(
                    "No craving data yet.\nLog cravings to see your intensity trend."
                )
            } else {
                CravingTrendChart(points = points)
            }
        }

        InsightsCard(title = "Trigger Frequency") {
            if (viewModel.triggerFrequency.isEmpty()) {
                EmptyStateText(
                    "No trigger data yet.\nAdd daily logs to track your triggers."
                )
            } else {
                TriggerBarChart(items = viewModel.triggerFrequency)
            }
        }

        InsightsCard(title = "Mood Distribution") {
            if (viewModel.moodDistribution.isEmpty()) {
                EmptyStateText("No mood distribution data yet.")
            } else {
                MoodDonutChart(items = viewModel.moodDistribution)
            }
        }

        InsightsCard(title = "Most Used Strategies") {
            if (viewModel.topStrategies.isEmpty()) {
                EmptyStateText("No strategy usage data yet.")
            } else {
                viewModel.topStrategies.forEach { item ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = item.name,
                            style = MaterialTheme.typography.bodyMedium
                        )

                        Text(
                            text = "${item.usageCount}",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }

        InsightsCard(title = "Most Effective Strategies") {
            if (viewModel.effectiveStrategies.isEmpty()) {
                EmptyStateText("No effectiveness data yet.")
            } else {
                viewModel.effectiveStrategies.forEach { item ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = item.name,
                            style = MaterialTheme.typography.bodyMedium
                        )

                        Text(
                            text = String.format(
                                Locale.US,
                                "%.1f ★",
                                item.avgScore
                            ),
                            color = Color(0xFFFFB300),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        InsightsCard(title = "Triggers With Rising Frequency") {
            if (viewModel.triggerTrends.isEmpty()) {
                EmptyStateText("No trigger trend data yet.")
            } else {
                viewModel.triggerTrends.forEach { item ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = item.triggerName,
                            style = MaterialTheme.typography.bodyMedium
                        )

                        Text(
                            text = if (item.change >= 0) {
                                "+${item.change}"
                            } else {
                                item.change.toString()
                            },
                            color = if (item.change >= 0) {
                                Color.Red
                            } else {
                                Color.Green
                            },
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        InsightsCard(title = "Top Strategies by Trigger") {
            if (viewModel.topStrategiesByTrigger.isEmpty()) {
                EmptyStateText("No trigger strategy data yet.")
            } else {
                viewModel.topStrategiesByTrigger.forEach { (trigger, strategies) ->
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp)
                    ) {
                        Text(
                            text = trigger,
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.SemiBold
                        )

                        if (strategies.isEmpty()) {
                            Text(
                                text = "No strategies recorded",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        } else {
                            strategies.forEach { strategy ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(
                                            start = 8.dp,
                                            top = 4.dp,
                                            bottom = 4.dp
                                        ),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = strategy.name,
                                        style = MaterialTheme.typography.bodyMedium
                                    )

                                    Text(
                                        text = "${strategy.uses} uses",
                                        style = MaterialTheme.typography.bodySmall,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        InsightsCard(title = "Strategy Usage — Last 2 Weeks") {
            if (viewModel.dailyStrategyUsage.isEmpty()) {
                EmptyStateText("No usage data yet.")
            } else {
                DailyUsageBarChart(points = viewModel.dailyStrategyUsage)
            }
        }

        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Composable
private fun InsightsCard(
    title: String,
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            content()
        }
    }
}

@Composable
private fun SummaryRow(
    label: String,
    value: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Text(
            text = value,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun EmptyStateText(message: String) {
    Text(
        text = message,
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
    )
}