package com.example.bookstore.model

data class BookDetailData(
    val id: Int,
    val title: String,
    val category: String,
    val author: String,
    val imageRes: Int,
    val description: String,
    val price: Double,
    val star: Float
)