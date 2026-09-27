package com.android.reclaim.ui.profile

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.android.reclaim.util.TimestampParser

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileEditScreen(
    userId: String?,
    viewModel: ProfileViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    var name by remember { mutableStateOf(viewModel.profile?.fullName ?: "") }
    var bio by remember { mutableStateOf(viewModel.profile?.bio ?: "") }
    var soberDate by remember { mutableStateOf(viewModel.profile?.soberStartDate ?: "") }

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null && userId != null) {
            try {
                val bytes = context.contentResolver.openInputStream(uri)?.use { it.readBytes() }
                if (bytes != null) {
                    viewModel.uploadProfilePhoto(userId, bytes)
                }
            } catch (_: Exception) {}
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Edit Profile") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Profile Picture Avatar & Picker
            Box(
                modifier = Modifier
                    .size(110.dp)
                    .clip(CircleShape)
                    .clickable { photoPickerLauncher.launch("image/*") },
                contentAlignment = Alignment.Center
            ) {
                if (viewModel.profile?.photoUrl != null) {
                    AsyncImage(
                        model = viewModel.profile?.photoUrl,
                        contentDescription = "Profile Picture",
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.surfaceVariant),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "👤", style = MaterialTheme.typography.headlineLarge)
                    }
                }

                // Overlay Camera Icon / Spinner
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.scrim.copy(alpha = 0.35f)),
                    contentAlignment = Alignment.Center
                ) {
                    if (viewModel.isUploadingPhoto) {
                        CircularProgressIndicator(
                            color = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.size(32.dp)
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.CameraAlt,
                            contentDescription = "Change Photo",
                            tint = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedButton(
                onClick = { photoPickerLauncher.launch("image/*") },
                enabled = !viewModel.isUploadingPhoto
            ) {
                Icon(
                    imageVector = Icons.Default.CameraAlt,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(if (viewModel.isUploadingPhoto) "Uploading Photo..." else "Change Profile Picture")
            }

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Full Name") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = bio,
                onValueChange = { bio = it },
                label = { Text("Bio") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = soberDate,
                onValueChange = { soberDate = it },
                label = { Text("Sober Start Date (YYYY-MM-DD)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    if (userId != null) {
                        if (name != viewModel.profile?.fullName) viewModel.updateName(userId, name)
                        if (bio != viewModel.profile?.bio) viewModel.updateBio(userId, bio)
                        if (soberDate != viewModel.profile?.soberStartDate) {
                            val parsedDate = TimestampParser.parse(soberDate)
                            if (parsedDate != null) {
                                viewModel.updateSoberStartDate(userId, parsedDate)
                            }
                        }
                        onBack()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save Changes")
            }
        }
    }
}
