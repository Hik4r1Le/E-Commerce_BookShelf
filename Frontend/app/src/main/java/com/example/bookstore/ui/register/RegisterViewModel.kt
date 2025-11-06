package com.example.bookstore.ui.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

// Model dữ liệu
data class RegisterCredentials(
    val username: String = "",
    val fullname: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = ""
)

data class RegisterUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

// ViewModel
class RegisterViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState

    private var currentCredentials = RegisterCredentials()

    fun updateCredentials(credentials: RegisterCredentials) {
        currentCredentials = credentials
    }

    fun register(onSuccess: () -> Unit) {
        // Validate input
        val error = validate(currentCredentials)
        if (error != null) {
            _uiState.value = RegisterUiState(isLoading = false, errorMessage = error)
            return
        }

        // Giả lập API call
        viewModelScope.launch {
            _uiState.value = RegisterUiState(isLoading = true)
            delay(2000) // simulate network delay
            _uiState.value = RegisterUiState(isLoading = false, errorMessage = null)
            onSuccess() // callback navigation
        }
    }

    private fun validate(credentials: RegisterCredentials): String? {
        if (credentials.username.isBlank()) return "Vui lòng nhập tên tài khoản"
        if (credentials.fullname.isBlank()) return "Vui lòng nhập họ và tên"
        if (credentials.email.isBlank()) return "Vui lòng nhập email"
        if (!credentials.email.contains("@")) return "Email không hợp lệ"
        if (credentials.password.isBlank()) return "Vui lòng nhập mật khẩu"
        if (credentials.password.length < 6) return "Mật khẩu phải >= 6 ký tự"
        if (credentials.password != credentials.confirmPassword) return "Mật khẩu xác nhận không khớp"
        return null
    }
}