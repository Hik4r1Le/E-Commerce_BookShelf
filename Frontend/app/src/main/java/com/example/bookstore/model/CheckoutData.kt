package com.example.bookstore.model

data class CheckoutData(
    val address: Address?,
    val coupon: List<Coupon>?,
    val shipping: List<Shipping>?,
    val cart: List<CartItem>?
)