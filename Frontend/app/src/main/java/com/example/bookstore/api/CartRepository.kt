package com.example.bookstore.api

import com.example.bookstore.model.CartItemData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CartRepository(private val api: CartApi) {

    suspend fun getCartItems(): List<CartItemData> = withContext(Dispatchers.IO) {
        val response = api.getCartItems()
        if (response.isSuccessful) response.body() ?: emptyList()
        else emptyList()
    }

    suspend fun updateQuantity(id: Int, quantity: Int): CartItemData? = withContext(Dispatchers.IO) {
        val response = api.updateQuantity(id, quantity)
        if (response.isSuccessful) response.body() else null
    }

    suspend fun toggleChecked(id: Int, checked: Boolean): CartItemData? = withContext(Dispatchers.IO) {
        val response = api.toggleChecked(id, checked)
        if (response.isSuccessful) response.body() else null
    }

    suspend fun removeItem(id: Int): Boolean = withContext(Dispatchers.IO) {
        val response = api.removeItem(id)
        response.isSuccessful
    }
}