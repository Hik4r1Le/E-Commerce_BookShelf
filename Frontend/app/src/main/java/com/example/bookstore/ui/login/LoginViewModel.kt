package com.example.bookstore.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookstore.model.LoginCredentials
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

class LoginViewModel : ViewModel() {

    // UI state variables
    var credentials by mutableStateOf(LoginCredentials())
        private set

    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    // handle input
    fun onUsernameChange(newUsername: String) {
        credentials = credentials.copy(username = newUsername)
    }

    fun onPasswordChange(newPassword: String) {
        credentials = credentials.copy(password = newPassword)
    }

    // simulate login process
    fun login(onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null

            delay(1200) // simulate API call delay - replace with API call

            if (credentials.username == "admin" && credentials.password == "1234") {
                onSuccess()
            } else {
                errorMessage = "Sai tài khoản hoặc mật khẩu"
            }

            isLoading = false
        }
    }

    fun clearError() {
        errorMessage = null
    }
}
