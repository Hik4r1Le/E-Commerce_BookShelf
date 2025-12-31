package com.example.bookstore.ui.register

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.bookstore.network.TokenManager
import com.example.bookstore.network.RetrofitInstance
import com.example.bookstore.repository.AuthRepository

class RegisterViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
            // Khởi tạo các dependency
            val appCtx = context.applicationContext
            val apiService = RetrofitInstance.getApiService(appCtx)
            val tokenManager = TokenManager(appCtx)
            val authRepository = AuthRepository(apiService, tokenManager)

            @Suppress("UNCHECKED_CAST")
            return RegisterViewModel(authRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}