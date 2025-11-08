package com.example.bookstore.ui.cart

import androidx.lifecycle.ViewModel
import com.example.bookstore.data.CartItem
import com.example.bookstore.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class CartViewModel : ViewModel() {

    private val _cartItems = MutableStateFlow(
        listOf(
            CartItem(1, "Tâm Lý Học & Đời Sống", "Richard J. Gerrig & Philip G. Zimbardo", 95_000, R.drawable.book1, checked = true),
            CartItem(2, "Memoirs", "Kanika Sharma", 88_000, R.drawable.book2, quantity = 2, checked = true),
            CartItem(3, "Đắc Nhân Tâm", "Dale Carnegie", 120_000, R.drawable.book3, checked = true)
        )
    )
    val cartItems: StateFlow<List<CartItem>> = _cartItems

    fun getTotalItems() = _cartItems.value.sumOf { it.quantity }

    fun getTotalPrice() = _cartItems.value.sumOf { it.price * it.quantity }

    fun toggleChecked(id: Int, checked: Boolean) {
        _cartItems.value = _cartItems.value.map { if (it.id == id) it.copy(checked = checked) else it }
    }

    fun updateQuantity(id: Int, quantity: Int) {
        _cartItems.value = _cartItems.value.map { if (it.id == id) it.copy(quantity = quantity.coerceAtLeast(1)) else it }
    }

    fun removeItem(id: Int) {
        _cartItems.value = _cartItems.value.filter { it.id != id }
    }
}