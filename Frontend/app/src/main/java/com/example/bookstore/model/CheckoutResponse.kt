package com.example.bookstore.model

data class CheckoutResponse(
    val books: List<CheckoutBook>,
    val totalPrice: Int
)