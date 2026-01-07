package com.example.bookstore.ui.sellerpanel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookstore.model.sellerpanel.*
import com.example.bookstore.repository.SellerPanelRepository
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

data class SellerPanelUiState(
    val isLoading: Boolean = false,
    val selectedTab: Int = 0,
    val products: List<SellerProductUIModel> = emptyList(),
    val categories: List<SellerCategoryItem> = emptyList(),
    val orders: List<SellerOrderUIModel> = emptyList(),
    val error: String? = null,
    val actionSuccess: Boolean = false
)

class SellerPanelViewModel(private val repository: SellerPanelRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(SellerPanelUiState())
    val uiState: StateFlow<SellerPanelUiState> = _uiState

    fun onTabSelected(index: Int) {
        _uiState.update { it.copy(selectedTab = index) }
        loadData()
    }

    fun loadData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            if (_uiState.value.selectedTab == 0) {
                repository.getSellerProducts().onSuccess { resp ->
                    _uiState.update { it.copy(
                        isLoading = false,
                        products = resp.data.products.map { it.toUIModel() },
                        categories = resp.data.categories
                    )}
                }.onFailure { e -> _uiState.update { it.copy(isLoading = false, error = e.message) } }
            } else {
                repository.getSellerOrders().onSuccess { resp ->
                    _uiState.update { it.copy(
                        isLoading = false,
                        orders = resp.data.map { it.toUIModel() }
                    )}
                }.onFailure { e -> _uiState.update { it.copy(isLoading = false, error = e.message) } }
            }
        }
    }

    fun addProduct(context: Context, name: String, author: String, desc: String, price: String, qty: String, catId: String, imageUri: Uri?) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val fields = mutableMapOf<String, RequestBody>()
            fields["name"] = name.toPart()
            fields["author_name"] = author.toPart()
            fields["description"] = desc.toPart()
            fields["price"] = price.toPart()
            fields["quantity"] = qty.toPart()
            fields["category_id"] = catId.toPart()

            val imagePart = imageUri?.let { prepareFilePart(context, "image", it) }
            repository.createSellerProduct(imagePart, fields).onSuccess {
                _uiState.update { it.copy(isLoading = false, actionSuccess = true) }
                loadData()
            }.onFailure { e -> _uiState.update { it.copy(isLoading = false, error = e.message) } }
        }
    }

    fun updateProduct(
        context: Context,
        productId: String,
        name: String?,
        author: String?,
        desc: String?,
        price: String?,
        qty: String?,
        catId: String?,
        imageUri: Uri?
    ) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val fields = mutableMapOf<String, RequestBody>()

            // Chỉ thêm vào Map những trường có dữ liệu (Partial Update)
            name?.takeIf { it.isNotBlank() }?.let { fields["name"] = it.toPart() }
            author?.takeIf { it.isNotBlank() }?.let { fields["author_name"] = it.toPart() }
            desc?.takeIf { it.isNotBlank() }?.let { fields["description"] = it.toPart() }
            price?.takeIf { it.isNotBlank() }?.let { fields["price"] = it.toPart() }
            qty?.takeIf { it.isNotBlank() }?.let { fields["quantity"] = it.toPart() }
            catId?.takeIf { it.isNotBlank() }?.let { fields["category_id"] = it.toPart() }

            val imagePart = imageUri?.let { prepareFilePart(context, "image", it) }

            repository.updateSellerProduct(productId, imagePart, fields).onSuccess {
                _uiState.update { it.copy(isLoading = false, actionSuccess = true) }
                loadData() // Reload danh sách sau khi sửa
            }.onFailure { e ->
                _uiState.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }

    fun deleteProduct(productId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            repository.deleteSellerProduct(productId).onSuccess {
                _uiState.update { it.copy(isLoading = false) }
                loadData()
            }.onFailure { e -> _uiState.update { it.copy(isLoading = false, error = e.message) } }
        }
    }

    fun updateOrderStatus(orderId: String, currentStatus: String) {
        val nextStatus = when (currentStatus) {
            "PENDING" -> "PROCESSING"
            "PROCESSING" -> "SHIPPING"
            else -> return
        }
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            repository.updateOrderStatus(orderId, nextStatus).onSuccess {
                loadData()
            }.onFailure { e -> _uiState.update { it.copy(isLoading = false, error = e.message) } }
        }
    }

    // Helper conversion functions
    private fun String.toPart() = this.toRequestBody("text/plain".toMediaTypeOrNull())

    private fun prepareFilePart(context: Context, partName: String, fileUri: Uri): MultipartBody.Part? {
        val file = File(context.cacheDir, "temp_img_${System.currentTimeMillis()}.png")
        context.contentResolver.openInputStream(fileUri)?.use { input ->
            FileOutputStream(file).use { output -> input.copyTo(output) }
        }
        return MultipartBody.Part.createFormData(partName, file.name, file.asRequestBody(context.contentResolver.getType(fileUri)?.toMediaTypeOrNull()))
    }
}