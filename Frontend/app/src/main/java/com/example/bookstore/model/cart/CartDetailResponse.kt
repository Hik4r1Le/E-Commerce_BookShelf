package com.example.bookstore.model.cart

import com.google.gson.annotations.SerializedName

data class CartDetailResponse(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("data")
    val data: List<CartItemDetail>
)

data class CartItemDetail(
    @SerializedName("id")
    val id: String, // Id của cart item

    @SerializedName("quantity")
    val quantity: Int, // Số lượng sản phẩm trong cart item

    @SerializedName("price_at_add")
    val priceAtAdd: Double, // Giá sản phẩm tại thời điểm thêm vào giỏ hàng

    @SerializedName("stock")
    val stock: CartItemStock
)

data class CartItemStock(
    @SerializedName("id")
    val id: String, // Id của stock (phiên bản sản phẩm cụ thể)

    @SerializedName("product")
    val product: CartItemProduct
)

data class CartItemProduct(
    @SerializedName("id")
    val id: String,

    @SerializedName("name")
    val name: String,

    @SerializedName("author_name")
    val authorName: String,

    @SerializedName("image_url")
    val imageUrl: String,

    @SerializedName("productCategory")
    val productCategory: List<CartItemProductCategory>,

    @SerializedName("price")
    val price: Double,

    @SerializedName("discount")
    val discount: Double
)

data class CartItemProductCategory(
    @SerializedName("category")
    val category: CartItemCategoryName
)

data class CartItemCategoryName(
    @SerializedName("name")
    val name: String
)