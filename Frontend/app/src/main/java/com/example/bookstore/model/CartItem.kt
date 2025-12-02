package com.example.bookstore.model

data class CartItem(
    val id: String,
    val quantity: Int,
    val is_in_stock: Boolean,
    val stock: Stock
)