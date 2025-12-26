package com.example.bookstore.ui.notification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookstore.R
import com.example.bookstore.model.NotificationItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class NotificationViewModel : ViewModel() {

    // State chính cho UI
    private val _notifications =
        MutableStateFlow<List<NotificationItem>>(emptyList())

    val notifications: StateFlow<List<NotificationItem>>
        get() = _notifications

    init {
        loadNotifications()
    }

    // Load dữ liệu
    private fun loadNotifications() {
        viewModelScope.launch {
            _notifications.value = listOf(
                NotificationItem(
                    id = 1,
                    title = "Giao hàng thành công",
                    message = "Đơn hàng SPXVN0123 đã giao thành công đến bạn vào ngày 20-12-2025.",
                    time = "11:30 20-12-2025",
                    imageRes = R.drawable.book6
                ),
                NotificationItem(
                    id = 2,
                    title = "Đã gửi cho đơn vị vận chuyển",
                    message = "Đơn hàng SPXVN0123 đã được gửi cho đơn vị vận chuyển. Ngày giao hàng dự kiến là 20-12-2025.",
                    time = "11:00 19-12-2025",
                    imageRes = R.drawable.book6
                ),
                NotificationItem(
                    id = 3,
                    title = "Đặt hàng thành công",
                    message = "Đơn hàng SPXVN0123 đã được đặt thành công. Ngày giao hàng dự kiến là 20-12-2025.",
                    time = "10:00 18-12-2025",
                    imageRes = R.drawable.book6
                )
            )
        }
    }

    // Thêm thông báo mới
    fun addNotification(item: NotificationItem) {
        _notifications.value = listOf(item) + _notifications.value
    }

    // Xóa toàn bộ thông báo
    fun clearNotifications() {
        _notifications.value = emptyList()
    }
}