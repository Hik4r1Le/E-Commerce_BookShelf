package com.example.bookstore.ui.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookstore.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class CartItem(
    val id: Int,
    val name: String,
    val author: String,
    val price: Int,
    val imageRes: Int,
    val quantity: Int = 1,
    val checked: Boolean = true
)

class CartViewModel : ViewModel() {

    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems: StateFlow<List<CartItem>> = _cartItems

    init {
        loadSampleData()
    }

    private fun loadSampleData() {
        viewModelScope.launch {
            _cartItems.value = listOf(
                CartItem(1, "Tâm Lý Học & Đời Sống", "Richard J. Gerrig & Philip G. Zimbardo", 95_000, R.drawable.book1, quantity = 1),
                CartItem(2, "Memoirs", "Kanika Sharma", 88_000, R.drawable.book2, quantity = 2),
                CartItem(3, "Đắc Nhân Tâm", "Dale Carnegie", 120_000, R.drawable.book3, quantity = 1),
                CartItem(4, "Kính Vạn Hoa", "Nguyễn Nhật Ánh", 86_000, R.drawable.book4, quantity = 3)
            )
        }
    }

    fun toggleChecked(itemId: Int, checked: Boolean) {
        _cartItems.update { list ->
            list.map { if (it.id == itemId) it.copy(checked = checked) else it }
        }
    }

    fun updateQuantity(itemId: Int, quantity: Int) {
        if (quantity < 1) return
        _cartItems.update { list ->
            list.map { if (it.id == itemId) it.copy(quantity = quantity) else it }
        }
    }

    fun removeItem(itemId: Int) {
        _cartItems.update { list -> list.filterNot { it.id == itemId } }
    }

    fun getTotalPrice(): Int =
        _cartItems.value.filter { it.checked }.sumOf { it.price * it.quantity }

    fun getTotalItems(): Int =
        _cartItems.value.filter { it.checked }.sumOf { it.quantity }
}