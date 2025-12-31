package com.example.bookstore.ui.checkout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import com.example.bookstore.model.checkout.*
import com.example.bookstore.model.order.CreateOrderRequest
import com.example.bookstore.model.order.OrderItemRequest
import com.example.bookstore.repository.CheckoutRepository
import com.example.bookstore.repository.OrderRepository

data class CheckoutUiState(
    val reviewData: CheckoutUIModel? = null,
    val isLoadingReview: Boolean = false,
    val isPlacingOrder: Boolean = false,
    val errorMessage: String? = null,
    val orderSuccess: Boolean = false,

    // Trạng thái lựa chọn
    val selectedAddressId: String? = null,
    val selectedShippingId: String? = null,
    val selectedCouponId: String? = null,

    // Dữ liệu nhập cho địa chỉ (Auto-fill hoặc Manual)
    val recipientName: String = "",
    val phoneNumber: String = "",
    val street: String = "",
    val district: String = "",
    val city: String = ""
)

class CheckoutViewModel(
    private val checkoutRepository: CheckoutRepository,
    private val orderRepository: OrderRepository,
) : ViewModel() {
    private val _uiState = mutableStateOf(CheckoutUiState())
    val uiState: State<CheckoutUiState> = _uiState

    fun loadCheckoutReview(cartItemIds: List<String>) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoadingReview = true, errorMessage = null)
            val result = checkoutRepository.checkoutReview(CheckoutReviewRequest(cartItemIds))
            result.onSuccess { response ->
                val uiModel = response.data.toUIModel()
                _uiState.value = _uiState.value.copy(
                    reviewData = uiModel,
                    isLoadingReview = false,
                    // Mặc định chọn cái đầu tiên nếu có
                    selectedShippingId = uiModel.shippingMethods.firstOrNull()?.id
                )
            }.onFailure { e ->
                _uiState.value = _uiState.value.copy(errorMessage = e.message, isLoadingReview = false)
            }
        }
    }

    // Khi người dùng chọn 1 địa chỉ từ danh sách
    fun onAddressSelected(address: AddressUIItem) {
        _uiState.value = _uiState.value.copy(
            selectedAddressId = address.id,
            recipientName = address.recipientName,
            phoneNumber = address.phoneNumber,
            street = address.street,
            district = address.district,
            city = address.city
        )
    }

    // Cập nhật thủ công các trường
    fun onNameChange(value: String) = _uiState.value.let { _uiState.value = it.copy(recipientName = value, selectedAddressId = null) }
    fun onPhoneChange(value: String) = _uiState.value.let { _uiState.value = it.copy(phoneNumber = value, selectedAddressId = null) }
    fun onStreetChange(value: String) = _uiState.value.let { _uiState.value = it.copy(street = value, selectedAddressId = null) }
    fun onDistrictChange(value: String) = _uiState.value.let { _uiState.value = it.copy(district = value, selectedAddressId = null) }
    fun onCityChange(value: String) = _uiState.value.let { _uiState.value = it.copy(city = value, selectedAddressId = null) }

    fun onShippingSelected(id: String) { _uiState.value = _uiState.value.copy(selectedShippingId = id) }
    fun onCouponSelected(id: String?) { _uiState.value = _uiState.value.copy(selectedCouponId = id) }

    fun placeOrder() {
        val state = _uiState.value
        val review = state.reviewData ?: return
        if (state.isPlacingOrder) return

        if (state.recipientName.isBlank() || state.phoneNumber.isBlank() || state.street.isBlank()) {
            _uiState.value = _uiState.value.copy(errorMessage = "Vui lòng điền đầy đủ thông tin giao hàng.")
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isPlacingOrder = true)

            // Tính toán giá từng item dựa trên Logic UI
            val orderRequests = review.productItems.map { item ->
                val isNewAddress = state.selectedAddressId == null

                OrderItemRequest(
                    addressId = state.selectedAddressId ?: "NEW_ADDRESS",

                    recipientName = if (isNewAddress) state.recipientName else null,
                    phoneNumber = if (isNewAddress) state.phoneNumber else null,
                    street = if (isNewAddress) state.street else null,
                    district = if (isNewAddress) state.district else null,
                    city = if (isNewAddress) state.city else null,

                    couponId = state.selectedCouponId,
                    shippingMethodId = state.selectedShippingId ?: "",
                    stockId = item.stockId,
                    quantity = item.quantity,
                    totalPrice = item.unitPrice * item.quantity
                )
            }

            val result = orderRepository.createOrder(CreateOrderRequest(orderRequests))
            result.onSuccess { _uiState.value = _uiState.value.copy(orderSuccess = true, isPlacingOrder = false) }
                .onFailure { _uiState.value = _uiState.value.copy(errorMessage = it.message, isPlacingOrder = false) }
        }
    }
}