package com.example.bookstore.model.login

import com.google.gson.annotations.SerializedName

data class GoogleLoginRequest(
    @SerializedName("idToken")
    val idToken: String
)
