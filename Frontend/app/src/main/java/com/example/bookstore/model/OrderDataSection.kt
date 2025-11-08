package com.example.bookstore.model

data class OrderDataSection(
    val status: OrderStatus,
    val orders: List<OrderData>,
    val totalPrice: Int,
    val actionText: String
)