package com.example.bookstore.api.order

import com.example.bookstore.R
import com.example.bookstore.model.OrderData
import com.example.bookstore.model.OrderStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class OrderRepository {

    private val orderApi = ApiClient.orderApi

    suspend fun fetchOrders(token: String): List<OrderData>? {
        return withContext(Dispatchers.IO) {
            try {
                val response = orderApi.getOrders("Bearer $token")
                response.map { apiOrder ->
                    OrderData(
                        id = apiOrder.id,
                        title = apiOrder.title,
                        author = apiOrder.author,
                        price = apiOrder.price,
                        quantity = apiOrder.quantity,
                        imageRes = R.drawable.book6,
                        status = when (apiOrder.statusLabel) {
                            "PENDING" -> OrderStatus.PENDING
                            "WAIT_PICKUP" -> OrderStatus.WAIT_PICKUP
                            "WAIT_DELIVERY" -> OrderStatus.WAIT_DELIVERY
                            "DELIVERED" -> OrderStatus.DELIVERED
                            else -> OrderStatus.PENDING
                        },
                        actionText = when (apiOrder.statusLabel) {
                            "PENDING" -> "Đang xử lý"
                            "WAIT_PICKUP" -> "Liên hệ Shop"
                            "WAIT_DELIVERY" -> "Đã nhận hàng"
                            "DELIVERED" -> "Mua lại"
                            else -> ""
                        }
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }
}