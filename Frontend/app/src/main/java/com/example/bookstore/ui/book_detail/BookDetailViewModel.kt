package com.example.bookstore.ui.book_detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

import com.example.bookstore.repository.CartRepository
import com.example.bookstore.model.cart.AddToCartRequest
import com.example.bookstore.repository.ProductRepository
import com.example.bookstore.model.products.ProductDetailUI
import com.example.bookstore.model.products.toUIModel

class BookDetailViewModel(
    private val productRepository: ProductRepository,
    private val cartRepository: CartRepository
) : ViewModel() {
    var isLoading by mutableStateOf(false)
        private set
    var errorMessage by mutableStateOf<String?>(null)
        private set

    var productDetail by mutableStateOf<ProductDetailUI?>(null)
        private set

    var isAddingToCart by mutableStateOf(false)
        private set

    // ★ NEW: Review submission states
    var isSubmittingReview by mutableStateOf(false)
        private set

    var reviewSubmitSuccess by mutableStateOf(false)
        private set

    fun loadProductDetail(productId: String) {
        if (productDetail != null) return

        viewModelScope.launch {
            isLoading = true
            errorMessage = null

            val result = productRepository.getProductDetail(productId)

            result.onSuccess { response ->
                productDetail = response.toUIModel()
            }.onFailure { e ->
                errorMessage = e.message
                productDetail = null
            }

            isLoading = false
        }
    }

    fun addToCart(stockId: String, quantity: Int, priceAtAdd: Double, onSuccess: () -> Unit) {
        if (isAddingToCart) return

        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            isAddingToCart = true

            val request = AddToCartRequest(
                stockId = stockId,
                quantity = quantity,
                priceAtAdd = priceAtAdd
            )

            val result = cartRepository.addToCart(request)

            result.onSuccess {
                onSuccess()
            }.onFailure { e ->
                errorMessage = e.message
            }

            isAddingToCart = false
            isLoading = false
        }
    }

    // ★ NEW: Submit review function
    fun submitReview(productId: String, rating: Int, comment: String, onSuccess: () -> Unit = {}) {
        if (isSubmittingReview) return

        viewModelScope.launch {
            isSubmittingReview = true
            errorMessage = null
            reviewSubmitSuccess = false

            val result = productRepository.submitReview(productId, rating, comment)

            result.onSuccess {
                reviewSubmitSuccess = true
                // Reload product detail to show new review
                productDetail = null
                loadProductDetail(productId)
                onSuccess()
            }.onFailure { e ->
                errorMessage = e.message
                reviewSubmitSuccess = false
            }

            isSubmittingReview = false
        }
    }

    fun clearError() {
        errorMessage = null
    }

    fun resetReviewSuccess() {
        reviewSubmitSuccess = false
    }
}