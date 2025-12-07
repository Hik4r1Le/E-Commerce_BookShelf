package com.example.bookstore.ui.checkout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf

import com.example.bookstore.model.checkout.CheckoutReviewRequest
import com.example.bookstore.model.checkout.CheckoutUIModel
import com.example.bookstore.model.checkout.toUIModel
import com.example.bookstore.model.order.CreateOrderRequest
import com.example.bookstore.model.order.OrderItemRequest
import com.example.bookstore.repository.CheckoutRepository
import com.example.bookstore.repository.OrderRepository

// Định nghĩa trạng thái UI
data class CheckoutUiState(
    val reviewData: CheckoutUIModel? = null,
    val isLoadingReview: Boolean = false,
    val isPlacingOrder: Boolean = false,
    val errorMessage: String? = null,
    val orderSuccess: Boolean = false
)

class CheckoutViewModel(
    private val checkoutRepository: CheckoutRepository,
    private val orderRepository: OrderRepository,
) : ViewModel() {
    private val _uiState = mutableStateOf(CheckoutUiState())
    val uiState: State<CheckoutUiState> = _uiState

    private fun updateUiState(newState: CheckoutUiState) {
        _uiState.value = newState
    }

    private fun updateError(message: String?) {
        _uiState.value = _uiState.value.copy(errorMessage = message, isLoadingReview = false, isPlacingOrder = false)
    }

    // --- CHỨC NĂNG REVIEW CHECKOUT ---

    // API: Xem lại thông tin checkout
    fun loadCheckoutReview(cartItemIds: List<String>) {
        if (cartItemIds.isEmpty()) {
            updateError("Không có sản phẩm nào được chọn để thanh toán.")
            return
        }

        viewModelScope.launch {
            updateUiState(_uiState.value.copy(isLoadingReview = true, errorMessage = null))

            val request = CheckoutReviewRequest(cartIds = cartItemIds)
            val result = checkoutRepository.checkoutReview(request)

            result.onSuccess { response ->
                val uiModel = response.data.toUIModel()
                updateUiState(_uiState.value.copy(reviewData = uiModel, isLoadingReview = false))
            }.onFailure { e ->
                updateError(e.message)
                updateUiState(_uiState.value.copy(reviewData = null, isLoadingReview = false))
            }
        }
    }

    // --- CHỨC NĂNG ĐẶT HÀNG ---

    // API: Tạo Order sau khi Checkout
    fun placeOrder(addressId: String, shippingMethodId: String, couponId: String? = null) {
        val reviewData = _uiState.value.reviewData
        if (reviewData == null || _uiState.value.isPlacingOrder) return

        viewModelScope.launch {
            updateUiState(_uiState.value.copy(isPlacingOrder = true, errorMessage = null))

            // 1. Chuẩn bị CreateOrderRequest từ CheckoutUIModel
            val orderRequests = reviewData.productItems.map { item ->
                OrderItemRequest(
                    addressId = addressId,
                    couponId = couponId,
                    shippingMethodId = shippingMethodId,
                    stockId = item.stockId,
                    quantity = item.quantity,
                    // Tính tổng giá cho item này (đã bao gồm giảm giá sản phẩm)
                    totalPrice = item.unitPrice * item.quantity
                )
            }

            val request = CreateOrderRequest(orders = orderRequests)

            // 2. Gọi API Đặt hàng
            val result = orderRepository.createOrder(request)

            result.onSuccess {
                updateUiState(_uiState.value.copy(isPlacingOrder = false, orderSuccess = true))
            }.onFailure { e ->
                updateError(e.message)
                updateUiState(_uiState.value.copy(isPlacingOrder = false))
            }
        }
    }
}