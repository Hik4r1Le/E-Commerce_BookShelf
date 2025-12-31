package com.example.bookstore.model.forgot_password

import com.google.gson.annotations.SerializedName

data class ForgotPasswordRequest (
    @SerializedName("email")
    val email: String = "",
)