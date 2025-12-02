package com.example.bookstore.model

data class Address(
    val id: String,
    val label: String,
    val recipient_name: String,
    val phone_number: String,
    val street: String,
    val district: String,
    val city: String
)