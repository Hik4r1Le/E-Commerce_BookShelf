package com.example.bookstore.model.order

data class OrderUIModel(
    val id: String,
    val productId: String,
    val productName: String,
    val authorName: String,
    val imageUrl: String,
    val categoryName: String,
    val status: String,
    val quantity: Int,
    val totalPrice: Double, // Tổng giá của sản phẩm đã tính phí giao hàng và giảm giá
    val unitPrice: Double, // Giá gốc
    val discount: Double
)

// Extension function để chuyển đổi từ OrderDetailItem sang OrderUIModel
fun OrderDetailItem.toUIModel(): OrderUIModel {
    val product = this.stock.product
    return OrderUIModel(
        id = this.id,
        productId = product.id,
        productName = product.name,
        authorName = product.authorName,
        imageUrl = product.imageUrl,
        categoryName = product?.productCategory?.firstOrNull()?.category?.name ?: "Khác",
        status = this.status,
        quantity = this.quantity,
        totalPrice = this.totalPrice,
        unitPrice = product.price,
        discount = product.discount
    )
}