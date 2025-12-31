package com.example.bookstore.ui.userprofile

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.bookstore.network.TokenManager
import com.example.bookstore.network.RetrofitInstance
import com.example.bookstore.repository.UserProfileRepository
import com.example.bookstore.repository.AuthRepository

class UserProfileViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserProfileViewModel::class.java)) {
            val apiService = RetrofitInstance.getApiService(context.applicationContext)
            val tokenManager = TokenManager(context.applicationContext)
            val repository = UserProfileRepository(apiService)
            val authRepository = AuthRepository(apiService, tokenManager)

            @Suppress("UNCHECKED_CAST")
            return UserProfileViewModel(repository, authRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}