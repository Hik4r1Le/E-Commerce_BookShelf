package com.example.bookstore.ui.otp

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.bookstore.network.RetrofitInstance
import com.example.bookstore.network.TokenManager
import com.example.bookstore.repository.AuthRepository

class OtpViewModelFactory(
    private val context: Context,
    private val initialEmail: String,
    private val initialType: String
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(OtpViewModel::class.java)) {
            val appCtx = context.applicationContext
            val apiService = RetrofitInstance.getApiService(appCtx)
            val tokenManager = TokenManager(appCtx)
            val authRepository = AuthRepository(apiService, tokenManager)

            @Suppress("UNCHECKED_CAST")
            return OtpViewModel(
                authRepository = authRepository,
                initialEmail = initialEmail,
                initialType = initialType
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}