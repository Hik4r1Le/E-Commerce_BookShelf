package com.example.bookstore.model.sellerpanel

import com.google.gson.annotations.SerializedName

// --- GET Orders Response ---
data class SellerOrderResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("data") val data: List<SellerOrderItem>
)

data class SellerOrderItem(
    @SerializedName("id") val id: String,
    @SerializedName("quantity") val quantity: Int,
    @SerializedName("total_price") val totalPrice: String,
    @SerializedName("status") val status: String,
    @SerializedName("updated_at") val updatedAt: String,
    @SerializedName("address") val address: SellerOrderAddress,
    @SerializedName("stock") val stock: SellerOrderStock
)

data class SellerOrderAddress(
    @SerializedName("recipient_name") val recipientName: String?,
    @SerializedName("street") val street: String?,
    @SerializedName("district") val district: String?,
    @SerializedName("city") val city: String?
)

data class SellerOrderStock(
    @SerializedName("product") val product: SellerOrderProduct
)

data class SellerOrderProduct(
    @SerializedName("name") val name: String,
    @SerializedName("price") val price: String,
    @SerializedName("image_url") val imageUrl: String
)

// --- PATCH Request Model cho Order Status ---
data class UpdateOrderStatusRequest(
    @SerializedName("status") val status: String // e.g., "IN_STOCK", "OUT_OF_STOCK" hoặc các trạng thái vận chuyển
)

// --- UI Model cho Đơn hàng của Seller ---
data class SellerOrderUIModel(
    val orderId: String,
    val productName: String,
    val productPrice: Double,
    val imageUrl: String,
    val quantity: Int,
    val totalPrice: Double,
    val status: String,
    val date: String,
    val customerName: String,
    val fullAddress: String
)

fun SellerOrderItem.toUIModel(): SellerOrderUIModel {
    val addr = this.address
    val fullAddr = "${addr.street ?: ""}, ${addr.district ?: ""}, ${addr.city ?: ""}".trim(',',' ')

    return SellerOrderUIModel(
        orderId = this.id,
        productName = this.stock.product.name,
        productPrice = this.stock.product.price.toDoubleOrNull() ?: 0.0,
        imageUrl = this.stock.product.imageUrl,
        quantity = this.quantity,
        totalPrice = this.totalPrice.toDoubleOrNull() ?: 0.0,
        status = this.status,
        date = this.updatedAt,
        customerName = addr.recipientName ?: "N/A",
        fullAddress = if (fullAddr.isEmpty()) "N/A" else fullAddr
    )
}