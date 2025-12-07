package com.example.bookstore.repository

import com.example.bookstore.model.checkout.CheckoutReviewRequest
import com.example.bookstore.model.checkout.CheckoutReviewResponse
import com.example.bookstore.network.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import org.json.JSONObject

class CheckoutRepository(
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

    // API khi vào checkout (Xem lại thông tin đơn hàng trước khi tạo)
    suspend fun checkoutReview(request: CheckoutReviewRequest): Result<CheckoutReviewResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.checkoutReview(request)
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