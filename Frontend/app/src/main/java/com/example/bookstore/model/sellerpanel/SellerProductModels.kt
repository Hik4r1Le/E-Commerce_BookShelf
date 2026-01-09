package com.example.bookstore.model.sellerpanel

import com.google.gson.annotations.SerializedName

// --- GET Response Model ---
data class SellerPanelResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("data") val data: SellerPanelData
)

data class SellerPanelData(
    @SerializedName("product") val products: List<SellerProductItem>,
    @SerializedName("category") val categories: List<SellerCategoryItem>
)

data class SellerProductItem(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("author_name") val authorName: String,
    @SerializedName("description") val description: String?,
    @SerializedName("price") val price: String,
    @SerializedName("image_url") val imageUrl: String,
    @SerializedName("stock") val stock: SellerStockInfo,
    @SerializedName("productCategory") val productCategory: List<CategoryItemOfProduct>
)

data class SellerStockInfo(
    @SerializedName("quantity") val quantity: Int,
    @SerializedName("status") val status: String // "IN_STOCK" | "OUT_OF_STOCK"
)

data class SellerCategoryItem(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String
)

data class CategoryItemOfProduct(
    @SerializedName("category") val category: SellerCategoryItem
)

// --- UI Model để hiển thị lên giao diện ---
data class SellerProductUIModel(
    val id: String,
    val name: String,
    val authorName: String,
    val description: String,
    val price: Double,
    val imageUrl: String,
    val quantity: Int,
    val status: String,
    val productCategoryId: String,
    val productCategoryName: String,
)

fun SellerProductItem.toUIModel(): SellerProductUIModel {
    return SellerProductUIModel(
        id = this.id,
        name = this.name,
        authorName = this.authorName,
        description= this.description ?: "Không có mô tả sản phẩm",
        price = this.price.toDoubleOrNull() ?: 0.0,
        imageUrl = this.imageUrl,
        quantity = this.stock.quantity,
        status = this.stock.status,
        productCategoryId = this.productCategory.firstOrNull()?.category?.id ?: "",
        productCategoryName = this.productCategory.firstOrNull()?.category?.name ?: ""
    )
}