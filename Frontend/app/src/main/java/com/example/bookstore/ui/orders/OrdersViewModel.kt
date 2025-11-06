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
            val sections = listOf(
                OrderSection(
                    "Chờ xác nhận",
                    listOf(
                        BookOrder("Muôn kiếp nhân sinh", "Nguyễn Phong", 99, 1, R.drawable.book6, "Chờ xác nhận"),
                        BookOrder("Cho tôi xin một vé đi tuổi thơ", "Nguyễn Nhật Ánh", 69, 2, R.drawable.book7, "Chờ xác nhận")
                    ),
                    99*1 + 69*2, "Đang xử lý"
                ),
                OrderSection(
                    "Chờ lấy hàng",
                    listOf(
                        BookOrder("Đắc Nhân Tâm", "Dale Carnegie", 89, 1, R.drawable.book6, "Chờ lấy hàng"),
                        BookOrder("Sapiens", "Yuval Harari", 120, 1, R.drawable.book7, "Chờ lấy hàng")
                    ),
                    89*1 + 120*1, "Liên hệ Shop"
                ),
                OrderSection(
                    "Chờ giao hàng",
                    listOf(
                        BookOrder("Nhà giả kim", "Paulo Coelho", 75, 1, R.drawable.book6, "Chờ giao hàng"),
                        BookOrder("Tuổi thơ dữ dội", "Phùng Quán", 65, 2, R.drawable.book7, "Chờ giao hàng")
                    ),
                    75*1 + 65*2, "Đã nhận hàng"
                ),
                OrderSection(
                    "Đã giao",
                    listOf(
                        BookOrder("Đi tìm thời gian đã mất", "Marcel Proust", 110, 1, R.drawable.book6, "Đã giao"),
                        BookOrder("Bí mật tư duy triệu phú", "T. Harv Eker", 95, 1, R.drawable.book7, "Đã giao")
                    ),
                    110*1 + 95*1, "Mua lại"
                )
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