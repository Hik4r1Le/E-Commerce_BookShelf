package com.example.bookstore.ui.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf

import com.example.bookstore.model.cart.CartUIModel
import com.example.bookstore.model.cart.UpdateCartItemRequest
import com.example.bookstore.model.cart.toUIModel
import com.example.bookstore.repository.CartRepository


// Định nghĩa CartUiState để gom nhóm toàn bộ trạng thái
data class CartUiState(
    // Sử dụng CartUIModel đã được cập nhật
    val cartItems: List<CartUIModel> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

class CartViewModel(private val repository: CartRepository) : ViewModel() {

    // Sử dụng Compose MutableState để quản lý trạng thái
    private val _uiState = mutableStateOf(CartUiState())
    val uiState: State<CartUiState> = _uiState

    // --- HÀM HỖ TRỢ CẬP NHẬT TRẠNG THÁI ---

    private fun updateUiState(newState: CartUiState) {
        _uiState.value = newState
    }

    private fun updateCartItems(newItems: List<CartUIModel>) {
        _uiState.value = _uiState.value.copy(cartItems = newItems, errorMessage = null)
    }

    private fun updateError(message: String?) {
        _uiState.value = _uiState.value.copy(errorMessage = message, isLoading = false)
    }

    // --- CHỨC NĂNG CƠ BẢN ---

    // API: Xem chi tiết các sản phẩm trong giỏ hàng
    fun loadCartDetails() {
        viewModelScope.launch {
            updateUiState(_uiState.value.copy(isLoading = true, errorMessage = null))

            val result = repository.getCartDetails()

            result.onSuccess { response ->
                // Ánh xạ từ CartItemDetail (API) sang CartUIModel (UI)
                val newItems = response.data.map { it.toUIModel() }

                updateUiState(_uiState.value.copy(cartItems = newItems))
            }.onFailure { e ->
                updateError(e.message)
                updateCartItems(emptyList())
            }

            updateUiState(_uiState.value.copy(isLoading = false))
        }
    }

    // Chức năng: Toggle trạng thái chọn/bỏ chọn local
    fun toggleChecked(cartItemId: String, checked: Boolean) {
        val currentItems = _uiState.value.cartItems
        val newItems = currentItems.map {
            // Sử dụng copy để tạo CartUIModel mới với trạng thái checked đã đổi
            if (it.cartItemId == cartItemId) it.copy(checked = checked) else it
        }
        updateCartItems(newItems)
    }

    // API: Cập nhật số lượng sản phẩm của 1 cart item
    fun updateQuantity(cartItemId: String, newQuantity: Int) {
        if (newQuantity <= 0) {
            removeItem(cartItemId)
            return
        }

        viewModelScope.launch {
            val currentItem = _uiState.value.cartItems.find { it.cartItemId == cartItemId }
            if (currentItem == null) return@launch

            // Chuẩn bị Request Body
            val updateInfo = UpdateCartItemRequest(
                quantity = newQuantity,
                priceAtAdd = currentItem.priceAtAdd
            )

            val result = repository.updateCartItem(cartItemId, updateInfo)

            result.onSuccess {
                // Cập nhật local state nếu API thành công
                val newItems = _uiState.value.cartItems.map {
                    if (it.cartItemId == cartItemId) it.copy(quantity = newQuantity) else it
                }
                updateCartItems(newItems)
            }.onFailure { e ->
                updateError(e.message)
            }
        }
    }

    // API: Xóa một cart item
    fun removeItem(cartItemId: String) {
        viewModelScope.launch {
            val result = repository.deleteCartItem(cartItemId)

            result.onSuccess {
                val newItems = _uiState.value.cartItems.filter { it.cartItemId != cartItemId }
                updateCartItems(newItems)
            }.onFailure { e ->
                updateError(e.message)
            }
        }
    }

    // API: Xóa tất cả cart item
    fun clearAllCart() {
        viewModelScope.launch {
            val result = repository.clearCart()

            result.onSuccess {
                updateCartItems(emptyList())
            }.onFailure { e ->
                updateError(e.message)
            }
        }
    }

    // Logic: Tính tổng tiền các sản phẩm ĐÃ CHỌN (giá sau giảm)
    fun getTotalPrice(): Double {
        return _uiState.value.cartItems
            .filter { it.checked }
            .sumOf { (it.price * (1.0 - it.discount / 100.0)) * it.quantity }
    }

    // Logic: Tính tổng số lượng sản phẩm ĐÃ CHỌN
    fun getTotalItems(): Int =
        _uiState.value.cartItems.filter { it.checked }.sumOf { it.quantity }

    fun getCheckedCartItemIds(): Array<String> {
        return _uiState.value.cartItems
            .filter { it.checked }
            .map { it.cartItemId }
            .toTypedArray()
    }
}