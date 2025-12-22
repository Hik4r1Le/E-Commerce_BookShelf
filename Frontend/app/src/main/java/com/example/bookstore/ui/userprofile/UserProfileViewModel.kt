package com.example.bookstore.ui.userprofile

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.example.bookstore.model.UserProfileUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class UserProfileViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(
        UserProfileUiState(
            name = "Nguyễn Văn A",
            email = "nguyenvana123@gmail.com",
            phone = "0987654321",
            address = " Khu phố 34, P. Linh Xuân, TP. Hồ Chí Minh"
        )
    )

    val uiState: StateFlow<UserProfileUiState> = _uiState

    // Update fields
    fun onNameChange(value: String) {
        _uiState.value = _uiState.value.copy(name = value)
    }

    fun onPhoneChange(value: String) {
        _uiState.value = _uiState.value.copy(phone = value)
    }

    fun onAddressChange(value: String) {
        _uiState.value = _uiState.value.copy(address = value)
    }

    fun onAvatarChange(uri: Uri?) {
        _uiState.value = _uiState.value.copy(avatarUri = uri)
    }

    // Save profile
    fun saveProfile() {
        // TODO: Call API update profile
    }
}