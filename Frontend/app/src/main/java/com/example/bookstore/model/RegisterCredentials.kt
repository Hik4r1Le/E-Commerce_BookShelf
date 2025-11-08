package com.example.bookstore.model

data class RegisterCredentials(
    val username: String = "",
    val fullname: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = ""
)