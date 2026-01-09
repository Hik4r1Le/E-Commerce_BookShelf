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
import com.example.bookstore.model.products.ProductCommentUI
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

    var isSubmittingReview by mutableStateOf(false)
        private set

    var reviewSubmitSuccess by mutableStateOf(false)
        private set

    var isLoadingMoreReviews by mutableStateOf(false)
        private set

    var hasMoreReviews by mutableStateOf(false)
        private set

    var allReviews by mutableStateOf<List<ProductCommentUI>>(emptyList())
        private set

    fun loadProductDetail(productId: String) {
        if (productDetail != null) return

        viewModelScope.launch {
            isLoading = true
            errorMessage = null

            val result = productRepository.getProductDetail(productId)

            result.onSuccess { response ->
                // ★ toUIModel() returns ProductDetailUI? (nullable), so we need to handle null
                val product = response.toUIModel()
                if (product != null) {
                    productDetail = product
                    // ★ Initialize reviews list - product is non-null here
                    allReviews = product.comments
                    // ★ Check if there are more reviews to load (initially shows 3)
                    hasMoreReviews = product.comments.size > 3
                } else {
                    errorMessage = "Không thể tải thông tin sản phẩm"
                    productDetail = null
                    allReviews = emptyList()
                    hasMoreReviews = false
                }
            }.onFailure { e ->
                errorMessage = e.message
                productDetail = null
                allReviews = emptyList()
                hasMoreReviews = false
            }

            isLoading = false
        }
    }

    // Load all reviews from API
    fun loadAllReviews(productId: String) {
        if (isLoadingMoreReviews) return

        viewModelScope.launch {
            isLoadingMoreReviews = true
            errorMessage = null

            val result = productRepository.getAllReviews(productId)

            result.onSuccess { reviews ->
                allReviews = reviews
                hasMoreReviews = false // No more to load
            }.onFailure { e ->
                errorMessage = e.message
            }

            isLoadingMoreReviews = false
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

    // Submit review function
    fun submitReview(productId: String, rating: Int, comment: String, onSuccess: () -> Unit = {}) {
        if (isSubmittingReview) return

        viewModelScope.launch {
            isSubmittingReview = true
            errorMessage = null
            reviewSubmitSuccess = false

            val result = productRepository.submitReview(productId, rating, comment)

            result.onSuccess {
                reviewSubmitSuccess = true
                //Reload product detail to get updated rating_avg
                productDetail = null
                loadProductDetail(productId)
                // reload all reviews to show the new one
                loadAllReviews(productId)
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