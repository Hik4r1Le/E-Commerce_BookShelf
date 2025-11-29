package com.example.bookstore.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

import com.example.bookstore.model.login.LoginRequest
import com.example.bookstore.model.login.LoginResponse
import com.example.bookstore.repository.AuthRepository

class LoginViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {
    // UI state variables
    var credentials by mutableStateOf(LoginRequest())
        private set

    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    var loginResponse by mutableStateOf<LoginResponse?>(null)
        private set

    // handle input
    fun onEmailChange(newEmail: String) {
        credentials = credentials.copy(email = newEmail)
    }

    fun onPasswordChange(newPassword: String) {
        credentials = credentials.copy(password = newPassword)
    }

    fun onErrorMessageChange(newErrorMessage: String) {
        errorMessage = newErrorMessage
    }

    fun login(onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            loginResponse = null

            val result = authRepository.login(credentials)
            result.onSuccess {
                loginResponse = it
                onSuccess()
            }.onFailure {
                errorMessage = it.message
            }

            isLoading = false
        }
    }

    fun loginWithGoogleIdToken(idToken: String) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            loginResponse = null // Đảm bảo reset trạng thái trước khi gọi API

            val result = authRepository.loginWithGoogle(idToken)

            result.onSuccess {
                loginResponse = it
            }.onFailure { e ->
                errorMessage = e.message
            }

            isLoading = false
        }
    }

    fun clearError() {
        errorMessage = null
    }

    fun clearLoginResponse() {
        loginResponse = null
    }
}
