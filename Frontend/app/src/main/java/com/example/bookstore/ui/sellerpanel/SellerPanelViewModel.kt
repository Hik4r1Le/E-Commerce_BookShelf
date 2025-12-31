package com.example.bookstore.ui.sellerpanel

import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateListOf
import com.example.bookstore.R
import com.example.bookstore.model.SellerProduct
import com.example.bookstore.model.Order
import com.example.bookstore.model.OrderProduct

class SellerPanelViewModel : ViewModel() {

    // Tab State

    var selectedTab: Int = 0

    // Product

    val products = mutableStateListOf(
        SellerProduct(
            "Muôn kiếp nhân sinh",
            "Nguyên Phong",
            "99.000đ",
            "Còn hàng",
            50,
            R.drawable.book6
        ),
        SellerProduct(
            "Cho tôi xin một vé đi tuổi thơ",
            "Nguyễn Nhật Ánh",
            "69.000đ",
            "Hết hàng",
            0,
            R.drawable.book7
        )
    )

    fun addProduct(product: SellerProduct) {
        products.add(product)
    }

    fun removeProduct(product: SellerProduct) {
        products.remove(product)
    }

    fun updateProduct(index: Int, product: SellerProduct) {
        if (index in products.indices) {
            products[index] = product
        }
    }

    // Order

    val orders = mutableStateListOf(
        Order(
            "001",
            "Nguyễn Văn A",
            "12/12/2025",
            "Mới",
            "257.000đ",
            "Khu phố 34, P. Linh Xuân, TP.Hồ Chí Minh",
            listOf(
                OrderProduct("Muôn kiếp nhân sinh", 1, "99.000đ", R.drawable.book6),
                OrderProduct("Nhà giả kim", 2, "89.000đ", R.drawable.book8)
            )
        ),
        Order(
            "002",
            "Trần Thị B",
            "13/12/2025",
            "Đang xử lý",
            "480.000đ",
            "Ktx khu B, P. Linh Trung, TP. Hồ Chí Minh",
            listOf(
                OrderProduct("Muôn kiếp nhân sinh", 2, "99.000đ", R.drawable.book6),
                OrderProduct("Nhà giả kim", 3, "89.000đ", R.drawable.book8)
            )
        )
    )

    fun updateOrderStatus(orderId: String, newStatus: String) {
        val index = orders.indexOfFirst { it.id == orderId }
        if (index != -1) {
            orders[index] = orders[index].copy(status = newStatus)
        }
    }
}