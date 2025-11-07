package com.example.bookstore.ui.orders

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookstore.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class OrdersViewModel : ViewModel() {

    val tabs = listOf("Chờ xác nhận", "Chờ lấy hàng", "Chờ giao hàng", "Đã giao")

    private val _selectedTabIndex = MutableStateFlow(0)
    val selectedTabIndex: StateFlow<Int> = _selectedTabIndex

    private val _totalMessages = MutableStateFlow(8)
    val totalMessages: StateFlow<Int> = _totalMessages

    private val _orderSections = MutableStateFlow<List<OrderSection>>(emptyList())
    val orderSections: StateFlow<List<OrderSection>> = _orderSections

    init {
        loadOrders()
    }

    private fun loadOrders() {
        viewModelScope.launch {
            val sampleOrders = listOf(
                BookOrder("Muôn kiếp nhân sinh", "Nguyễn Phong", 99, 1, R.drawable.book6),
                BookOrder("Cho tôi xin một vé đi tuổi thơ", "Nguyễn Nhật Ánh", 69, 2, R.drawable.book7)
            )

            val sections = listOf(
                OrderSection("Chờ xác nhận", sampleOrders, 257, "Đang xử lý"),
                OrderSection("Chờ lấy hàng", sampleOrders, 257, "Liên hệ Shop"),
                OrderSection("Chờ giao hàng", sampleOrders, 257, "Đã nhận hàng"),
                OrderSection("Đã giao", sampleOrders, 257, "Mua lại")
            )

            _orderSections.value = sections
        }
    }

    fun onTabSelected(index: Int) {
        _selectedTabIndex.value = index
    }

    fun clearMessages() {
        _totalMessages.value = 0
    }
}