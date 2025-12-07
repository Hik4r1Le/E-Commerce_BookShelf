package com.example.bookstore.model.checkout

import com.google.gson.annotations.SerializedName

// Model cho API khi vào checkout
data class CheckoutReviewRequest(
    @SerializedName("cart_id")
    val cartIds: List<String>
)

