package com.example.bookstore.ui.orders

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookstore.R
import com.example.bookstore.data.Order
import com.example.bookstore.data.OrderSection
import com.example.bookstore.data.OrderStatus
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
                    OrderStatus.PENDING,
                    listOf(
                        Order(1, "Muôn kiếp nhân sinh", "Nguyễn Phong", 99, 1, R.drawable.book6, OrderStatus.PENDING, "Đang xử lý"),
                        Order(2, "Cho tôi xin một vé đi tuổi thơ", "Nguyễn Nhật Ánh", 69, 2, R.drawable.book7, OrderStatus.PENDING, "Đang xử lý")
                    ),
                    99*1 + 69*2,
                    "Đang xử lý"
                ),
                OrderSection(
                    OrderStatus.WAIT_PICKUP,
                    listOf(
                        Order(3, "Đắc Nhân Tâm", "Dale Carnegie", 89, 1, R.drawable.book6, OrderStatus.WAIT_PICKUP, "Liên hệ Shop"),
                        Order(4, "Sapiens", "Yuval Harari", 120, 1, R.drawable.book7, OrderStatus.WAIT_PICKUP, "Liên hệ Shop")
                    ),
                    89*1 + 120*1,
                    "Liên hệ Shop"
                ),
                OrderSection(
                    OrderStatus.WAIT_DELIVERY,
                    listOf(
                        Order(5, "Nhà giả kim", "Paulo Coelho", 75, 1, R.drawable.book6, OrderStatus.WAIT_DELIVERY, "Đã nhận hàng"),
                        Order(6, "Tuổi thơ dữ dội", "Phùng Quán", 65, 2, R.drawable.book7, OrderStatus.WAIT_DELIVERY, "Đã nhận hàng")
                    ),
                    75*1 + 65*2,
                    "Đã nhận hàng"
                ),
                OrderSection(
                    OrderStatus.DELIVERED,
                    listOf(
                        Order(7, "Đi tìm thời gian đã mất", "Marcel Proust", 110, 1, R.drawable.book6, OrderStatus.DELIVERED, "Mua lại"),
                        Order(8, "Bí mật tư duy triệu phú", "T. Harv Eker", 95, 1, R.drawable.book7, OrderStatus.DELIVERED, "Mua lại")
                    ),
                    110*1 + 95*1,
                    "Mua lại"
                )
            )
            _orderSections.value = sections
        }
    }

    fun onTabSelected(index: Int) { _selectedTabIndex.value = index }

    fun clearMessages() { _totalMessages.value = 0 }
}