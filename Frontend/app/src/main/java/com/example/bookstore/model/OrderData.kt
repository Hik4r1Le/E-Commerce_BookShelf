package com.example.bookstore.model

import com.example.bookstore.R

data class OrderData(
    val id: Int,
    val title: String,
    val author: String,
    val price: Int,
    val quantity: Int,
    val imageRes: Int = R.drawable.book6,
    val status: OrderStatus,
    val actionText: String
)