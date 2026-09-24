package com.android.reclaim.ui.profile

import android.app.DatePickerDialog
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewModelScope
import coil.compose.AsyncImage
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileEditScreen(
    userId: String?,
    viewModel: ProfileViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val profile = viewModel.profile

    var name by remember {
        mutableStateOf(profile?.fullName ?: "")
    }

    var bio by remember {
        mutableStateOf(profile?.bio ?: "")
    }

    var soberDate by remember {
        mutableStateOf(
            parseDate(profile?.soberStartDate)
        )
    }

    var selectedPhotoBytes by remember {
        mutableStateOf<ByteArray?>(null)
    }

    var selectedPhotoPreview by remember {
        mutableStateOf<Bitmap?>(null)
    }

    var isSaving by remember {
        mutableStateOf(false)
    }

    var localError by remember {
        mutableStateOf<String?>(null)
    }

    /*
     * Android system photo picker.
     */
    val photoPickerLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.PickVisualMedia()
        ) { uri ->

            if (uri == null) return@rememberLauncherForActivityResult

            try {
                val inputStream =
                    context.contentResolver.openInputStream(uri)

                val bitmap =
                    BitmapFactory.decodeStream(inputStream)

                inputStream?.close()

                if (bitmap == null) {
                    localError = "Unable to read the selected photo."
                    return@rememberLauncherForActivityResult
                }

                val outputStream =
                    ByteArrayOutputStream()

                bitmap.compress(
                    Bitmap.CompressFormat.JPEG,
                    90,
                    outputStream
                )

                selectedPhotoBytes =
                    outputStream.toByteArray()

                selectedPhotoPreview = bitmap

            } catch (e: Exception) {
                localError = "Unable to select photo."
            }
        }

    /*
     * Keep fields synchronized when the profile is loaded.
     */
    LaunchedEffect(profile?.id) {
        if (profile != null) {
            name = profile.fullName ?: ""
            bio = profile.bio ?: ""
            soberDate = parseDate(profile.soberStartDate)
        }
    }

    /*
     * Date picker.
     */
    fun showDatePicker() {
        val calendar = Calendar.getInstance()

        soberDate?.let {
            calendar.time = it
        }

        DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->

                val selectedCalendar =
                    Calendar.getInstance().apply {
                        set(
                            year,
                            month,
                            dayOfMonth
                        )
                    }

                soberDate =
                    selectedCalendar.time
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Edit Profile")
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            if (!isSaving) {
                                onBack()
                            }
                        }
                    ) {
                        Icon(
                            imageVector =
                                Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->

        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .padding(bottom = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(
                modifier = Modifier.height(20.dp)
            )

            // ---------------------------------------------------------
            // Profile Photo
            // ---------------------------------------------------------

            Box(
                modifier = Modifier.size(140.dp),
                contentAlignment = Alignment.Center
            ) {

                when {
                    selectedPhotoPreview != null -> {

                        androidx.compose.foundation.Image(
                            bitmap =
                                selectedPhotoPreview!!
                                    .asImageBitmap(),
                            contentDescription =
                                "Selected profile photo",
                            modifier = Modifier
                                .size(120.dp)
                                .clip(CircleShape),
                            contentScale =
                                ContentScale.Crop
                        )
                    }

                    profile?.photoUrl != null -> {

                        AsyncImage(
                            model = profile.photoUrl,
                            contentDescription =
                                "Profile Picture",
                            modifier = Modifier
                                .size(120.dp)
                                .clip(CircleShape),
                            contentScale =
                                ContentScale.Crop
                        )
                    }

                    else -> {

                        Box(
                            modifier = Modifier
                                .size(120.dp)
                                .clip(CircleShape)
                                .background(
                                    MaterialTheme
                                        .colorScheme
                                        .surfaceVariant
                                ),
                            contentAlignment =
                                Alignment.Center
                        ) {
                            Text(
                                text = "👤",
                                style =
                                    MaterialTheme.typography
                                        .headlineLarge
                            )
                        }
                    }
                }
            }

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            TextButton(
                onClick = {
                    if (!isSaving) {
                        photoPickerLauncher.launch(
                            PickVisualMediaRequest(
                                ActivityResultContracts
                                    .PickVisualMedia
                                    .ImageOnly
                            )
                        )
                    }
                }
            ) {
                Icon(
                    imageVector =
                        Icons.Default.CameraAlt,
                    contentDescription = null
                )

                Spacer(
                    modifier = Modifier.size(8.dp)
                )

                Text("Change Photo")
            }

            Spacer(
                modifier = Modifier.height(20.dp)
            )

            // ---------------------------------------------------------
            // Full Name
            // ---------------------------------------------------------

            OutlinedTextField(
                value = name,
                onValueChange = {
                    name = it
                },
                label = {
                    Text("Full Name")
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                enabled = !isSaving
            )

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            // ---------------------------------------------------------
            // Bio
            // ---------------------------------------------------------

            OutlinedTextField(
                value = bio,
                onValueChange = {
                    bio = it
                },
                label = {
                    Text("Bio")
                },
                modifier = Modifier.fillMaxWidth(),
                minLines = 4,
                enabled = !isSaving
            )

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            // ---------------------------------------------------------
            // Sober Start Date
            // ---------------------------------------------------------

            OutlinedTextField(
                value = formatDate(soberDate),
                onValueChange = {},
                readOnly = true,
                label = {
                    Text("Sober Start Date")
                },
                trailingIcon = {
                    IconButton(
                        onClick = {
                            if (!isSaving) {
                                showDatePicker()
                            }
                        }
                    ) {
                        Icon(
                            imageVector =
                                Icons.Default.CalendarMonth,
                            contentDescription =
                                "Select sober start date"
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !isSaving
            )

            Spacer(
                modifier = Modifier.height(24.dp)
            )

            // ---------------------------------------------------------
            // Error
            // ---------------------------------------------------------

            val error =
                localError ?: viewModel.errorMessage

            if (error != null) {
                Text(
                    text = error,
                    color =
                        MaterialTheme.colorScheme.error,
                    style =
                        MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                )
            }

            // ---------------------------------------------------------
            // Save
            // ---------------------------------------------------------

            Button(
                onClick = {

                    if (userId == null) {
                        localError =
                            "Unable to save profile."
                        return@Button
                    }

                    isSaving = true
                    localError = null

                    // Launch the save operation using the
                    // ViewModel's coroutine scope.
                    viewModel.viewModelScope.launch {

                        val success =
                            viewModel.saveProfile(
                                userId = userId,
                                name = name.trim(),
                                bio = bio.trim(),
                                soberStartDate = soberDate,
                                photoBytes =
                                    selectedPhotoBytes
                            )

                        isSaving = false

                        if (success) {
                            onBack()
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !isSaving
            ) {

                if (isSaving) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp
                    )
                } else {
                    Text("Save Changes")
                }
            }
        }
    }
}

private fun parseDate(
    value: String?
): Date? {
    if (value.isNullOrBlank()) {
        return null
    }

    return try {
        SimpleDateFormat(
            "yyyy-MM-dd",
            Locale.US
        ).parse(value)
    } catch (_: Exception) {
        null
    }
}

private fun formatDate(
    date: Date?
): String {
    if (date == null) {
        return ""
    }

    return SimpleDateFormat(
        "MMM d, yyyy",
        Locale.US
    ).format(date)
}