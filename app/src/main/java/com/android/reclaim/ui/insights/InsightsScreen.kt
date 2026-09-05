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
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Text(
            text = "Insights",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        // Weekly Summary Card
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "Weekly Summary", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(12.dp))
                SummaryRow("Check‑Ins", "${viewModel.weeklyCheckIns} / 7")
                SummaryRow("Average Mood", viewModel.weeklyMoodAverage.toString())
                SummaryRow("Average Craving", viewModel.weeklyCravingAverage.toString())
                SummaryRow("Top Trigger", viewModel.weeklyTopTrigger)
            }
        }

        // Mood Trend
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "Mood Trend", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))

                val filteredMood = viewModel.moodTrend.takeLast(moodDays)
                if (filteredMood.isEmpty()) {
                    EmptyStateText("No mood data yet.\nComplete a few check‑ins to see your emotional trend.")
                } else {
                    MoodTrendChart(points = filteredMood)
                }
            }
        }

        // Craving Trend
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "Craving Intensity", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))

                val filteredCraving = viewModel.cravingTrend.takeLast(cravingDays)
                if (filteredCraving.isEmpty()) {
                    EmptyStateText("No craving data yet.\nLog cravings to see your intensity trend.")
                } else {
                    CravingTrendChart(points = filteredCraving)
                }
            }
        }

        // Trigger Frequency
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "Trigger Frequency", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))

                if (viewModel.triggerFrequency.isEmpty()) {
                    EmptyStateText("No trigger data yet.\nAdd daily logs to track your triggers.")
                } else {
                    TriggerBarChart(items = viewModel.triggerFrequency)
                }
            }
        }

        // Mood Distribution
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "Mood Distribution", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))

                if (viewModel.moodDistribution.isEmpty()) {
                    EmptyStateText("No mood distribution data yet.")
                } else {
                    MoodDonutChart(items = viewModel.moodDistribution)
                }
            }
        }

        // Most Effective Strategies (30 Days)
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "Most Effective Strategies (30 Days)", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))

                if (viewModel.effectiveStrategies.isEmpty()) {
                    EmptyStateText("No effectiveness data yet.")
                } else {
                    viewModel.effectiveStrategies.forEach { item ->
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = item.name, style = MaterialTheme.typography.bodyMedium)
                            Text(text = "${String.format(Locale.US, "%.1f", item.avgScore)} ★", color = Color(0xFFFFB300), fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // Triggers With Rising Frequency
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "Triggers With Rising Frequency", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))

                if (viewModel.triggerTrends.isEmpty()) {
                    EmptyStateText("No trigger trend data yet.")
                } else {
                    viewModel.triggerTrends.forEach { item ->
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = item.triggerName, style = MaterialTheme.typography.bodyMedium)
                            Text(
                                text = if (item.change >= 0) "+${item.change}" else "${item.change}",
                                color = if (item.change >= 0) Color.Red else Color.Green,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }

        // Strategy Usage (Last 2 Weeks)
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "Strategy Usage (Last 2 Weeks)", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))

                if (viewModel.dailyStrategyUsage.isEmpty()) {
                    EmptyStateText("No usage data yet.")
                } else {
                    DailyUsageBarChart(points = viewModel.dailyStrategyUsage)
                }
            }
        }

        Spacer(modifier = Modifier.height(40.dp))
    }
}

@Composable
private fun SummaryRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(text = value, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
private fun EmptyStateText(msg: String) {
    Text(
        text = msg,
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp)
    )
}
