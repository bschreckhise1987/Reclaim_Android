package com.android.reclaim.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.reclaim.data.model.CopingStrategy
import com.android.reclaim.ui.checkin.CheckInViewModel
import com.android.reclaim.ui.dailylog.DailyLogViewModel
import com.android.reclaim.ui.profile.ProfileViewModel
import com.android.reclaim.ui.strategy.StrategyViewModel
import com.android.reclaim.util.SoberTimeManager
import com.android.reclaim.util.TimestampParser

@Composable
fun HomeScreen(
    userId: String?,
    profileVM: ProfileViewModel,
    checkInVM: CheckInViewModel,
    dailyLogVM: DailyLogViewModel,
    strategyVM: StrategyViewModel,
    homeVM: HomeViewModel,
    onStartCheckIn: () -> Unit,
    onAddDailyLog: () -> Unit,
    onOpenStrategies: () -> Unit,
    onOpenStrategyDetail: (CopingStrategy) -> Unit,
    modifier: Modifier = Modifier
) {
    LaunchedEffect(userId) {
        if (userId != null) {
            profileVM.load(userId)
            checkInVM.load(userId)
            dailyLogVM.load(userId)
            strategyVM.load(userId)
            homeVM.loadRecommendedStrategies(userId, strategyVM)
        }
    }

    val milestones = listOf(
        "30d",
        "60d",
        "90d",
        "180d",
        "365d"
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(top = 20.dp),
        verticalArrangement = Arrangement.spacedBy(28.dp)
    ) {

        // ---------------------------------------------------------
        // Greeting
        // ---------------------------------------------------------

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = "Welcome back",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Text(
                text = "How are you feeling today?",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
        }

        // ---------------------------------------------------------
        // Recommended Strategies
        // ---------------------------------------------------------

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 2.dp
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Recommended Strategies Today",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(12.dp))

                if (homeVM.recommendedToday.isEmpty()) {
                    Text(
                        text = "No recommendations yet.\nUse strategies and log cravings to train your coach.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    )
                } else {
                    homeVM.recommendedToday.forEach { strategy ->

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onOpenStrategyDetail(strategy)
                                }
                                .padding(vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Box(
                                modifier = Modifier
                                    .size(12.dp)
                                    .clip(CircleShape)
                                    .background(strategy.type.color)
                            )

                            Spacer(modifier = Modifier.width(12.dp))

                            Text(
                                text = strategy.name,
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier.weight(1f)
                            )

                            Icon(
                                imageVector = Icons.Default.ChevronRight,
                                contentDescription = "View Strategy",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }

        // ---------------------------------------------------------
        // Quick Actions
        // ---------------------------------------------------------

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            if (!checkInVM.hasCheckedInToday) {

                Button(
                    onClick = onStartCheckIn,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF007AFF)
                    )
                ) {
                    Text(
                        text = "Start Daily Check-In",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.White
                    )
                }

            } else {

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFE8F5E9)
                    ),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 0.dp
                    )
                ) {
                    Text(
                        text = "Daily Check-In Completed",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF34C759),
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    )
                }
            }

            Button(
                onClick = onAddDailyLog,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFAF52DE)
                )
            ) {
                Text(
                    text = "Add Daily Log",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White
                )
            }

            Button(
                onClick = onOpenStrategies,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF34C759)
                )
            ) {
                Text(
                    text = "Coping Strategies",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White
                )
            }
        }

        // ---------------------------------------------------------
        // Sober Time
        // ---------------------------------------------------------

        profileVM.profile?.soberStartDate?.let { startString ->

            val startDate = TimestampParser.parse(startString)

            if (startDate != null) {

                val soberTime = SoberTimeManager.calculate(startDate)

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFE5E5EA)
                    ),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 4.dp
                    )
                ) {

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                vertical = 28.dp,
                                horizontal = 20.dp
                            ),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {

                        Text(
                            text = "Your Sober Time",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Text(
                            text = "${soberTime.years} years • " +
                                    "${soberTime.months} months • " +
                                    "${soberTime.days} days",
                            fontSize = 34.sp,
                            lineHeight = 40.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )

                        Text(
                            text = "Since ${TimestampParser.formatDateShort(startDate)}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        // ---------------------------------------------------------
        // Streak Card
        // ---------------------------------------------------------

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 2.dp
            )
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = "Current Streak",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "${profileVM.currentStreak} Days",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = "Longest Streak: ${profileVM.longestStreak} Days",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(16.dp))

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {

                    items(milestones) { milestone ->

                        val requiredDays =
                            milestone.removeSuffix("d").toIntOrNull() ?: 0

                        val unlocked =
                            profileVM.currentStreak >= requiredDays

                        Column(
                            modifier = Modifier.width(50.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {

                            Icon(
                                imageVector =
                                    if (unlocked) {
                                        Icons.Default.MilitaryTech
                                    } else {
                                        Icons.Default.Lock
                                    },
                                contentDescription =
                                    if (unlocked) {
                                        "$milestone milestone unlocked"
                                    } else {
                                        "$milestone milestone locked"
                                    },
                                modifier = Modifier.size(26.dp),
                                tint =
                                    if (unlocked) {
                                        milestoneColor(milestone)
                                    } else {
                                        MaterialTheme.colorScheme.outline.copy(
                                            alpha = 0.4f
                                        )
                                    }
                            )

                            Text(
                                text = milestone,
                                style = MaterialTheme.typography.labelSmall,
                                color =
                                    if (unlocked) {
                                        MaterialTheme.colorScheme.onSurfaceVariant
                                    } else {
                                        MaterialTheme.colorScheme.outline.copy(
                                            alpha = 0.4f
                                        )
                                    }
                            )
                        }
                    }
                }
            }
        }

        // ---------------------------------------------------------
        // Weekly Check-In Summary
        // ---------------------------------------------------------

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 2.dp
            )
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                Text(
                    text = "This Week",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Text(
                    text = "${checkInVM.daysCheckedInThisWeek} of 7 days",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {

                    (0..6).forEach { dayIndex ->

                        val checkedIn =
                            checkInVM.checkedInDays.contains(dayIndex)

                        Box(
                            modifier = Modifier
                                .size(14.dp)
                                .clip(CircleShape)
                                .background(
                                    if (checkedIn) {
                                        Color(0xFF34C759)
                                    } else {
                                        Color.Gray.copy(alpha = 0.3f)
                                    }
                                )
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(40.dp))
    }
}

// -------------------------------------------------------------
// Milestone Colors
// -------------------------------------------------------------

private fun milestoneColor(milestone: String): Color =
    when (milestone) {
        "30d" -> Color(0xFF8D6E63)
        "60d" -> Color(0xFFFF9500)
        "90d" -> Color.Gray
        "180d" -> Color(0xFFFFCC00)
        "365d" -> Color(0xFF007AFF)
        else -> Color.Gray
    }

