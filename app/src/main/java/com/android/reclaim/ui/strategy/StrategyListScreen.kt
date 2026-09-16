package com.android.reclaim.ui.strategy

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.android.reclaim.data.model.CopingStrategy

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StrategyListScreen(
    userId: String?,
    viewModel: StrategyViewModel,
    onBack: () -> Unit,
    onOpenDetail: (CopingStrategy) -> Unit,
    onAddStrategy: () -> Unit,
    onOpenGuide: () -> Unit,
    modifier: Modifier = Modifier
) {
    LaunchedEffect(userId) {
        if (userId != null) {
            viewModel.load(userId)
        }
    }

    val categoryOrder = listOf(
        "Breathing",
        "Distraction",
        "Mindfulness",
        "Physical",
        "Emotional",
        "Social"
    )

    val groupedStrategies: Map<String, List<CopingStrategy>> =
        viewModel.strategies.groupBy { strategy ->
            normalizeCategory(strategy.type.value)
        }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Coping Strategies",
                        fontWeight = FontWeight.SemiBold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = onOpenGuide) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Help,
                            contentDescription = "Open coping guide"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddStrategy,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add strategy"
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            if (viewModel.strategies.isEmpty()) {
                EmptyStrategiesState()
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    item {
                        Text(
                            text = "Your Strategies",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(
                                top = 8.dp,
                                bottom = 4.dp
                            )
                        )
                    }

                    categoryOrder.forEach { category ->
                        val strategiesInCategory = groupedStrategies[category]
                            ?.sortedWith(
                                compareBy<CopingStrategy> {
                                    it.dateAdded
                                }.thenBy {
                                    it.name
                                }
                            )
                            .orEmpty()

                        if (strategiesInCategory.isNotEmpty()) {
                            item {
                                Text(
                                    text = category,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(top = 8.dp)
                                )
                            }

                            items(
                                items = strategiesInCategory,
                                key = { it.id }
                            ) { strategy ->
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            onOpenDetail(strategy)
                                        },
                                    shape = MaterialTheme.shapes.large,
                                    colors = CardDefaults.cardColors(
                                        containerColor = MaterialTheme.colorScheme.surface
                                    ),
                                    elevation = CardDefaults.cardElevation(
                                        defaultElevation = 1.dp
                                    )
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(16.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(42.dp)
                                                .clip(CircleShape)
                                                .background(strategy.type.color),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Star,
                                                contentDescription = null,
                                                tint = Color.White,
                                                modifier = Modifier.size(20.dp)
                                            )
                                        }

                                        Spacer(modifier = Modifier.width(12.dp))

                                        Column(
                                            modifier = Modifier.weight(1f)
                                        ) {
                                            Text(
                                                text = strategy.name,
                                                style = MaterialTheme.typography.titleMedium,
                                                fontWeight = FontWeight.SemiBold
                                            )

                                            Spacer(modifier = Modifier.size(4.dp))

                                            Text(
                                                text = strategy.type.value
                                                    .replaceFirstChar { it.uppercase() },
                                                style = MaterialTheme.typography.bodySmall,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )

                                            Row(
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.Star,
                                                    contentDescription = null,
                                                    tint = Color(0xFFFFB300),
                                                    modifier = Modifier.size(14.dp)
                                                )

                                                Spacer(modifier = Modifier.width(4.dp))

                                                Text(
                                                    text = "${strategy.rating}/5",
                                                    style = MaterialTheme.typography.bodySmall,
                                                    fontWeight = FontWeight.Medium
                                                )
                                            }
                                        }

                                        Icon(
                                            imageVector = Icons.Default.ChevronRight,
                                            contentDescription = "Open strategy",
                                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }
                            }
                        }
                    }

                    item {
                        Spacer(modifier = Modifier.size(80.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun EmptyStrategiesState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "No strategies yet",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.size(8.dp))

            Text(
                text = "Tap + to create your first coping strategy.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

private fun normalizeCategory(value: String): String {
    return when (value.lowercase()) {
        "physical activity",
        "physical_activity",
        "physical" -> "Physical"

        "social support",
        "social_support",
        "social" -> "Social"

        "breathing" -> "Breathing"
        "distraction" -> "Distraction"
        "mindfulness" -> "Mindfulness"
        "emotional" -> "Emotional"

        else -> value.replaceFirstChar { it.uppercase() }
    }
}