package com.example.bookstore.api.checkout

import com.example.bookstore.model.CheckoutRequest
import com.example.bookstore.model.CheckoutResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CheckoutRepository {

    private val api: ApiService

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://e-commerce-bookshelf-backend.onrender.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        api = retrofit.create(ApiService::class.java)
    }

    suspend fun checkout(token: String, request: CheckoutRequest): CheckoutResponse? {
        val response = api.checkout("Bearer $token", request)
        return if (response.isSuccessful) response.body() else null
    }
}