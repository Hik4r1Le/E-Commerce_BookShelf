package com.example.bookstore.model.otp

import com.google.gson.annotations.SerializedName

data class ResendOtpRequest(
    @SerializedName("email")
    val email: String,

    @SerializedName("type")
    val type: String,
)
