package com.example.bookstore.ui.book_detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

import com.example.bookstore.repository.ProductRepository
import com.example.bookstore.model.products.ProductDetailUI
import com.example.bookstore.model.products.toUIModel

class BookDetailViewModel(
    private val productRepository: ProductRepository
) : ViewModel() {
    var isLoading by mutableStateOf(false)
        private set
    var errorMessage by mutableStateOf<String?>(null)
        private set

    // Lưu trữ dữ liệu chi tiết sản phẩm
    var productDetail by mutableStateOf<ProductDetailUI?>(null)
        private set

    // Hàm gọi API lấy chi tiết sản phẩm
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

    fun clearError() {
        errorMessage = null
    }
}