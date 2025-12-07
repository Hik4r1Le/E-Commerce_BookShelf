package com.example.bookstore.ui.checkout

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.bookstore.network.RetrofitInstance
import com.example.bookstore.repository.CheckoutRepository
import com.example.bookstore.repository.OrderRepository

class CheckoutViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CheckoutViewModel::class.java)) {
            val apiService = RetrofitInstance.getApiService(context.applicationContext)
            val checkoutRepository = CheckoutRepository(apiService)
            val orderRepository = OrderRepository(apiService)

            @Suppress("UNCHECKED_CAST")
            return CheckoutViewModel(checkoutRepository, orderRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}