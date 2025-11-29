package com.example.bookstore.model.otp

import com.example.bookstore.model.login.LoginItem
import com.google.gson.annotations.SerializedName

data class VerifyOtpResponse (
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("data")
    val data: VerifyOtpItem,


)

data class VerifyOtpItem(
    @SerializedName("is_verified")
    val isVerified: String,

    @SerializedName("token")
    val token: String?,
)