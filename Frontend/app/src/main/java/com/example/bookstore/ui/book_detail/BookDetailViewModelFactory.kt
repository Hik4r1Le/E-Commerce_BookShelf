package com.example.bookstore.ui.book_detail

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.bookstore.network.RetrofitInstance
import com.example.bookstore.repository.ProductRepository
import com.example.bookstore.repository.CartRepository

class BookDetailViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BookDetailViewModel::class.java)) {
            val appCtx = context.applicationContext
            val apiService = RetrofitInstance.getApiService(appCtx)
            val productRepository = ProductRepository(apiService)
            val cartRepository = CartRepository(apiService)

            @Suppress("UNCHECKED_CAST")
            return BookDetailViewModel(productRepository, cartRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}