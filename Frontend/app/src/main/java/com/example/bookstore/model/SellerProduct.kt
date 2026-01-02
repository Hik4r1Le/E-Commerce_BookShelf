package com.example.bookstore.model

import android.net.Uri

data class SellerProduct(
    val title: String,
    val author: String,
    val price: String,
    val status: String,
    val quantity: Int,
    val imageUri: Uri?
)