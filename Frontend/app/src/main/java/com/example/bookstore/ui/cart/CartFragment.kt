package com.example.bookstore.ui.cart

import android.os.Bundle
import android.view.View
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.bookstore.R
import com.example.bookstore.ui.theme.BookstoreTheme

class CartFragment : Fragment(R.layout.fragment_cart) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Dữ liệu giỏ hàng 
        val cartItems = mutableStateListOf(
            CartItemData(1, "Sách A", "Tác giả A", 100000, 1, R.drawable.book1),
            CartItemData(2, "Sách B", "Tác giả B", 85000, 2, R.drawable.book2),
            CartItemData(3, "Sách C", "Tác giả C", 120000, 1, R.drawable.book3)
        )

        val composeView = view.findViewById<ComposeView>(R.id.composeView)
        composeView.setContent {
            BookstoreTheme {
                CartScreen(
                    cartItems = cartItems,
                    onNavigateBack = { findNavController().popBackStack() },
                    onCheckout = { /* TODO: xử lý thanh toán */ }
                )
            }
        }
    }
}
