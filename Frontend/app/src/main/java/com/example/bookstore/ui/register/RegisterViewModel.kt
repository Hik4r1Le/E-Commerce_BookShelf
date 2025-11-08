package com.example.bookstore.ui.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookstore.model.RegisterCredentials
import com.example.bookstore.model.RegisterUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RegisterViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState

    private var _credentials = RegisterCredentials()

    fun updateCredentials(credentials: RegisterCredentials) {
        _credentials = credentials
    }

    fun register(onSuccess: () -> Unit) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            // Kiểm tra dữ liệu nhập
            if (_credentials.username.isBlank() ||
                _credentials.fullname.isBlank() ||
                _credentials.email.isBlank() ||
                _credentials.password.isBlank() ||
                _credentials.confirmPassword.isBlank()
            ) {
                _uiState.update { it.copy(isLoading = false, errorMessage = "Vui lòng điền đầy đủ thông tin") }
                return@launch
            }

            if (_credentials.password != _credentials.confirmPassword) {
                _uiState.update { it.copy(isLoading = false, errorMessage = "Mật khẩu không khớp") }
                return@launch
            }

            // Giả lập đăng ký thành công
            _uiState.update { it.copy(isLoading = false, errorMessage = null) }
            onSuccess()
        }
    }
}