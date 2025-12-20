package com.example.bookstore.model.checkout

data class AddressUIItem(
    val id: String,
    val label: String,
    val recipientName: String,
    val phoneNumber: String,
    val street: String,
    val district: String,
    val city: String
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

data class CheckoutUIModel(
    val addresses: List<AddressUIItem>,
    val coupons: List<CouponDetail>,
    val shippingMethods: List<ShippingDetail>,
    val productItems: List<CheckoutUIProductItem>
)


// Extension function để chuyển đổi từ CheckoutReviewData sang CheckoutUIModel
fun CheckoutReviewData.toUIModel(): CheckoutUIModel {
    val addressList = this.address?.map { addr ->
        AddressUIItem(
            id = addr.id ?: "", // Nếu id null thì để rỗng
            label = addr.label ?: "Địa chỉ",
            recipientName = addr.recipientName ?: "Người nhận",
            phoneNumber = addr.phoneNumber ?: "",
            street = addr.street ?: "",
            district = addr.district ?: "",
            city = addr.city ?: ""
        )
    } ?: emptyList()

    val productList = this.cart?.map { cartItem ->
        val product = cartItem.stock?.product
        val primaryCategoryName = product?.productCategory?.firstOrNull()?.category?.name ?: "Khác"
        CheckoutUIProductItem(
            cartItemId = cartItem.id ?: "",
            stockId = cartItem.stock?.id ?: "",
            productId = product?.id ?: "",
            productName = product?.name ?: "Sản phẩm không tên",
            authorName = product?.authorName ?: "Ẩn danh",
            imageUrl = product?.imageUrl ?: "", // Coil sẽ tự hiện placeholder nếu url rỗng
            categoryName = primaryCategoryName,
            originalPrice = product?.price ?: 0.0,
            // Tính toán an toàn với discount
            unitPrice = (product?.price ?: 0.0) * (1.0 - (product?.discount ?: 0.0) / 100.0),
            quantity = cartItem.quantity ?: 0,
            isInStock = cartItem.isInStock ?: false
        )
    } ?: emptyList()

    return CheckoutUIModel(
        addresses = addressList,
        coupons = this.coupon ?: emptyList(), // Nếu không có coupon, trả về mảng rỗng thay vì null
        shippingMethods = this.shippingMethod ?: emptyList(), // Tương tự shipping
        productItems = productList
    )
}