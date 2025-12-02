package com.example.bookstore.model

data class Shipping(
    val id: String,
    val type: String,
    val shipping_fee: Double,
    val estimated_time_text: String
)