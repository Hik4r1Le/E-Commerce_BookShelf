package com.example.bookstore.model.otp

import com.google.gson.annotations.SerializedName

data class VerifyOtpRequest(
    @SerializedName("email")
    val email: String,

    @SerializedName("otp")
    val otp: Int,

    @SerializedName("type")
    val type: String,
)
