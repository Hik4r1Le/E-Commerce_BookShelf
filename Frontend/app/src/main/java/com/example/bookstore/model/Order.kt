package com.example.bookstore.model

data class Order(
    val id: String,
    val customer: String,
    val date: String,
    var status: String,
    val total: String,
    val address: String,
    val products: List<OrderProduct>
)