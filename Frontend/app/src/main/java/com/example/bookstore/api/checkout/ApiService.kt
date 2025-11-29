package com.example.bookstore.api.checkout

import com.example.bookstore.model.CheckoutRequest
import com.example.bookstore.model.CheckoutResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiService {

    @POST("api/v1/checkout/review")
    suspend fun checkout(
        @Header("Authorization") token: String,
        @Body request: CheckoutRequest
    ): Response<CheckoutResponse>
}