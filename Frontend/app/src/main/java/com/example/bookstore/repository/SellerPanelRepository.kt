package com.example.bookstore.repository

import com.example.bookstore.model.sellerpanel.*
import com.example.bookstore.network.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import org.json.JSONObject

class SellerPanelRepository(private val apiService: ApiService) {
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

    // 1. Get Products
    suspend fun getSellerProducts(): Result<SellerPanelResponse> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.getSellerProducts()
            if (response.isSuccessful) {
                response.body()?.let { Result.success(it) } ?: Result.failure(Exception("Empty body"))
            } else Result.failure(Exception(getErrorMessage(response)))
        } catch (e: Exception) {
            Result.failure(Exception("Connection error: ${e.message}", e))
        }
    }

    // 2. Create Seller Product
    suspend fun createSellerProduct(
        image: MultipartBody.Part?,
        fields: Map<String, RequestBody>
    ): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.createSellerProduct(
                image,
                fields["name"]!!,
                fields["author_name"]!!,
                fields["description"]!!,
                fields["price"]!!,
                fields["quantity"]!!,
                fields["category_id"]!!
            )
            if (response.isSuccessful) Result.success(Unit)
            else Result.failure(Exception(getErrorMessage(response)))
        } catch (e: Exception) {
            Result.failure(Exception("Connection error: ${e.message}", e))
        }
    }

    // 3. Update Product
    suspend fun updateSellerProduct(
        productId: String,
        image: MultipartBody.Part?,
        fields: Map<String, RequestBody>
    ): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.updateSellerProduct(productId, image, fields)
            if (response.isSuccessful) Result.success(Unit)
            else Result.failure(Exception(getErrorMessage(response)))
        } catch (e: Exception) {
            Result.failure(Exception("Connection error: ${e.message}", e))
        }
    }

    // 4. Delete Product
    suspend fun deleteSellerProduct(productId: String): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.deleteSellerProduct(productId)
            if (response.isSuccessful) Result.success(Unit)
            else Result.failure(Exception(getErrorMessage(response)))
        } catch (e: Exception) {
            Result.failure(Exception("Connection error: ${e.message}", e))
        }
    }

    // 5. Get Orders
    suspend fun getSellerOrders(): Result<SellerOrderResponse> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.getSellerOrders()
            if (response.isSuccessful) {
                response.body()?.let { Result.success(it) } ?: Result.failure(Exception("Empty body"))
            } else Result.failure(Exception(getErrorMessage(response)))
        } catch (e: Exception) { Result.failure(e) }
    }

    // 6. Update Order Status
    suspend fun updateOrderStatus(orderId: String, status: String): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.updateSellerOrderStatus(orderId, UpdateOrderStatusRequest(status))
            if (response.isSuccessful) Result.success(Unit)
            else Result.failure(Exception(getErrorMessage(response)))
        } catch (e: Exception) {
            Result.failure(Exception("Connection error: ${e.message}", e))
        }
    }
}