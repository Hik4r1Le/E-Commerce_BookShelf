package com.example.bookstore.data.remote
import com.example.bookstore.model.HomeProductItem
import com.example.bookstore.model.SearchResult
import retrofit2.http.GET
import retrofit2.http.Query

data class ApiResponse<T>(
    val success: Boolean,
    val data: T
)

interface BookApi {
    @GET("api/v1/products/home")
    suspend fun searchBooks(
        @Query("filter") filter: String
    ): ApiResponse<List<HomeProductItem>>
}

