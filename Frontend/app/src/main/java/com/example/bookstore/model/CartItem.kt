package com.example.bookstore.model

data class CartItem(
    val id: Int,
    val name: String,
    val author: String,
    val price: Int,
    val imageRes: Int,
    val quantity: Int = 1,
    val checked: Boolean = false
)