package com.example.bookstore.model.order

import com.google.gson.annotations.SerializedName

data class OrderResponse(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("data")
    val data: List<OrderDetailItem>
)

data class OrderDetailItem(
    @SerializedName("status")
    val status: String,

    @SerializedName("quantity")
    val quantity: Int,

    @SerializedName("total_price")
    val totalPrice: Double,

    @SerializedName("stock")
    val stock: OrderStock
)

data class OrderStock(
    @SerializedName("product")
    val product: OrderProduct
)

data class OrderProduct(
    @SerializedName("id")
    val id: String,

    @SerializedName("name")
    val name: String,

    @SerializedName("author_name")
    val authorName: String,

    @SerializedName("image_url")
    val imageUrl: String,

    @SerializedName("productCategory")
    val productCategory: List<OrderProductCategory>,

    @SerializedName("price")
    val price: Double,

    @SerializedName("discount")
    val discount: Double
)

data class OrderProductCategory(
    @SerializedName("category")
    val category: OrderCategoryName
)

data class OrderCategoryName(
    @SerializedName("name")
    val name: String
)