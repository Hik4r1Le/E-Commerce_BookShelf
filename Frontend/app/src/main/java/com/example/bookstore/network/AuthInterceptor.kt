package com.example.bookstore.network

import com.example.bookstore.network.TokenManager
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private val tokenManager: TokenManager) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        // 1. Lấy token một cách đồng bộ (blocking)
        // Vì Interceptor chạy trên luồng của OkHttp, ta cần dùng runBlocking
        // để lấy giá trị Flow<String?> từ DataStore.
        val token = runBlocking {
            tokenManager.getAuthToken().firstOrNull()
        }

        // 2. Xây dựng request mới
        val originalRequest = chain.request()
        val builder = originalRequest.newBuilder()

        // 3. Thêm token vào Header nếu token tồn tại
        if (token != null) {
            // Chuẩn Bearer Token: Authorization: Bearer [token_value]
            builder.header("Authorization", "Bearer $token")
        }

        // 4. Thực hiện request
        return chain.proceed(builder.build())
    }
}