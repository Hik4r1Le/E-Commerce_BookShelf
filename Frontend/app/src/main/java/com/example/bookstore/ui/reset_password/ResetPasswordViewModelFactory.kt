package com.example.bookstore.ui.reset_password

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.bookstore.network.RetrofitInstance
import com.example.bookstore.network.TokenManager
import com.example.bookstore.repository.AuthRepository

class ResetPasswordViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ResetPasswordViewModel::class.java)) {
            val appCtx = context.applicationContext
            val apiService = RetrofitInstance.getApiService(appCtx)
            val tokenManager = TokenManager(appCtx)
            val authRepository = AuthRepository(apiService, tokenManager)

            @Suppress("UNCHECKED_CAST")
            return ResetPasswordViewModel(authRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}