package com.example.bookstore.ui.userprofile

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookstore.model.userprofile.toUIModel
import com.example.bookstore.model.userprofile.UserProfileUIModel
import com.example.bookstore.repository.UserProfileRepository
import com.example.bookstore.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.FileOutputStream

data class UserProfileUiState(
    val isLoading: Boolean = false,
    val data: UserProfileUIModel = UserProfileUIModel(),
    val email: String = "", // Chỉ để hiển thị
    val selectedLocalUri: Uri? = null,
    val error: String? = null,
    val phoneError: String? = null,
    val updateSuccess: Boolean = false,
    val isLoggedOut: Boolean = false
)

class UserProfileViewModel(
    private val repository: UserProfileRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(UserProfileUiState())
    val uiState: StateFlow<UserProfileUiState> = _uiState

    fun loadProfile() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            repository.getUserProfile().onSuccess { resp ->
                _uiState.update { it.copy(
                    isLoading = false,
                    data = resp.data.toUIModel(),
                    email = "nguyenvana123@gmail.com" // Giả sử lấy từ session/auth
                )}
            }.onFailure { e ->
                _uiState.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }

    // Các hàm update field
    fun onNameChange(v: String) = _uiState.update { it.copy(data = it.data.copy(fullname = v)) }
    fun onDobChange(v: String) = _uiState.update { it.copy(data = it.data.copy(dob = v)) }
    fun onGenderChange(v: String) = _uiState.update { it.copy(data = it.data.copy(gender = v)) }
    fun onPhoneChange(v: String) {
        _uiState.update { it.copy(
            data = it.data.copy(phoneNumber = v),
            phoneError = null // Xóa lỗi ngay khi người dùng bắt đầu sửa lại
        )}
    }
    fun onStreetChange(v: String) = _uiState.update { it.copy(data = it.data.copy(street = v)) }
    fun onDistrictChange(v: String) = _uiState.update { it.copy(data = it.data.copy(district = v)) }
    fun onCityChange(v: String) = _uiState.update { it.copy(data = it.data.copy(city = v)) }
    fun onAvatarChange(uri: Uri?) = _uiState.update { it.copy(selectedLocalUri = uri) }

    fun saveProfile(context: Context) {
        viewModelScope.launch {
            val state = _uiState.value
            val profile = state.data

            val phone = state.data.phoneNumber

            // Kiểm tra validate trước khi chạy Coroutine
            if (phone.length < 8 || phone.length > 20) {
                _uiState.update { it.copy(phoneError = "Số điện thoại phải từ 8 đến 20 chữ số") }
                return@launch
            }

            // Chuyển đổi dữ liệu sang RequestBody
            val fields = mutableMapOf<String, RequestBody>()
            fields["fullname"] = profile.fullname.toRequestBody("text/plain".toMediaTypeOrNull())

            val formattedDob = formatDobToApi(profile.dob)
            fields["dob"] = formattedDob.toRequestBody("text/plain".toMediaTypeOrNull())

            fields["gender"] = profile.gender.toRequestBody("text/plain".toMediaTypeOrNull())
            fields["phone_number"] = profile.phoneNumber.toRequestBody("text/plain".toMediaTypeOrNull())
            fields["street"] = profile.street.toRequestBody("text/plain".toMediaTypeOrNull())
            fields["district"] = profile.district.toRequestBody("text/plain".toMediaTypeOrNull())
            fields["city"] = profile.city.toRequestBody("text/plain".toMediaTypeOrNull())

            // Rule: address_id null nếu ban đầu không có
            profile.addressId?.let {
                fields["address_id"] = it.toRequestBody("text/plain".toMediaTypeOrNull())
            }

            // Rule avatar
            var avatarPart: MultipartBody.Part? = null
            if (state.selectedLocalUri != null) {
                // Có ảnh mới -> avatar_url = null
                avatarPart = prepareFilePart(context, "avatar", state.selectedLocalUri)
            } else {
                // Không có ảnh mới -> avatar_url = url hiện tại
                profile.avatarUrl?.let {
                    fields["avatar_url"] = it.toRequestBody("text/plain".toMediaTypeOrNull())
                }
            }

            _uiState.update { it.copy(isLoading = true, error = null) }
            repository.updateUserProfile(avatarPart, fields).onSuccess {
                _uiState.update { it.copy(isLoading = false, updateSuccess = true) }
            }.onFailure { e ->
                _uiState.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }

    private fun prepareFilePart(context: Context, partName: String, fileUri: Uri): MultipartBody.Part? {
        val file = uriToFile(context, fileUri) ?: return null
        val requestFile = file.asRequestBody(context.contentResolver.getType(fileUri)?.toMediaTypeOrNull())
        return MultipartBody.Part.createFormData(partName, file.name, requestFile)
    }

    private fun uriToFile(context: Context, uri: Uri): File? {
        val file = File(context.cacheDir, "temp_avatar_${System.currentTimeMillis()}.png")
        context.contentResolver.openInputStream(uri)?.use { input ->
            FileOutputStream(file).use { output -> input.copyTo(output) }
        }
        return if (file.exists()) file else null
    }

    private fun formatDobToApi(dob: String): String {
        // Nếu người dùng nhập đủ 8 ký tự DDMMYYYY (ví dụ 10052001)
        return if (dob.length == 8) {
            val day = dob.substring(0, 2)
            val month = dob.substring(2, 4)
            val year = dob.substring(4, 8)
            "$year-$month-$day" // Kết quả: 2001-05-10
        } else {
            dob // Hoặc xử lý báo lỗi nếu không đủ ký tự
        }
    }

    fun logout() {
        viewModelScope.launch {
            authRepository.logout() // Xóa token qua Repository
            _uiState.update { it.copy(isLoggedOut = true) }
        }
    }
}