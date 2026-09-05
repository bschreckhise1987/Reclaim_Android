package com.android.reclaim.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.android.reclaim.data.model.CravingTrendPoint
import com.android.reclaim.data.model.DailyUsagePoint
import com.android.reclaim.data.model.MoodDistributionPoint
import com.android.reclaim.data.model.MoodTrendPoint
import com.android.reclaim.data.model.TriggerFrequencyPoint

@Composable
fun MoodTrendChart(
    points: List<MoodTrendPoint>,
    modifier: Modifier = Modifier
) {
    if (points.isEmpty()) return

    val primaryColor = MaterialTheme.colorScheme.primary

    Canvas(modifier = modifier.height(200.dp).fillMaxWidth()) {
        val width = size.width
        val height = size.height

        val maxScore = 10f
        val minScore = 0f

        val spacing = width / (points.size.coerceAtLeast(2) - 1)

        val path = Path()
        points.forEachIndexed { i, pt ->
            val x = i * spacing
            val y = height - ((pt.score - minScore) / (maxScore - minScore) * height)
            if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
        }

        drawPath(
            path = path,
            color = primaryColor,
            style = Stroke(width = 4.dp.toPx())
        )

        points.forEachIndexed { i, pt ->
            val x = i * spacing
            val y = height - ((pt.score - minScore) / (maxScore - minScore) * height)
            drawCircle(
                color = primaryColor,
                radius = 6.dp.toPx(),
                center = Offset(x, y)
            )
        }
    }
}

@Composable
fun CravingTrendChart(
    points: List<CravingTrendPoint>,
    modifier: Modifier = Modifier
) {
    if (points.isEmpty()) return

    val color = MaterialTheme.colorScheme.tertiary

    Canvas(modifier = modifier.height(200.dp).fillMaxWidth()) {
        val width = size.width
        val height = size.height

        val maxIntensity = 10f
        val minIntensity = 0f

        val spacing = width / (points.size.coerceAtLeast(2) - 1)

        val path = Path()
        points.forEachIndexed { i, pt ->
            val x = i * spacing
            val y = height - ((pt.intensity - minIntensity) / (maxIntensity - minIntensity) * height)
            if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
        }

        drawPath(
            path = path,
            color = color,
            style = Stroke(width = 4.dp.toPx())
        )

        points.forEachIndexed { i, pt ->
            val x = i * spacing
            val y = height - ((pt.intensity - minIntensity) / (maxIntensity - minIntensity) * height)
            drawRect(
                color = color,
                topLeft = Offset(x - 4.dp.toPx(), y - 4.dp.toPx()),
                size = Size(8.dp.toPx(), 8.dp.toPx())
            )
        }
    }
}

@Composable
fun TriggerBarChart(
    items: List<TriggerFrequencyPoint>,
    modifier: Modifier = Modifier
) {
    if (items.isEmpty()) return

    val barColor = MaterialTheme.colorScheme.secondary

    Column(modifier = modifier.fillMaxWidth()) {
        val maxCount = (items.maxOfOrNull { it.count } ?: 1).toFloat()

        items.forEach { item ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = item.trigger,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.width(90.dp)
                )

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(20.dp)
                ) {
                    Canvas(modifier = Modifier.matchParentSize()) {
                        val barWidth = (item.count / maxCount) * size.width
                        drawRect(
                            color = barColor,
                            size = Size(barWidth, size.height)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "${item.count}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Composable
fun DailyUsageBarChart(
    points: List<DailyUsagePoint>,
    modifier: Modifier = Modifier
) {
    if (points.isEmpty()) return

    val barColor = MaterialTheme.colorScheme.primary

    Canvas(modifier = modifier.height(180.dp).fillMaxWidth()) {
        val maxCount = (points.maxOfOrNull { it.usageCount } ?: 1).toFloat()
        val width = size.width
        val height = size.height

        val barWidth = width / (points.size * 2)

        points.forEachIndexed { i, pt ->
            val x = i * (barWidth * 2) + barWidth / 2
            val h = (pt.usageCount / maxCount) * height
            drawRect(
                color = barColor,
                topLeft = Offset(x, height - h),
                size = Size(barWidth, h)
            )
        }
    }
}

@Composable
fun MoodDonutChart(
    items: List<MoodDistributionPoint>,
    modifier: Modifier = Modifier
) {
    if (items.isEmpty()) return

    val total = items.sumOf { it.count }.toFloat().coerceAtLeast(1f)

    val colors = listOf(
        Color(0xFF4CAF50), Color(0xFF2196F3), Color(0xFFFF9800),
        Color(0xFF9C27B0), Color(0xFFE91E63), Color(0xFF009688)
    )

    Column(modifier = modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        Canvas(modifier = Modifier.size(160.dp)) {
            var startAngle = -90f
            items.forEachIndexed { i, item ->
                val sweep = (item.count / total) * 360f
                val color = colors[i % colors.size]

                drawArc(
                    color = color,
                    startAngle = startAngle,
                    sweepAngle = sweep,
                    useCenter = false,
                    style = Stroke(width = 30.dp.toPx())
                )
                startAngle += sweep
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Column {
            items.forEachIndexed { i, item ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 2.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                    ) {
                        Canvas(modifier = Modifier.matchParentSize()) {
                            drawCircle(color = colors[i % colors.size])
                        }
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "${item.emoji} ${item.label}: ${item.count}",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}
