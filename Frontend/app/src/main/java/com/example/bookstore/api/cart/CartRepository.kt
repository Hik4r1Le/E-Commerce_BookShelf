package com.example.bookstore.api.cart

import com.example.bookstore.model.CartItemData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CartRepository(private val api: CartApi) {

    // Lấy danh sách giỏ hàng
    suspend fun getCartItems(): List<CartItemData> = withContext(Dispatchers.IO) {
        val response = api.getCartItems()
        if (response.isSuccessful) response.body() ?: emptyList()
        else emptyList()
    }

    // Thêm vào giỏ
    suspend fun addItem(productId: Int, quantity: Int): CartItemData? = withContext(Dispatchers.IO) {
        val response = api.addItem(productId, quantity)
        if (response.isSuccessful) response.body() else null
    }

    // Cập nhật số lượng
    suspend fun updateQuantity(id: Int, quantity: Int): CartItemData? = withContext(Dispatchers.IO) {
        val response = api.updateQuantity(id, quantity)
        if (response.isSuccessful) response.body() else null
    }

    // Toggle checked
    suspend fun toggleChecked(id: Int, checked: Boolean): CartItemData? = withContext(Dispatchers.IO) {
        val response = api.toggleChecked(id, checked)
        if (response.isSuccessful) response.body() else null
    }

    // Xóa 1 item
    suspend fun removeItem(id: Int): Boolean = withContext(Dispatchers.IO) {
        val response = api.removeItem(id)
        response.isSuccessful
    }

    // Xóa những item đã tích
    suspend fun clearChecked(): Boolean = withContext(Dispatchers.IO) {
        val response = api.clearChecked()
        response.isSuccessful
    }
}