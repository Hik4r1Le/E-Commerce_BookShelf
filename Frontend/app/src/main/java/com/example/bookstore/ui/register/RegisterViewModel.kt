package com.example.bookstore.ui.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf

import com.example.bookstore.model.register.RegisterRequest
import com.example.bookstore.model.register.RegisterResponse
import com.example.bookstore.repository.AuthRepository

data class RegisterCredentials(
    val username: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = ""
)

data class RegisterUiState(
    val credentials: RegisterCredentials = RegisterCredentials(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val registerResponse: RegisterResponse? = null
)

// ViewModel
class RegisterViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {
    private val _uiState = mutableStateOf(RegisterUiState())
    val uiState: State<RegisterUiState> = _uiState


    fun updateCredentials(newCredentials: RegisterCredentials) {
        _uiState.value = _uiState.value.copy(credentials = newCredentials, errorMessage = null)
    }

    fun onEmailChange(newEmail: String) {
        _uiState.value = _uiState.value.copy(
            credentials = _uiState.value.credentials.copy(email = newEmail),
            errorMessage = null
        )
    }

    fun onUsernameChange(newUsername: String) {
        _uiState.value = _uiState.value.copy(
            credentials = _uiState.value.credentials.copy(username = newUsername),
            errorMessage = null
        )
    }

    fun onPasswordChange(newPassword: String) {
        _uiState.value = _uiState.value.copy(
            credentials = _uiState.value.credentials.copy(password = newPassword),
            errorMessage = null
        )
    }

    fun onConfirmPasswordChange(newConfirmPassword: String) {
        _uiState.value = _uiState.value.copy(
            credentials = _uiState.value.credentials.copy(confirmPassword = newConfirmPassword),
            errorMessage = null
        )
    }

    fun register(onSuccess: (String) -> Unit) {
        val credentials = _uiState.value.credentials

        // 1. Validation
        val validationError = validate(credentials)
        if (validationError != null) {
            _uiState.value = _uiState.value.copy(errorMessage = validationError)
            return
        }

        // 2. Gọi API
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

            // Chuyển đổi từ UI Credentials sang API Request Model
            val apiRequest = RegisterRequest(
                email = credentials.email,
                username = credentials.username,
                password = credentials.password
            )

            val result = authRepository.register(apiRequest)
            result.onSuccess { response ->
                _uiState.value = _uiState.value.copy(registerResponse = response)
                onSuccess(credentials.email)
            }.onFailure { e ->
                _uiState.value = _uiState.value.copy(errorMessage = e.message)
            }

            _uiState.value = _uiState.value.copy(isLoading = false)
        }
    }

    // Logic kiểm tra dữ liệu đầu vào
    private fun validate(credentials: RegisterCredentials): String? {
        if (credentials.username.isBlank()) return "Vui lòng nhập tên tài khoản."
        if (credentials.email.isBlank()) return "Vui lòng nhập email."
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(credentials.email).matches()) return "Email không hợp lệ."
        if (credentials.password.isBlank()) return "Vui lòng nhập mật khẩu."
        if (credentials.password.length < 6) return "Mật khẩu phải có ít nhất 6 ký tự."
        if (credentials.confirmPassword.isBlank()) return "Vui lòng nhập lại mật khẩu xác nhận."
        if (credentials.password != credentials.confirmPassword) return "Mật khẩu xác nhận không khớp."
        return null
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }
}
