package com.example.bookstore.api.cart

import com.example.bookstore.model.CartItemData
import retrofit2.Response
import retrofit2.http.*

interface CartApi {

    // Lấy giỏ hàng
    @GET("cart")
    suspend fun getCartItems(): Response<List<CartItemData>>

    // Thêm sản phẩm vào giỏ
    @POST("cart/add")
    suspend fun addItem(
        @Query("productId") productId: Int,
        @Query("quantity") quantity: Int
    ): Response<CartItemData>

    // Cập nhật số lượng
    @POST("cart/update-quantity")
    suspend fun updateQuantity(
        @Query("id") id: Int,
        @Query("quantity") quantity: Int
    ): Response<CartItemData>

    // Chọn/bỏ chọn item
    @POST("cart/toggle-checked")
    suspend fun toggleChecked(
        @Query("id") id: Int,
        @Query("checked") checked: Boolean
    ): Response<CartItemData>

    // Xóa 1 item
    @DELETE("cart/remove/{id}")
    suspend fun removeItem(
        @Path("id") id: Int
    ): Response<Unit>

    // Xóa tất cả item đã check
    @POST("cart/clear")
    suspend fun clearChecked(): Response<Unit>
}