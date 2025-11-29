package com.example.bookstore.ui.checkout

import com.example.bookstore.model.CheckoutResponse

sealed class CheckoutUiState {
    object Loading : CheckoutUiState()
    data class Success(val response: CheckoutResponse) : CheckoutUiState()
    data class Error(val message: String) : CheckoutUiState()
}