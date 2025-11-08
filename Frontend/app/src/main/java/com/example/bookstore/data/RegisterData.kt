package com.example.bookstore.data

// Data class cho credentials
data class RegisterCredentials(
    val username: String = "",
    val fullname: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = ""
)

// Data class cho UI state
data class RegisterUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)