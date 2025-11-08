package com.example.bookstore.data

import com.example.bookstore.R

enum class OrderStatus(val label: String) {
    PENDING("Chờ xác nhận"),
    WAIT_PICKUP("Chờ lấy hàng"),
    WAIT_DELIVERY("Chờ giao hàng"),
    DELIVERED("Đã giao")
}

data class Order(
    val id: Int,
    val title: String,
    val author: String,
    val price: Int,
    val quantity: Int,
    val imageRes: Int = R.drawable.book6,
    val status: OrderStatus,
    val actionText: String
)

data class OrderSection(
    val status: OrderStatus,
    val orders: List<Order>,
    val totalPrice: Int,
    val actionText: String
)