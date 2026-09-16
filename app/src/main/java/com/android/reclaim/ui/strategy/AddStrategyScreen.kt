package com.android.reclaim.ui.strategy

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.Switch
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.android.reclaim.data.model.CopingStrategy
import com.android.reclaim.data.model.StrategyType
import com.android.reclaim.util.TimestampParser
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddStrategyScreen(
    userId: String?,
    viewModel: StrategyViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var name by remember { mutableStateOf("") }
    var selectedType by remember { mutableStateOf(StrategyType.BREATHING) }
    var instructions by remember { mutableStateOf("") }
    var rating by remember { mutableIntStateOf(3) }
    var expanded by remember { mutableStateOf(false) }
    var isShared by remember { mutableStateOf(false)}

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Add Strategy",
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
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Create a coping strategy",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Add a strategy you can use when cravings or difficult moments occur.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Strategy Name") },
                placeholder = { Text("e.g. Take a short walk") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = {
                    expanded = !expanded
                }
            ) {
                OutlinedTextField(
                    value = selectedType.value
                        .replaceFirstChar { it.uppercase() },
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Category") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(
                            expanded = expanded
                        )
                    },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = {
                        expanded = false
                    }
                ) {
                    StrategyType.entries.forEach { type ->
                        DropdownMenuItem(
                            text = {
                                Text(
                                    type.value
                                        .replaceFirstChar { it.uppercase() }
                                )
                            },
                            onClick = {
                                selectedType = type
                                expanded = false
                            }
                        )
                    }
                }
            }

            OutlinedTextField(
                value = instructions,
                onValueChange = { instructions = it },
                label = { Text("Instructions") },
                placeholder = {
                    Text("Describe how to use this strategy")
                },
                modifier = Modifier.fillMaxWidth(),
                minLines = 5
            )

            Column {
                Text(
                    text = "How helpful is this strategy?",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Choose an initial rating. You can update it later.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(top = 6.dp)
                ) {
                    (1..5).forEach { star ->
                        IconButton(
                            onClick = {
                                rating = star
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = "Rate $star out of 5",
                                tint = if (star <= rating) {
                                    Color(0xFFFFB300)
                                } else {
                                    MaterialTheme.colorScheme.outlineVariant
                                }
                            )
                        }
                    }
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "Share with the Reclaim community",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Allow other users to discover and use this strategy.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Switch(
                    checked = isShared,
                    onCheckedChange = { isShared = it }
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    if (userId != null && name.isNotBlank()) {
                        val strategy = CopingStrategy(
                            id = UUID.randomUUID().toString(),
                            userId = userId,
                            name = name.trim(),
                            type = selectedType,
                            instructions = instructions.trim(),
                            rating = rating,
                            dateAdded = TimestampParser.formatIso8601(),
                            isShared = isShared
                        )

                        viewModel.addStrategy(userId, strategy)
                        onBack()
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = name.isNotBlank()
            ) {
                Text("Save Strategy")
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}