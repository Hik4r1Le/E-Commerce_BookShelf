package com.example.bookstore.model

import android.net.Uri

data class UserProfileUiState(
    val name: String = "",
    val email: String = "",
    val phone: String = "",
    val address: String = "",
    val avatarUri: Uri? = null,
    val isLoading: Boolean = false
)