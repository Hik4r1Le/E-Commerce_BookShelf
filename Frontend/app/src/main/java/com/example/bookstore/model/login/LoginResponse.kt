package com.example.bookstore.model.login

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("data")
    val data: LoginItem,
)

data class LoginItem(
    @SerializedName("token")
    val token: String = "",
)
