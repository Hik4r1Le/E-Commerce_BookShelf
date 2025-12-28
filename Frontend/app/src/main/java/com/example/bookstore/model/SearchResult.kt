package com.example.bookstore.model

data class SearchResult(
    val id: String,
    val name: String?,
    val author_name: String?,
    val price: String?,
    val image_url: String?
)
