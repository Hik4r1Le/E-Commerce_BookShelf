package com.example.bookstore.ui.otp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State

import com.example.bookstore.repository.AuthRepository
import com.example.bookstore.model.otp.ResendOtpRequest
import com.example.bookstore.model.otp.VerifyOtpRequest
import com.example.bookstore.model.otp.VerifyOtpResponse

object Constants {
    const val OTP_TYPE_VERIFY_EMAIL = "VERIFY_EMAIL"
    const val OTP_TYPE_RESET_PASSWORD = "RESET_PASSWORD"
}

data class OtpUiState(
    val email: String = "",
    val otpCode: String = "",
    val otpType: String = Constants.OTP_TYPE_VERIFY_EMAIL,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isVerified: Boolean = false,
    val successMessage: String? = null,
)

class OtpViewModel(
    private val authRepository: AuthRepository,
    initialEmail: String,
    initialType: String
) : ViewModel() {
    private val _uiState = mutableStateOf(
        OtpUiState(email = initialEmail, otpType = initialType)
    )
    val uiState: State<OtpUiState> = _uiState

    fun onOtpCodeChange(newCode: String) {
        if (newCode.length <= 6 && newCode.all { it.isDigit() }) {
            _uiState.value = _uiState.value.copy(otpCode = newCode, errorMessage = null)
        }
    }

    fun verifyOtp(onSuccess: () -> Unit) {
        val otpCode = _uiState.value.otpCode // Lấy giá trị hiện tại

        if (otpCode.length < 4) {
            _uiState.value = _uiState.value.copy(errorMessage = "Vui lòng nhập mã OTP đầy đủ.")
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null, successMessage = null)

            val request = VerifyOtpRequest(
                email = _uiState.value.email,
                otp = otpCode.toIntOrNull() ?: 0,
                type = _uiState.value.otpType
            )

            val result = authRepository.verifyOtp(request)

            result.onSuccess {
                // Repository đã lưu token.
                _uiState.value = _uiState.value.copy(
                    successMessage = "Xác thực thành công!",
                    isVerified = true,
                    // Xóa mã OTP sau khi xác thực thành công là một thực hành tốt
                    otpCode = ""
                )
                onSuccess()
            }.onFailure { e ->
                _uiState.value = _uiState.value.copy(errorMessage = e.message)
            }

            _uiState.value = _uiState.value.copy(isLoading = false)
        }
    }

    fun resendOtp() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null, successMessage = null)

            val request = ResendOtpRequest(
                email = _uiState.value.email,
                type = _uiState.value.otpType
            )

            val result = authRepository.resendOtp(request)

            result.onSuccess {
                _uiState.value = _uiState.value.copy(successMessage = "Mã OTP mới đã được gửi!")
            }.onFailure { e ->
                _uiState.value = _uiState.value.copy(errorMessage = e.message)
            }

            _uiState.value = _uiState.value.copy(isLoading = false)
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }
}