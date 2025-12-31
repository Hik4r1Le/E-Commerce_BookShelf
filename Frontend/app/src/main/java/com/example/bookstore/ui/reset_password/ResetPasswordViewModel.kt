package com.example.bookstore.ui.reset_password

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State

import com.example.bookstore.model.reset_password.ResetPasswordRequest
import com.example.bookstore.repository.AuthRepository

data class ResetPasswordUiState(
    val newPassword: String = "",
    val confirmPassword: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isPasswordReset: Boolean = false,
    val successMessage: String? = null
)

class ResetPasswordViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {
    private val _uiState = mutableStateOf(ResetPasswordUiState())
    val uiState: State<ResetPasswordUiState> = _uiState

    // Handle Input
    fun onNewPasswordChange(newPass: String) {
        _uiState.value = _uiState.value.copy(newPassword = newPass, errorMessage = null)
    }

    fun onConfirmPasswordChange(confirmPass: String) {
        _uiState.value = _uiState.value.copy(confirmPassword = confirmPass, errorMessage = null)
    }

    // --- Logic Đặt lại Mật khẩu ---
    fun resetPassword(onSuccess: () -> Unit) {
        val currentState = _uiState.value

        // 1. Validation
        val validationError = validate(currentState)
        if (validationError != null) {
            _uiState.value = currentState.copy(errorMessage = validationError)
            return
        }

        viewModelScope.launch {
            _uiState.value = currentState.copy(isLoading = true, errorMessage = null, successMessage = null)

            val request = ResetPasswordRequest(password = currentState.newPassword)
            val result = authRepository.resetPassword(request)

            result.onSuccess {
                // BƯỚC QUAN TRỌNG: XÓA TOKEN TẠM THỜI SAU KHI ĐỔI MẬT KHẨU THÀNH CÔNG
                authRepository.logout()

                _uiState.value = currentState.copy(
                    isPasswordReset = true,
                    successMessage = "Mật khẩu đã được đặt lại thành công! Vui lòng đăng nhập lại."
                )
                onSuccess() // Chuyển về màn hình Login
            }.onFailure { e ->
                _uiState.value = currentState.copy(errorMessage = e.message)
            }

            _uiState.value = _uiState.value.copy(isLoading = false)
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }

    // Validation đơn giản cho mật khẩu
    private fun validate(state: ResetPasswordUiState): String? {
        if (state.newPassword.isBlank()) return "Vui lòng nhập mật khẩu mới."
        if (state.newPassword.length < 6) return "Mật khẩu phải có ít nhất 6 ký tự."
        if (state.newPassword != state.confirmPassword) return "Mật khẩu xác nhận không khớp."
        return null
    }
}