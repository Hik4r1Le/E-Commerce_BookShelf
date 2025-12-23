package com.example.bookstore.api

import com.example.bookstore.model.CartItemData
import retrofit2.Response
import retrofit2.http.*

interface CartApi {

    @GET("cart")
    suspend fun getCartItems(): Response<List<CartItemData>>

    @POST("cart/update-quantity")
    suspend fun updateQuantity(
        @Query("id") id: Int,
        @Query("quantity") quantity: Int
    ): Response<CartItemData>

    @POST("cart/toggle-checked")
    suspend fun toggleChecked(
        @Query("id") id: Int,
        @Query("checked") checked: Boolean
    ): Response<CartItemData>

    @DELETE("cart/remove/{id}")
    suspend fun removeItem(
        @Path("id") id: Int
    ): Response<Unit>
}