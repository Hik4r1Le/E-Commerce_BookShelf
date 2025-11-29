package com.example.bookstore.ui.checkout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookstore.api.checkout.CheckoutRepository
import com.example.bookstore.model.CheckoutRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CheckoutViewModel : ViewModel() {

    private val repository = CheckoutRepository()

    private val _uiState = MutableStateFlow<CheckoutUiState>(CheckoutUiState.Loading)
    val uiState: StateFlow<CheckoutUiState> = _uiState

    fun checkoutFromCart(token: String, cartIds: List<String>) {
        viewModelScope.launch {
            _uiState.value = CheckoutUiState.Loading
            try {
                val request = CheckoutRequest(cartIds)
                val response = repository.checkout(token, request)
                if (response != null) {
                    _uiState.value = CheckoutUiState.Success(response)
                } else {
                    _uiState.value = CheckoutUiState.Error("Checkout thất bại")
                }
            } catch (e: Exception) {
                _uiState.value = CheckoutUiState.Error(e.message ?: "Lỗi không xác định")
            }
        }
    }
}