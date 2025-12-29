package com.example.bookstore.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
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

    var searchQuery by mutableStateOf("") // Lưu chuỗi người dùng nhập
    var searchResults by mutableStateOf<List<HomeUIProduct>>(emptyList()) // Kết quả search
    var isSearching by mutableStateOf(false) // Trạng thái đang tìm kiếm

    private val _searchFlow = MutableStateFlow("")

    init {
        setupSearchDebounce()
    }

    @OptIn(FlowPreview::class)
    private fun setupSearchDebounce() {
        viewModelScope.launch {
            _searchFlow
                .debounce(1000L) // Đợi 1000ms sau khi người dùng ngừng gõ
                .filter { query ->
                    // Chỉ gọi API nếu query không rỗng (tránh gọi dư thừa khi reset)
                    query.isNotBlank()
                }
                .distinctUntilChanged() // Chỉ search nếu từ khóa thực sự thay đổi
                .collectLatest { query ->
                    // collectLatest sẽ hủy bỏ request cũ nếu có request mới đến
                    performSearch(query)
                }
        }
    }

    fun onQueryChange(newQuery: String) {
        searchQuery = newQuery // Cập nhật ngay lập tức lên giao diện (Compose)
        _searchFlow.value = newQuery // Đẩy vào Flow để xử lý Debounce

        // Nếu xóa trắng ô search thì xóa kết quả ngay lập tức, không cần đợi debounce
        if (newQuery.isBlank()) {
            searchResults = emptyList()
        }
    }

    private suspend fun performSearch(query: String) {
        viewModelScope.launch {
            isSearching = true
            errorMessage = null
            // Định dạng filter theo yêu cầu API: filter={"name":"query"}
            val filterJson = "{\"name\":\"$query\"}"

            val result = productRepository.getHomeProducts(filterJson)
            result.onSuccess { response ->
                searchResults = response.data.map { it.toUIModel() }
            }.onFailure { e ->
                errorMessage = e.message
                searchResults = emptyList()
            }
            isSearching = false
        }
    }

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