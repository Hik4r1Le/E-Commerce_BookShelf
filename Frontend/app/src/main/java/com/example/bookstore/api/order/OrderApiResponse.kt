package com.example.bookstore.api.order

import com.google.gson.annotations.SerializedName

data class OrderApiResponse(
    val id: Int,
    val title: String,
    val author: String,
    val price: Int,
    val quantity: Int,
    @SerializedName("status") val statusLabel: String
)