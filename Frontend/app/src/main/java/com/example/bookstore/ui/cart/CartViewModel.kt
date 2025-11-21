package com.example.bookstore.ui.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookstore.api.cart.CartRepository
import com.example.bookstore.model.CartItemData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CartViewModel(private val repository: CartRepository) : ViewModel() {

    data class CartItem(
        val id: Int,
        val name: String,
        val author: String,
        val price: Int,
        val imageRes: Int,
        val quantity: Int = 1,
        val checked: Boolean = true
    )

    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems: StateFlow<List<CartItem>> = _cartItems

    init {
        loadCart()
    }

    private fun loadCart() {
        viewModelScope.launch {
            val items = repository.getCartItems().map { data ->
                val priceInt = when (val p = data.price) {
                    is Double -> p.toInt()
                    is Float -> p.toInt()
                    is Long -> p.toInt()
                    is Int -> p
                    else -> try {
                        p.toString().toDouble().toInt()
                    } catch (e: Exception) { 0 }
                }

                val qtyInt = when (val q = data.quantity) {
                    is Double -> q.toInt()
                    is Float -> q.toInt()
                    is Long -> q.toInt()
                    is Int -> q
                    else -> try {
                        q.toString().toDouble().toInt()
                    } catch (e: Exception) { 1 }
                }

                CartItem(
                    id = data.id,
                    name = data.title,
                    author = data.author,
                    price = priceInt,
                    imageRes = data.resId,
                    quantity = qtyInt,
                    checked = data.isChecked
                )
            }
            _cartItems.value = items
        }
    }

    fun toggleChecked(id: Int, checked: Boolean) {
        viewModelScope.launch {
            repository.toggleChecked(id, checked)
            _cartItems.value = _cartItems.value.map {
                if (it.id == id) it.copy(checked = checked) else it
            }
        }
    }

    fun updateQuantity(id: Int, quantity: Int) {
        viewModelScope.launch {
            repository.updateQuantity(id, quantity)
            _cartItems.value = _cartItems.value.map {
                if (it.id == id) it.copy(quantity = quantity) else it
            }
        }
    }

    fun removeItem(id: Int) {
        viewModelScope.launch {
            val ok = repository.removeItem(id)
            if (ok) {
                _cartItems.value = _cartItems.value.filter { it.id != id }
            }
        }
    }

    fun clearChecked() {
        viewModelScope.launch {
            val ok = repository.clearChecked()
            if (ok) {
                _cartItems.value = _cartItems.value.filter { !it.checked }
            }
        }
    }

    fun getTotalPrice(): Int =
        _cartItems.value.filter { it.checked }.sumOf { it.price * it.quantity }

    fun getTotalItems(): Int =
        _cartItems.value.filter { it.checked }.sumOf { it.quantity }
}