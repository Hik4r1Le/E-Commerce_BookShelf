package com.example.bookstore.ui.order

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf

import com.example.bookstore.model.order.OrderUIModel
import com.example.bookstore.model.order.toUIModel
import com.example.bookstore.repository.OrderRepository

enum class OrderStatus(val label: String) {
    PENDING("Chờ xác nhận"),
    WAIT_PICKUP("Chờ lấy hàng"),
    WAIT_DELIVERY("Chờ giao hàng"),
    DELIVERED("Đã giao"),
    CANCELED("Đã hủy") // Thêm trạng thái hủy nếu cần
}

data class OrderDataSection(
    val status: OrderStatus,
    val orders: List<OrderUIModel>, // Sử dụng OrderUIModel
    val totalPrice: Double, // Tổng giá của Section này
    val actionText: String
)

// --- 2. TRẠNG THÁI UI VÀ VIEWMODEL ---

data class OrdersUiState(
    val orderSections: List<OrderDataSection> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val selectedTabIndex: Int = 0,
    val totalMessages: Int = 0 // Giữ lại biến này nếu dùng cho UI
)

class OrderViewModel(private val repository: OrderRepository) : ViewModel() {

    private val _uiState = mutableStateOf(OrdersUiState())
    val uiState: State<OrdersUiState> = _uiState

    private fun updateUiState(newState: OrdersUiState) {
        _uiState.value = newState
    }

    // API: Xem các đơn hàng
    fun loadOrders() {
        viewModelScope.launch {
            updateUiState(_uiState.value.copy(isLoading = true, errorMessage = null))

            val result = repository.getOrders()

            result.onSuccess { response ->
                // Ánh xạ API Model sang UI Model
                val allOrdersUI = response.data.map { it.toUIModel() }

                // Phân nhóm đơn hàng
                val sections = groupOrdersByStatus(allOrdersUI)

                updateUiState(_uiState.value.copy(
                    orderSections = sections,
                    isLoading = false
                ))
            }.onFailure { e ->
                updateUiState(_uiState.value.copy(
                    errorMessage = e.message,
                    isLoading = false,
                    orderSections = emptyList()
                ))
            }
        }
    }

    private fun groupOrdersByStatus(orders: List<OrderUIModel>): List<OrderDataSection> {
        val groups = orders.groupBy { order ->
            // Chuyển đổi trạng thái chuỗi từ API sang Enum
            OrderStatus.values().find { it.name.equals(order.status, ignoreCase = true) }
                ?: OrderStatus.PENDING
        }

        val allStatusTabs = listOf(
            OrderStatus.PENDING,
            OrderStatus.WAIT_PICKUP,
            OrderStatus.WAIT_DELIVERY,
            OrderStatus.DELIVERED
            // Thêm OrderStatus.CANCELED nếu cần hiển thị tab Hủy
        )

        return allStatusTabs.map { status ->
            val sectionOrders = groups[status] ?: emptyList()

            OrderDataSection(
                status = status,
                orders = sectionOrders,
                totalPrice = sectionOrders.sumOf { it.totalPrice }, // Sum of total_price from API
                actionText = getActionTextForStatus(status)
            )
        }
    }

    private fun getActionTextForStatus(status: OrderStatus): String {
        return when (status) {
            OrderStatus.PENDING -> "Hủy đơn" // Hoặc "Đang xử lý" nếu không cho hủy
            OrderStatus.WAIT_PICKUP -> "Liên hệ Shop"
            OrderStatus.WAIT_DELIVERY -> "Đã nhận hàng"
            OrderStatus.DELIVERED -> "Mua lại"
            OrderStatus.CANCELED -> ""
        }
    }

    // Chức năng: Thay đổi tab
    fun onTabSelected(index: Int) {
        updateUiState(_uiState.value.copy(selectedTabIndex = index))
    }

    // Chức năng: Giả định Clear Messages (nếu có UI)
    fun clearMessages() {
        updateUiState(_uiState.value.copy(totalMessages = 0))
    }

    // Thuộc tính hỗ trợ UI
    val tabs = listOf(
        OrderStatus.PENDING.label,
        OrderStatus.WAIT_PICKUP.label,
        OrderStatus.WAIT_DELIVERY.label,
        OrderStatus.DELIVERED.label
    )
}