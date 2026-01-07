package com.example.bookstore.ui.sellerpanel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.bookstore.network.RetrofitInstance
import com.example.bookstore.repository.SellerPanelRepository

class SellerPanelViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SellerPanelViewModel::class.java)) {
            val apiService = RetrofitInstance.getApiService(context.applicationContext)
            val repository = SellerPanelRepository(apiService)
            @Suppress("UNCHECKED_CAST")
            return SellerPanelViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}