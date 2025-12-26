package com.example.bookstore.ui.userprofile

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookstore.model.UserProfileUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class UserProfileViewModel : ViewModel() {

    // StateFlow giữ trạng thái UI
    private val _uiState = MutableStateFlow(
        UserProfileUiState(
            name = "",
            email = "",
            phone = "",
            address = "",
            avatarUri = null
        )
    )
    val uiState: StateFlow<UserProfileUiState> = _uiState

    // Cập nhật tên
    fun onNameChange(newName: String) {
        _uiState.update { it.copy(name = newName) }
    }

    // Cập nhật số điện thoại
    fun onPhoneChange(newPhone: String) {
        _uiState.update { it.copy(phone = newPhone) }
    }

    // Cập nhật địa chỉ
    fun onAddressChange(newAddress: String) {
        _uiState.update { it.copy(address = newAddress) }
    }

    // Cập nhật avatar
    fun onAvatarChange(uri: Uri?) {
        _uiState.update { it.copy(avatarUri = uri) }
    }

    // Lưu profile
    fun saveProfile() {
        viewModelScope.launch {
            val profile = _uiState.value
            // TODO: gọi repository/API lưu profile
            println("Profile saved: $profile")
        }
    }
}