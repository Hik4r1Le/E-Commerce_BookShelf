package com.example.bookstore.network

import retrofit2.Retrofit
import okhttp3.OkHttpClient
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import com.google.gson.GsonBuilder
import com.google.gson.Gson
import java.util.*
import android.content.Context

object RetrofitInstance {
    private const val BASE_URL = "https://e-commerce-bookshelf-backend.onrender.com";

    private val customGson: Gson = GsonBuilder()
        .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        .registerTypeAdapter(Date::class.java, com.google.gson.internal.bind.DateTypeAdapter())
        .create()

    // Hàm khởi tạo OkHttpClient và thêm AuthInterceptor
    private fun createOkHttpClient(context: Context): OkHttpClient {
        // Khởi tạo TokenManager AuthInterceptor
        val tokenManager = TokenManager(context)
        val authInterceptor = AuthInterceptor(tokenManager)

        return OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(authInterceptor)
            .build()
    }

    // Hàm khởi tạo Retrofit
    private fun createRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(customGson))
            .build()
    }

    // Hàm công khai để lấy ApiService
    fun getApiService(context: Context): ApiService {
        val okHttpClient = createOkHttpClient(context.applicationContext)
        val retrofit = createRetrofit(okHttpClient)
        return retrofit.create(ApiService::class.java)
    }
}