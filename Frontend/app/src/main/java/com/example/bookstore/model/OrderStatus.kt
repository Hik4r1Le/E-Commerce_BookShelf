package com.example.bookstore.model

enum class OrderStatus(val label: String) {
    PENDING("Chờ xác nhận"),
    WAIT_PICKUP("Chờ lấy hàng"),
    WAIT_DELIVERY("Chờ giao hàng"),
    DELIVERED("Đã giao")
}