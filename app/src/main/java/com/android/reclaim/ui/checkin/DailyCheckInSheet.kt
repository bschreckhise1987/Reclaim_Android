package com.android.reclaim.ui.checkin

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.reclaim.util.MoodOptions

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DailyCheckInSheet(
    userId: String,
    viewModel: CheckInViewModel,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = {
            if (!viewModel.isSubmitting) {
                onDismiss()
            }
        }
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(top = 20.dp, bottom = 40.dp),
            verticalArrangement = Arrangement.spacedBy(28.dp)
        ) {

            // -----------------------------------------------------
            // Header
            // -----------------------------------------------------

            Text(
                text = "Daily Check-In",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 20.dp)
            )

            // -----------------------------------------------------
            // Mood Picker
            // -----------------------------------------------------

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(
                        MaterialTheme.colorScheme.surfaceVariant
                    )
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                Text(
                    text = "How are you feeling?",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )

                LazyVerticalGrid(
                    columns = GridCells.Fixed(4),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(410.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    userScrollEnabled = false
                ) {

                    items(MoodOptions.all) { mood ->

                        val selected =
                            viewModel.mood == "${mood.emoji} ${mood.label}"

                        val backgroundColor by animateColorAsState(
                            targetValue =
                                if (selected) {
                                    Color(0xFF007AFF).copy(alpha = 0.20f)
                                } else {
                                    MaterialTheme.colorScheme.surface
                                },
                            label = "moodBackground"
                        )

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable(enabled = !viewModel.isSubmitting) {
                                    viewModel.mood =
                                        "${mood.emoji} ${mood.label}"
                                },
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {

                            Box(
                                modifier = Modifier
                                    .size(64.dp)
                                    .clip(CircleShape)
                                    .background(backgroundColor),
                                contentAlignment = Alignment.Center
                            ) {

                                Text(
                                    text = mood.emoji,
                                    fontSize = 40.sp
                                )
                            }

                            Text(
                                text = mood.label,
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }

            // -----------------------------------------------------
            // Craving Level
            // -----------------------------------------------------

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(
                        MaterialTheme.colorScheme.surfaceVariant
                    )
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                Text(
                    text = "Craving Level",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Text(
                        text = "0",
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Slider(
                        value = viewModel.cravingLevel,
                        onValueChange = {
                            viewModel.cravingLevel = it
                        },
                        valueRange = 0f..10f,
                        steps = 9,
                        modifier = Modifier.weight(1f),
                        enabled = !viewModel.isSubmitting
                    )

                    Text(
                        text = "10",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                Text(
                    text = "Intensity: ${viewModel.cravingLevel.toInt()} / 10",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // -----------------------------------------------------
            // Notes
            // -----------------------------------------------------

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                Text(
                    text = "Notes (optional)",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )

                OutlinedTextField(
                    value = viewModel.notes,
                    onValueChange = {
                        viewModel.notes = it
                    },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 5,
                    enabled = !viewModel.isSubmitting,
                    shape = RoundedCornerShape(12.dp),
                    placeholder = {
                        Text("How was your day?")
                    }
                )
            }

            // -----------------------------------------------------
            // Error
            // -----------------------------------------------------

            viewModel.errorMessage?.let { message ->

                Text(
                    text = message,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    textAlign = TextAlign.Center
                )
            }

            // -----------------------------------------------------
            // Submit
            // -----------------------------------------------------

            Button(
                onClick = {
                    viewModel.submitCheckIn(userId) { success ->
                        if (!success) return@submitCheckIn
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .padding(horizontal = 16.dp),
                enabled = viewModel.canSubmit,
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF007AFF),
                    disabledContainerColor =
                        Color.Gray.copy(alpha = 0.4f)
                )
            ) {

                if (viewModel.isSubmitting) {

                    CircularProgressIndicator(
                        modifier = Modifier.size(22.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )

                } else {

                    Text(
                        text = "Submit Check-In",
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }

    // -------------------------------------------------------------
    // Success Alert
    // -------------------------------------------------------------

    if (viewModel.showSuccess) {

        AlertDialog(
            onDismissRequest = {
                viewModel.showSuccess = false
                onDismiss()
            },
            title = {
                Text(
                    text = "Success",
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text("Your check-in has been saved.")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.showSuccess = false
                        onDismiss()
                    }
                ) {
                    Text("OK")
                }
            }
        )
    }
}

