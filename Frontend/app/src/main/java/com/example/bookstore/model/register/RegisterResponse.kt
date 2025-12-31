package com.example.bookstore.model.register

import com.example.bookstore.model.login.LoginItem
import com.google.gson.annotations.SerializedName

data class RegisterResponse(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("data")
    val data: RegisterItem,
)

data class RegisterItem(
    @SerializedName("new_user")
    val newUser: NewUser?,

    @SerializedName("is_account_verified")
    val isAccountVerified: Boolean,
)

data class NewUser(
    @SerializedName("id")
    val id: String,

    @SerializedName("email")
    val email: String,
)