package com.example.bookstore.ui.orders

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.bookstore.model.OrderDataSection
import com.example.bookstore.model.OrderStatus
import com.example.bookstore.api.order.OrderRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class OrdersViewModel(private val token: String) : ViewModel() {

    private val repository = OrderRepository()

    val tabs = listOf("Chờ xác nhận", "Chờ lấy hàng", "Chờ giao hàng", "Đã giao")

    private val _selectedTabIndex = MutableStateFlow(0)
    val selectedTabIndex: StateFlow<Int> = _selectedTabIndex

    private val _totalMessages = MutableStateFlow(8)
    val totalMessages: StateFlow<Int> = _totalMessages

    private val _orderSections = MutableStateFlow<List<OrderDataSection>>(emptyList())
    val orderSections: StateFlow<List<OrderDataSection>> = _orderSections

    init {
        loadOrdersFromApi()
    }

    private fun loadOrdersFromApi() {
        viewModelScope.launch {
            val orders = repository.fetchOrders(token)
            orders?.let {
                val sections = tabs.mapIndexed { index, _ ->
                    val status = when (index) {
                        0 -> OrderStatus.PENDING
                        1 -> OrderStatus.WAIT_PICKUP
                        2 -> OrderStatus.WAIT_DELIVERY
                        3 -> OrderStatus.DELIVERED
                        else -> OrderStatus.PENDING
                    }
                    val sectionOrders = it.filter { order -> order.status == status }
                    OrderDataSection(
                        status = status,
                        orders = sectionOrders,
                        totalPrice = sectionOrders.sumOf { order -> order.price * order.quantity },
                        actionText = sectionOrders.firstOrNull()?.actionText ?: ""
                    )
                }
                _orderSections.value = sections
            }
        }
    }

    fun onTabSelected(index: Int) { _selectedTabIndex.value = index }

    fun clearMessages() { _totalMessages.value = 0 }

    class Factory(private val token: String) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return OrdersViewModel(token) as T
        }
    }
}