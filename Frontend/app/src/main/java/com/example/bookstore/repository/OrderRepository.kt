package com.example.bookstore.repository

import com.example.bookstore.model.order.CreateOrderRequest
import com.example.bookstore.model.order.OrderResponse
import com.example.bookstore.network.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import org.json.JSONObject

class OrderRepository(
    private val apiService: ApiService
) {
    // Hàm hỗ trợ trích xuất thông báo lỗi từ Response không thành công
    private fun getErrorMessage(response: Response<*>): String {
        return try {
            val errorBody = response.errorBody()?.string()
            if (!errorBody.isNullOrEmpty()) {
                val jsonObject = JSONObject(errorBody)
                jsonObject.optString("message", "Unknown error")
            } else {
                "Error: ${response.code()} ${response.message()}"
            }
        } catch (e: Exception) {
            "Unexpected error: ${response.code()}"
        }
    }

    // API Tạo order sau khi checkout
    suspend fun createOrder(request: CreateOrderRequest): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.createOrder(request)
                if (response.isSuccessful) {
                    Result.success(Unit)
                } else {
                    Result.failure(Exception(getErrorMessage(response)))
                }
            } catch (e: Exception) {
                Result.failure(Exception("Connection error: ${e.message}", e))
            }
        }
    }

    // API Xem các đơn hàng
    suspend fun getOrders(): Result<OrderResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getOrders()
                if (response.isSuccessful) {
                    response.body()?.let { Result.success(it) }
                        ?: Result.failure(Exception("Response body is empty"))
                } else {
                    Result.failure(Exception(getErrorMessage(response)))
                }
            } catch (e: Exception) {
                Result.failure(Exception("Connection error: ${e.message}", e))
            }
        }
    }
}