package com.example.bookstore.model.products

data class ProductDetailUI(
    val id: String,
    val name: String,
    val categoryName: String,
    val authorName: String,
    val description: String,
    val price: Double,
    val discount: Double,
    val imageUrl: String,
    val ratingAvg: Double,
    val ratingCount: Int,
    val stockQuantity: Int,
    val sellerName: String,
    val comments: List<ProductCommentUI>
)

data class ProductCommentUI(
    val id: String,
    val username: String,
    val rating: Double,
    val comment: String
)

fun ProductDetailResponse.toUIModel(): ProductDetailUI? {
    val detailData = this.data.firstOrNull() ?: return null
    val product = detailData.product ?: return null
    val category = detailData.category ?: return null

    val comments = product.review?.map { review ->
        ProductCommentUI(
            id = review.id ?: "",
            username = review.user?.select?.username ?: review.username ?: "Ẩn danh",
            rating = review.rating ?: 0.0,
            comment = review.comment ?: ""
        )
    } ?: emptyList()

    return ProductDetailUI(
        id = product.id ?: "",
        name = product.name ?: "Không rõ tên",
        categoryName = category.name ?: "Không rõ",
        authorName = product.authorName ?: "Không rõ tác giả",
        description = product.description ?: "Không có mô tả.",
        price = product.price ?: 0.0,
        discount = product.discount ?: 0.0,
        imageUrl = product.imageUrl ?: "",
        ratingAvg = product.ratingAvg ?: 0.0,
        ratingCount = product.ratingCount ?: 0,
        stockQuantity = product.stock?.quantity ?: 0,
        sellerName = product.seller?.profile?.fullname ?: "Không rõ người bán",
        comments = comments
    )
}
