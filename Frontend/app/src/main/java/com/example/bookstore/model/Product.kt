package com.example.bookstore.model

data class Product(
    val id: String,
    val name: String,
    val author_name: String,
    val image_url: String,
    val productCategory: ProductCategory,
    val price: Double,
    val discount: Double
)