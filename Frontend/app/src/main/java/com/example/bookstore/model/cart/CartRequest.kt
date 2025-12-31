package com.example.bookstore.model.cart

import com.google.gson.annotations.SerializedName

// Model cho API Thêm một sản phẩm vào giỏ hàng (POST /api/v1/cart/details)
data class AddToCartRequest(
    @SerializedName("stock_id")
    val stockId: String,

    @SerializedName("quantity")
    val quantity: Int,

    @SerializedName("price_at_add")
    val priceAtAdd: Double
)

// Model cho API Cập nhập số lượng sản phẩm của 1 cart item (PATCH /api/v1/cart/details/<id>)
data class UpdateCartItemRequest(
    @SerializedName("quantity")
    val quantity: Int,

    @SerializedName("price_at_add")
    val priceAtAdd: Double
)