package com.example.bookstore.model.products

import com.google.gson.annotations.SerializedName

data class ProductDetailResponse (
    val success: Boolean,
    val data: List<ProductDetailData>
)

data class ProductDetailData(
    val product: ProductDetailProduct?,
    val category: ProductDetailCategory?
)

data class ProductDetailProduct(
    val id: String?,
    val name: String?,
    val tag: String?,
    @SerializedName("author_name") val authorName: String?,
    val description: String?,
    val discount: Double?,
    @SerializedName("rating_avg") val ratingAvg: Double?,
    @SerializedName("rating_count") val ratingCount: Int?,
    @SerializedName("sold_count") val soldCount: Int?,
    val price: Double?,
    @SerializedName("image_url") val imageUrl: String?,
    @SerializedName("release_date") val releaseDate: String?,
    val stock: ProductStock?,
    val seller: ProductSeller?,
    val review: List<ProductReview>?
)

data class ProductStock(
    val id: String?,
    val quantity: Int?
)

data class ProductSeller(
    val profile: SellerProfile?
)

data class SellerProfile(
    val fullname: String?,
    @SerializedName("avatar_url") val avatarUrl: String?
)

data class ProductReview(
    val id: String?,
    val username: String?,
    val rating: Double?,
    val comment: String?,
    val user: ProductReviewUser?
)

data class ProductReviewUser(
    val select: ProductReviewSelect?
)

data class ProductReviewSelect(
    val username: String?
)

data class ProductDetailCategory(
    val id: String?,
    val name: String?,
    val slug: String?,
    val description: String?,
    val icon: String?
)