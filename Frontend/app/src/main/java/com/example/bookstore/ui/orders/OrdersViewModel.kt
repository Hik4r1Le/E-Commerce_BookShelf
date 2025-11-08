package com.example.bookstore.ui.orders

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookstore.model.OrderData
import com.example.bookstore.model.OrderDataSection
import com.example.bookstore.model.OrderStatus
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

    private val _orderSections = MutableStateFlow<List<OrderDataSection>>(emptyList())
    val orderSections: StateFlow<List<OrderDataSection>> = _orderSections

    init { loadOrders() }

    private fun loadOrders() {
        viewModelScope.launch {
            val sections = listOf(
                OrderDataSection(
                    OrderStatus.PENDING,
                    listOf(
                        OrderData(1, "Muôn kiếp nhân sinh", "Nguyễn Phong", 99, 1, R.drawable.book6, OrderStatus.PENDING, "Đang xử lý"),
                        OrderData(2, "Cho tôi xin một vé đi tuổi thơ", "Nguyễn Nhật Ánh", 69, 2, R.drawable.book7, OrderStatus.PENDING, "Đang xử lý")
                    ),
                    totalPrice = 99*1 + 69*2,
                    actionText = "Đang xử lý"
                ),
                OrderDataSection(
                    OrderStatus.WAIT_PICKUP,
                    listOf(
                        OrderData(3, "Đắc Nhân Tâm", "Dale Carnegie", 89, 1, R.drawable.book6, OrderStatus.WAIT_PICKUP, "Liên hệ Shop"),
                        OrderData(4, "Sapiens", "Yuval Harari", 120, 1, R.drawable.book7, OrderStatus.WAIT_PICKUP, "Liên hệ Shop")
                    ),
                    totalPrice = 89+120,
                    actionText = "Liên hệ Shop"
                ),
                OrderDataSection(
                    OrderStatus.WAIT_DELIVERY,
                    listOf(
                        OrderData(5, "Nhà giả kim", "Paulo Coelho", 75, 1, R.drawable.book6, OrderStatus.WAIT_DELIVERY, "Đã nhận hàng"),
                        OrderData(6, "Tuổi thơ dữ dội", "Phùng Quán", 65, 2, R.drawable.book7, OrderStatus.WAIT_DELIVERY, "Đã nhận hàng")
                    ),
                    totalPrice = 75 + 65*2,
                    actionText = "Đã nhận hàng"
                ),
                OrderDataSection(
                    OrderStatus.DELIVERED,
                    listOf(
                        OrderData(7, "Đi tìm thời gian đã mất", "Marcel Proust", 110, 1, R.drawable.book6, OrderStatus.DELIVERED, "Mua lại"),
                        OrderData(8, "Bí mật tư duy triệu phú", "T. Harv Eker", 95, 1, R.drawable.book7, OrderStatus.DELIVERED, "Mua lại")
                    ),
                    totalPrice = 110 + 95,
                    actionText = "Mua lại"
                )
            )
            _orderSections.value = sections
        }
    }

    fun onTabSelected(index: Int) { _selectedTabIndex.value = index }
    fun clearMessages() { _totalMessages.value = 0 }
}