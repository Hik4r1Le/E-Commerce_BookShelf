package com.example.bookstore.model.checkout

data class CheckoutUIModel(
    // Thông tin địa chỉ
    val addressId: String,
    val recipientName: String,
    val phoneNumber: String,
    val fullAddress: String, // Gộp street, district, city

    // Thông tin Coupon
    val couponCode: String?,
    val couponDiscountValue: Double,
    val couponDiscountType: String?,

    // Thông tin Vận chuyển
    val shippingMethodId: String,
    val shippingType: String,
    val shippingFee: Double,
    val estimatedTime: String,

    // Danh sách sản phẩm
    val productItems: List<CheckoutUIProductItem>
)

data class CheckoutUIProductItem(
    val cartItemId: String,
    val stockId: String,
    val productId: String,
    val productName: String,
    val authorName: String,
    val imageUrl: String,
    val categoryName: String,
    val unitPrice: Double, // Giá sau khi giảm giá (price * (1 - discount))
    val originalPrice: Double,
    val quantity: Int,
    val isInStock: Boolean
)

// Extension function để chuyển đổi từ CheckoutReviewData sang CheckoutUIModel
fun CheckoutReviewData.toUIModel(): CheckoutUIModel {
    val fullAddress = "${this.address.street}, ${this.address.district}, ${this.address.city}"

    val productItems = this.cart.map { cartItem ->
        val product = cartItem.stock.product
        CheckoutUIProductItem(
            cartItemId = cartItem.id,
            stockId = cartItem.stock.id,
            productId = product.id,
            productName = product.name,
            authorName = product.authorName,
            imageUrl = product.imageUrl,
            categoryName = product.productCategory.category.name,
            originalPrice = product.price,
            unitPrice = product.price * (1 - product.discount / 100.0), // Giả sử discount là %
            quantity = cartItem.quantity,
            isInStock = cartItem.isInStock
        )
    }

    return CheckoutUIModel(
        addressId = this.address.id,
        recipientName = this.address.recipientName,
        phoneNumber = this.address.phoneNumber,
        fullAddress = fullAddress,

        couponCode = this.coupon?.code,
        couponDiscountValue = this.coupon?.discountValue ?: 0.0,
        couponDiscountType = this.coupon?.discountType,

        shippingMethodId = this.shipping.id,
        shippingType = this.shipping.type,
        shippingFee = this.shipping.shippingFee,
        estimatedTime = this.shipping.estimatedTimeText,

        productItems = productItems
    )
}