package com.example.bookstore.model.checkout

import com.google.gson.annotations.SerializedName

data class CheckoutReviewResponse(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("data")
    val data: CheckoutReviewData
)

data class CheckoutReviewData(
    @SerializedName("address")
    val address: AddressDetail,

    @SerializedName("coupon")
    val coupon: CouponDetail?, // Có thể là null nếu không có coupon

    @SerializedName("shipping")
    val shipping: ShippingDetail,

    @SerializedName("cart")
    val cart: List<CheckoutCartItem>
)

data class AddressDetail(
    @SerializedName("id")
    val id: String,

    @SerializedName("label")
    val label: String,

    @SerializedName("recipient_name")
    val recipientName: String,

    @SerializedName("phone_number")
    val phoneNumber: String,

    @SerializedName("street")
    val street: String,

    @SerializedName("district")
    val district: String,

    @SerializedName("city")
    val city: String
)

data class CouponDetail(
    @SerializedName("id")
    val id: String,

    @SerializedName("code")
    val code: String,

    @SerializedName("discount_type")
    val discountType: String,

    @SerializedName("discount_value")
    val discountValue: Double,

    @SerializedName("start_date")
    val startDate: String, // Nên dùng kiểu Date/ZonedDateTime nếu cần xử lý ngày tháng

    @SerializedName("end_date")
    val endDate: String // Nên dùng kiểu Date/ZonedDateTime nếu cần xử lý ngày tháng
)

data class ShippingDetail(
    @SerializedName("id")
    val id: String,

    @SerializedName("type")
    val type: String,

    @SerializedName("shipping_fee")
    val shippingFee: Double,

    @SerializedName("estimated_time_text")
    val estimatedTimeText: String
)

data class CheckoutCartItem(
    @SerializedName("id")
    val id: String, // Id của cart item

    @SerializedName("quantity")
    val quantity: Int,

    @SerializedName("is_in_stock")
    val isInStock: Boolean,

    @SerializedName("stock")
    val stock: CheckoutStock
)

data class CheckoutStock(
    @SerializedName("id")
    val id: String,

    @SerializedName("quantity")
    val quantity: Int, // Số lượng còn trong kho

    @SerializedName("product")
    val product: CheckoutProduct
)

data class CheckoutProduct(
    @SerializedName("id")
    val id: String,

    @SerializedName("name")
    val name: String,

    @SerializedName("author_name")
    val authorName: String,

    @SerializedName("image_url")
    val imageUrl: String,

    @SerializedName("productCategory")
    val productCategory: CheckoutProductCategory,

    @SerializedName("price")
    val price: Double,

    @SerializedName("discount")
    val discount: Double
)

data class CheckoutProductCategory(
    @SerializedName("category")
    val category: CheckoutCategoryName
)

data class CheckoutCategoryName(
    @SerializedName("name")
    val name: String
)