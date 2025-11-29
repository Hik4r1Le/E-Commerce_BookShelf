package com.example.bookstore.model.reset_password

import com.google.gson.annotations.SerializedName

data class ResetPasswordRequest(
    @SerializedName("password")
    val password: String = "",
)