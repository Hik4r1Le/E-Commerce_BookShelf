package com.example.bookstore.ui.cart

import android.app.Application
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import com.example.bookstore.R

private val Application.dataStore by preferencesDataStore("cart_prefs")

class CartViewModel(app: Application) : AndroidViewModel(app) {

    data class CartItem(
        val id: Int,
        val name: String,
        val author: String,
        val price: Int,
        val imageRes: Int,
        val quantity: Int = 1,
        val checked: Boolean = true
    ) {
        companion object {
            fun sample() = listOf(
                CartItem(1, "Tâm Lý Học & Đời Sống", "Richard J. Gerrig & Philip G. Zimbardo", 95_000, R.drawable.book1, 1),
                CartItem(2, "Memoirs", "Kanika Sharma", 88_000, R.drawable.book2, 2),
                CartItem(3, "Đắc Nhân Tâm", "Dale Carnegie", 120_000, R.drawable.book3, 1)
            )
        }
    }

    private val gson = Gson()
    private val CART_KEY = stringPreferencesKey("cart_items")

    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems: StateFlow<List<CartItem>> = _cartItems

    init {
        viewModelScope.launch {
            app.dataStore.data
                .map { prefs ->
                    prefs[CART_KEY]?.let { json ->
                        val type = object : TypeToken<List<CartItem>>() {}.type
                        gson.fromJson<List<CartItem>>(json, type)
                    } ?: CartItem.sample()
                }
                .collect { _cartItems.value = it }
        }
    }

    private fun saveCart() {
        viewModelScope.launch {
            getApplication<Application>().dataStore.edit { prefs ->
                prefs[CART_KEY] = gson.toJson(_cartItems.value)
            }
        }
    }

    fun toggleChecked(id: Int, checked: Boolean) {
        _cartItems.value = _cartItems.value.map { if (it.id == id) it.copy(checked = checked) else it }
        saveCart()
    }

    fun updateQuantity(id: Int, quantity: Int) {
        _cartItems.value = _cartItems.value.map { if (it.id == id) it.copy(quantity = quantity) else it }
        saveCart()
    }

    fun removeItem(id: Int) {
        _cartItems.value = _cartItems.value.filter { it.id != id }
        saveCart()
    }

    fun getTotalPrice(): Int = _cartItems.value.filter { it.checked }.sumOf { it.price * it.quantity }

    fun getTotalItems(): Int = _cartItems.value.filter { it.checked }.sumOf { it.quantity }
}