package com.android.reclaim.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Profile(
    val id: String,
    @SerialName("full_name") var fullName: String? = null,
    @SerialName("sober_start_date") var soberStartDate: String? = null,
    @SerialName("created_at") var createdAt: String? = null,
    @SerialName("photo_url") var photoUrl: String? = null,
    var bio: String? = null
)
