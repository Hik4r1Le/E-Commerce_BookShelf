package com.example.bookstore.repository

import com.example.bookstore.model.products.HomeProductResponse
import com.example.bookstore.model.products.ProductDetailResponse

import com.example.bookstore.network.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import org.json.JSONObject

class ProductRepository(
    private val apiService: ApiService
) {
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

    suspend fun getHomeProducts(filter: String? = null): Result<HomeProductResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getHomeProducts(filter)
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

    suspend fun getProductDetail(productId: String): Result<ProductDetailResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getProductDetail(productId)
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