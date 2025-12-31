package com.example.bookstore.ui.forgot_password

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import kotlinx.coroutines.launch

import com.example.bookstore.model.forgot_password.ForgotPasswordRequest
import com.example.bookstore.repository.AuthRepository

data class ForgotPasswordUiState(
    val email: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isOtpSent: Boolean = false,
    val successMessage: String? = null
)

class ForgotPasswordViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {
    private val _uiState = mutableStateOf(ForgotPasswordUiState())
    val uiState: State<ForgotPasswordUiState> = _uiState

    // Handle Input
    fun onEmailChange(newEmail: String) {
        _uiState.value = _uiState.value.copy(email = newEmail, errorMessage = null)
    }

    // --- Logic Gửi Yêu cầu Quên Mật khẩu ---
    fun sendOtpRequest(onSuccess: (String) -> Unit) { // onSuccess nhận email
        val email = _uiState.value.email

        // 1. Validation
        val validationError = validate(email)
        if (validationError != null) {
            _uiState.value = _uiState.value.copy(errorMessage = validationError)
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null, successMessage = null)

            val request = ForgotPasswordRequest(email = email)
            val result = authRepository.forgotPassword(request)

            result.onSuccess {
                _uiState.value = _uiState.value.copy(
                    isOtpSent = true,
                    successMessage = "Mã OTP đã được gửi đến email của bạn."
                )
                onSuccess(email) // Chuyển sang màn hình OTP
            }.onFailure { e ->
                _uiState.value = _uiState.value.copy(errorMessage = e.message)
            }

            _uiState.value = _uiState.value.copy(isLoading = false)
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }

    // Validation đơn giản
    private fun validate(email: String): String? {
        if (email.isBlank()) return "Vui lòng nhập email."
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) return "Email không hợp lệ."
        return null
    }
}