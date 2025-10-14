package com.example.bookstore.model

data class CartItemData(
    val id: Int,
    val title: String,
    val author: String,
    val price: Double,
    val resId: Int,
    val imageUrl: String,
    var quantity: Int = 1,
    var isChecked: Boolean = true
)
