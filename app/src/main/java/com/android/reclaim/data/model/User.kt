package com.android.reclaim.data.model

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: String,
    val email: String
)
