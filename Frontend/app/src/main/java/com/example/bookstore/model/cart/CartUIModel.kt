package com.example.bookstore.model.cart

// Model đơn giản hóa để hiển thị lên UI
data class CartUIModel(
    val cartItemId: String, // Id của cart item
    val stockId: String, // Id của stock
    val productId: String, // Id của sản phẩm
    val productName: String,
    val authorName: String,
    val imageUrl: String,
    val categoryName: String,
    val price: Double, // Giá hiện tại của sản phẩm
    val discount: Double,
    val quantity: Int, // Số lượng trong giỏ hàng
    val priceAtAdd: Double, // Giá lúc thêm vào giỏ hàng (có thể dùng để tính tổng tạm thời)
    val checked: Boolean = true
)

// Extension function để chuyển đổi từ CartItemDetail sang CartUIModel
fun CartItemDetail.toUIModel(): CartUIModel {
    return CartUIModel(
        cartItemId = this.id,
        stockId = this.stock.id,
        productId = this.stock.product.id,
        productName = this.stock.product.name,
        authorName = this.stock.product.authorName,
        imageUrl = this.stock.product.imageUrl,
        categoryName = this.stock.product.productCategory.firstOrNull()?.category?.name ?: "N/A",
        price = this.stock.product.price,
        discount = this.stock.product.discount,
        quantity = this.quantity,
        priceAtAdd = this.priceAtAdd,
        checked = true
    )
}