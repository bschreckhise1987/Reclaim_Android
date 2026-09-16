package com.android.reclaim.ui.strategy

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CopingStrategyGuideScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        containerColor = Color(0xFFF5F9FD),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Coping Guide",
                        fontWeight = FontWeight.Medium
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
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFF5F9FD),
                    titleContentColor = Color(0xFF171C20),
                    navigationIconContentColor = Color(0xFF171C20)
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(Color(0xFFF5F9FD))
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(
                    horizontal = 16.dp,
                    vertical = 18.dp
                ),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            GuideCard(
                title = "Breathing",
                description = "Deep, slow breathing triggers the body's parasympathetic nervous system, reducing stress and acute cravings within minutes."
            )

            GuideCard(
                title = "Distraction",
                description = "Engage your mind in a high-focus activity (puzzles, reading, walking) to ride out a craving peak."
            )

            GuideCard(
                title = "Mindfulness",
                description = "Observe cravings as passing waves without judging them or acting on impulse (Urge Surfing)."
            )

            GuideCard(
                title = "Physical Activity",
                description = "Light to intense exercise releases endorphins and reduces stress hormones instantly."
            )

            GuideCard(
                title = "Social Support",
                description = "Reach out to a trusted friend, sponsor, or support community when experiencing intense triggers."
            )

            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@Composable
private fun GuideCard(
    title: String,
    description: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = Color(0xFFDDE5EC),
                shape = RoundedCornerShape(24.dp)
            )
            .padding(
                horizontal = 20.dp,
                vertical = 22.dp
            )
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF414B53)
        )

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = description,
            style = MaterialTheme.typography.bodyLarge,
            color = Color(0xFF414B53)
        )
    }
}