package com.example.bookstore.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

import com.example.bookstore.repository.ProductRepository
import com.example.bookstore.model.products.HomeUIProduct
import com.example.bookstore.model.products.toUIModel

class HomeViewModel(
    private val productRepository: ProductRepository
) : ViewModel() {
    // Trạng thái UI
    var isLoading by mutableStateOf(false)
        private set
    var errorMessage by mutableStateOf<String?>(null)
        private set

    // Danh sách sản phẩm đã được chuyển đổi để hiển thị trên UI
    var products by mutableStateOf<List<HomeUIProduct>>(emptyList())
        private set

    fun loadHomeProducts() {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null

            val result = productRepository.getHomeProducts()

            result.onSuccess { response ->
                // Ánh xạ API Response Model sang UI Model
                products = response.data.map { it.toUIModel() }
            }.onFailure { e ->
                errorMessage = e.message
                products = emptyList()
            }

            isLoading = false
        }
    }

    fun clearError() {
        errorMessage = null
    }
}