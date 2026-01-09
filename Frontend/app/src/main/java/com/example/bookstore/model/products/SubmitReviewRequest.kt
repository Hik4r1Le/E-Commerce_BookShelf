package com.example.bookstore.model.products


data class SubmitReviewRequest(
    val rating: Int,
    val comment: String
)

//Response model for getting all reviews
data class ReviewsResponse(
    val success: Boolean,
    val data: List<ReviewFromAPI>
)

//Review structure from API (before mapping to ProductCommentUI)
data class ReviewFromAPI(
    val id: String,
    val username: String?,
    val rating: Int,
    val comment: String,
    val user: UserSelect? = null
)

data class UserSelect(
    val username: String?
)