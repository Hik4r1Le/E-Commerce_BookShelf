package com.example.bookstore.api.order

import com.example.bookstore.api.order.OrderApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    private const val BASE_URL = "https://e-commerce-bookshelf-backend.onrender.com/api/v1/"

    val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val orderApi: OrderApi by lazy {
        retrofit.create(OrderApi::class.java)
    }
}