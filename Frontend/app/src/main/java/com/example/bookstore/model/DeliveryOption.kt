package com.example.bookstore.model

enum class DeliveryOption(val title: String, val price: Int, val time: String) {
    NHANH("Nhanh", 40000, "2-3 ngày"),
    HOA_TOC("Hỏa tốc", 60000, "trong ngày"),
    TIET_KIEM("Tiết kiệm", 20000, "5-7 ngày")
}