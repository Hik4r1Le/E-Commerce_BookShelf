package com.example.bookstore.model.products

data class SubmitReviewRequest(
    val rating: Int,
    val comment: String
)