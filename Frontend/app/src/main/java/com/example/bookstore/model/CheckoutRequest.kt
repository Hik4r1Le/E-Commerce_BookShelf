package com.example.bookstore.model

data class CheckoutRequest(
    val cart_id: List<String>
)