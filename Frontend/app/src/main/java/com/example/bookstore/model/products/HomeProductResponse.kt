package com.example.bookstore.model.products

import com.google.gson.annotations.SerializedName

data class HomeProductResponse(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("data")
    val data: List<HomeProductItem>
)

data class HomeProductItem(
    @SerializedName("product")
    val product: Product,

    @SerializedName("category")
    val category: Category
)

data class Product(
    @SerializedName("id")
    val id: String,

    @SerializedName("name")
    val name: String,

    @SerializedName("tag")
    val tag: String?,

    @SerializedName("author_name")
    val authorName: String,

    @SerializedName("discount")
    val discount: Double,

    @SerializedName("rating_avg")
    val ratingAvg: Double,

    @SerializedName("sold_count")
    val soldCount: Int,

    @SerializedName("price")
    val price: Double,

    @SerializedName("image_url")
    val imageUrl: String
)

data class Category(
    @SerializedName("id")
    val id: String,

    @SerializedName("name")
    val name: String,

    @SerializedName("slug")
    val slug: String,

    @SerializedName("description")
    val description: String?,

    @SerializedName("icon")
    val icon: String?
)
