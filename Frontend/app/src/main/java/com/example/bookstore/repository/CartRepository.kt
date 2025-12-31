package com.example.bookstore.repository

import com.example.bookstore.model.cart.AddToCartRequest
import com.example.bookstore.model.cart.CartDetailResponse
import com.example.bookstore.model.cart.UpdateCartItemRequest
import com.example.bookstore.network.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import org.json.JSONObject

class CartRepository(
    private val apiService: ApiService
) {
    // Hàm hỗ trợ trích xuất thông báo lỗi từ Response không thành công
    private fun getErrorMessage(response: Response<*>): String {
        return try {
            val errorBody = response.errorBody()?.string()
            if (!errorBody.isNullOrEmpty()) {
                val jsonObject = JSONObject(errorBody)
                // Giả định server trả về lỗi trong trường "message"
                jsonObject.optString("message", "Unknown error")
            } else {
                "Error: ${response.code()} ${response.message()}"
            }
        } catch (e: Exception) {
            "Unexpected error: ${response.code()}"
        }
    }

    // API Xem chi tiết các sản phẩm trong giỏ hàng
    suspend fun getCartDetails(): Result<CartDetailResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getCartDetails()
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

    // API Thêm một sản phẩm vào giỏ hàng
    suspend fun addToCart(item: AddToCartRequest): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.addToCart(item)
                if (response.isSuccessful) {
                    // Trả về Result.success(Unit) vì Response<Unit> không có body
                    Result.success(Unit)
                } else {
                    Result.failure(Exception(getErrorMessage(response)))
                }
            } catch (e: Exception) {
                Result.failure(Exception("Connection error: ${e.message}", e))
            }
        }
    }

    // API Cập nhập số lượng sản phẩm của 1 cart item
    suspend fun updateCartItem(cartItemId: String, updateInfo: UpdateCartItemRequest): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.updateCartItem(cartItemId, updateInfo)
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

    // API Xóa một cart item
    suspend fun deleteCartItem(cartItemId: String): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.deleteCartItem(cartItemId)
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

    // API Xóa tất cả cart item
    suspend fun clearCart(): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.clearCart()
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
}