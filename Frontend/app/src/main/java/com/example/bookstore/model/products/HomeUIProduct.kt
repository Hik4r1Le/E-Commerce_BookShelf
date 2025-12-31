package com.example.bookstore.model.products

data class HomeUIProduct(
    val id: String,
    val name: String,
    val authorName: String,
    val price: Double,
    val discount: Double,
    val imageUrl: String,
    val categoryName: String,
    val ratingAvg: Double,
    val isBestSeller: Boolean = false
)

fun HomeProductItem.toUIModel(): HomeUIProduct {
    return HomeUIProduct(
        id = this.product.id,
        name = this.product.name,
        authorName = this.product.authorName,
        price = this.product.price,
        discount = this.product.discount,
        imageUrl = this.product.imageUrl,
        categoryName = this.category.name,
        ratingAvg = this.product.ratingAvg,
        // Giả sử có logic để xác định BestSeller, ví dụ: dựa vào tag hoặc soldCount
        isBestSeller = this.product.tag == "best_seller" // hoặc logic khác
    )
}
