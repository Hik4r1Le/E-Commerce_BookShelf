package com.example.bookstore.model.order

import com.google.gson.annotations.SerializedName

// Model cho API Tạo order
data class CreateOrderRequest(
    @SerializedName("order")
    val orders: List<OrderItemRequest>
)

data class OrderItemRequest(
    @SerializedName("address_id")
    val addressId: String,

    @SerializedName("coupon_id")
    val couponId: String?,

    @SerializedName("shipping_method_id")
    val shippingMethodId: String,

    @SerializedName("stock_id")
    val stockId: String,

    @SerializedName("quantity")
    val quantity: Int,

    @SerializedName("total_price")
    val totalPrice: Double
)
