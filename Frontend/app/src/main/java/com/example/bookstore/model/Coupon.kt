package com.example.bookstore.model

data class Coupon(
    val id: String,
    val code: String,
    val discount_type: String,
    val discount_value: Double,
    val start_date: String,
    val end_date: String
)