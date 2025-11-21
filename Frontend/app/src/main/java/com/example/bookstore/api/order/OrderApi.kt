package com.example.bookstore.api.order

import com.example.bookstore.api.order.OrderApiResponse
import retrofit2.http.GET
import retrofit2.http.Header

interface OrderApi {
    @GET("order")
    suspend fun getOrders(@Header("Authorization") token: String): List<OrderApiResponse>
}