package com.example.bookstore.ui.userprofile

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import coil.compose.AsyncImage
import com.example.bookstore.R
import com.example.bookstore.ui.notification.NotificationFragment

// Định nghĩa màu sắc như bản gốc của bạn
private val HeaderPurple = Color(0xFFA7AAE1)
private val AccentYellow = Color(0xFFFFC107)

class UserProfileFragment : Fragment(R.layout.fragment_user_profile) {

    private val viewModel: UserProfileViewModel by viewModels {
        UserProfileViewModelFactory(requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val composeView = view.findViewById<ComposeView>(R.id.composeView)

        viewModel.loadProfile()

        composeView.setContent {
            // Sử dụng MaterialTheme hoặc BookstoreTheme của bạn
            MaterialTheme {
                val uiState by viewModel.uiState.collectAsState()
                val context = LocalContext.current
                var showAvatarPicker by remember { mutableStateOf(false) }

                // Xử lý thông báo thành công
                LaunchedEffect(uiState.updateSuccess) {
                    if (uiState.updateSuccess) {
                        Toast.makeText(context, "Cập nhật thành công!", Toast.LENGTH_SHORT).show()
                    }
                }

                LaunchedEffect(uiState.error) {
                    uiState.error?.let {
                        // Nếu Backend trả về "Avatar image is required", bạn sẽ thấy ngay lập tức
                        Toast.makeText(context, "Lỗi: $it", Toast.LENGTH_LONG).show()
                    }
                }

                Surface(modifier = Modifier.fillMaxSize()) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        UserProfileScreen(
                            uiState = uiState,
                            onBack = { findNavController().navigateUp() },
                            onPickAvatar = { showAvatarPicker = true },
                            onSave = { viewModel.saveProfile(context) },
                            onNameChange = viewModel::onNameChange,
                            onDobChange = viewModel::onDobChange,
                            onGenderChange = viewModel::onGenderChange,
                            onPhoneChange = viewModel::onPhoneChange,
                            onStreetChange = viewModel::onStreetChange,
                            onDistrictChange = viewModel::onDistrictChange,
                            onCityChange = viewModel::onCityChange,
                            onNotificationClick = {
                                NotificationFragment.open(this@UserProfileFragment)
                            }
                        )

                        if (uiState.isLoading) {
                            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                        }
                    }

                    if (showAvatarPicker) {
                        AvatarPickerDialog(
                            onDismiss = { showAvatarPicker = false },
                            onAvatarPicked = { uri ->
                                viewModel.onAvatarChange(uri)
                                showAvatarPicker = false
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun UserProfileScreen(
    uiState: UserProfileUiState,
    onBack: () -> Unit,
    onPickAvatar: () -> Unit,
    onSave: () -> Unit,
    onNameChange: (String) -> Unit,
    onDobChange: (String) -> Unit,
    onGenderChange: (String) -> Unit,
    onPhoneChange: (String) -> Unit,
    onStreetChange: (String) -> Unit,
    onDistrictChange: (String) -> Unit,
    onCityChange: (String) -> Unit,
    onNotificationClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFF8F3))
            .verticalScroll(rememberScrollState()) // Cho phép cuộn để không bị che nút Lưu
    ) {
        UserProfileHeader(uiState, onEdit = {}, onPickAvatar, onNotificationClick)

        Spacer(modifier = Modifier.height(16.dp))

        // Form Title & Back Button
        Box(modifier = Modifier.fillMaxWidth().height(60.dp)) {
            Icon(
                Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                modifier = Modifier
                    .size(32.dp)
                    .align(Alignment.CenterStart)
                    .offset(x = 24.dp)
                    .clickable { onBack() }
            )
            Text(
                "Chỉnh sửa thông tin",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        // --- Các trường nhập liệu ---
        Column(
            modifier = Modifier.padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            ProfileTextField("Họ và tên", uiState.data.fullname, onNameChange)

            // Nhập ngày sinh thông minh (Masking DD/MM/YYYY)
            ProfileTextField(
                label = "Ngày sinh (DD/MM/YYYY)",
                value = uiState.data.dob,
                onChange = { if (it.length <= 8) onDobChange(it) },
                visualTransformation = DateMaskTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            // Chọn giới tính (Dropdown)
            GenderDropdown(selectedGender = uiState.data.gender, onGenderChange = onGenderChange)

            ProfileTextField(
                label = "Số điện thoại",
                value = uiState.data.phoneNumber,
                onChange = onPhoneChange,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                isError = uiState.phoneError != null,
                supportingText = uiState.phoneError
            )

            // Địa chỉ tách làm 3
            Text("Địa chỉ", fontWeight = FontWeight.Bold, color = Color.Gray, fontSize = 14.sp)
            ProfileTextField("Tên đường / Số nhà", uiState.data.street, onStreetChange)
            ProfileTextField("Phường / Quận", uiState.data.district, onDistrictChange)
            ProfileTextField("Tỉnh / Thành phố", uiState.data.city, onCityChange)
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Nút LƯU
        Button(
            onClick = onSave,
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .fillMaxWidth()
                .height(55.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF2AEBB)),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("LƯU", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 24.sp)
        }

        // Khoảng trống dưới cùng để không bị che bởi Bottom Nav Bar
        Spacer(modifier = Modifier.height(130.dp))
    }
}

@Composable
fun UserProfileHeader(
    uiState: UserProfileUiState,
    onEdit: () -> Unit,
    onPickAvatar: () -> Unit,
    onNotificationClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .background(HeaderPurple)
            .padding(16.dp) // Padding tổng thể cho Header
    ) {
        // 1. Nút Edit: Đặt ở góc cực trên bên phải cho thoáng
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .size(34.dp)
                .clip(CircleShape)
                .background(Color.White)
                .clickable { onEdit() },
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.Edit, null, tint = AccentYellow, modifier = Modifier.size(20.dp))
        }

        // 2. Nhóm Avatar và Tên: Căn giữa theo chiều dọc (CenterStart)
        // Giảm padding start để Avatar không bị đẩy quá sâu vào trong
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterStart)
                .padding(start = 8.dp, bottom = 12.dp), // Padding bottom để né bớt hàng nút dưới
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box {
                AsyncImage(
                    model = uiState.selectedLocalUri ?: uiState.data.avatarUrl,
                    contentDescription = null,
                    error = painterResource(R.drawable.avatar_demo),
                    placeholder = painterResource(R.drawable.avatar_demo),
                    modifier = Modifier
                        .size(90.dp) // Giảm nhẹ size để hài hòa hơn với text
                        .clip(CircleShape)
                        .border(2.dp, Color.White, CircleShape),
                    contentScale = ContentScale.Crop
                )
                // Nút Camera: Chỉnh lại offset để bám sát vòng tròn avatar
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .size(28.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                        .clickable { onPickAvatar() }
                        .border(1.dp, Color.LightGray, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.CameraAlt, null, tint = AccentYellow, modifier = Modifier.size(16.dp))
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) { // Dùng weight để text tự xuống dòng nếu quá dài
                Text(
                    text = uiState.data.fullname.ifEmpty { "Người dùng" },
                    color = Color.White,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 28.sp
                )
                // Bạn có thể thêm email ở đây nếu muốn hiển thị lại
            }
        }

        // 3. Nhóm chức năng dưới cùng: Căn sát góc dưới bên phải (BottomEnd)
        Row(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Seller Panel",
                color = Color.Red,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.clickable { /* Handle click */ }
            )

            // Icon thông báo với vùng chạm rộng hơn cho dễ bấm
            IconButton(
                onClick = onNotificationClick,
                modifier = Modifier.size(24.dp)
            ) {
                Icon(Icons.Default.Notifications, null, tint = AccentYellow)
            }

            Text(
                "Đăng xuất",
                color = Color.Red,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.clickable { /* Handle logout */ }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GenderDropdown(selectedGender: String, onGenderChange: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    val options = listOf("MALE", "FEMALE", "OTHERS")

    ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
        OutlinedTextField(
            value = selectedGender,
            onValueChange = {},
            readOnly = true,
            label = { Text("Giới tính") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier.menuAnchor().fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        )
        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = { onGenderChange(option); expanded = false }
                )
            }
        }
    }
}

@Composable
fun ProfileTextField(
    label: String,
    value: String,
    onChange: (String) -> Unit,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    isError: Boolean = false,      // Thêm tham số này
    supportingText: String? = null // Thêm tham số này
) {
    OutlinedTextField(
        value = value,
        onValueChange = onChange,
        label = { Text(label) },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        keyboardOptions = keyboardOptions,
        visualTransformation = visualTransformation,
        singleLine = true,
        isError = isError, // Kích hoạt trạng thái lỗi (viền đỏ)
        supportingText = { // Hiển thị dòng chữ báo lỗi bên dưới
            if (isError && supportingText != null) {
                Text(text = supportingText, color = MaterialTheme.colorScheme.error)
            }
        }
    )
}

// --- Logic Nhập ngày tháng thông minh DD/MM/YYYY ---
class DateMaskTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val trimmed = if (text.text.length >= 8) text.text.substring(0, 8) else text.text
        var out = ""
        for (i in trimmed.indices) {
            out += trimmed[i]
            if (i == 1 || i == 3) out += "/"
        }
        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                if (offset <= 1) return offset
                if (offset <= 3) return offset + 1
                if (offset <= 8) return offset + 2
                return 10
            }
            override fun transformedToOriginal(offset: Int): Int {
                if (offset <= 2) return offset
                if (offset <= 5) return offset - 1
                if (offset <= 10) return offset - 2
                return 8
            }
        }
        return TransformedText(AnnotatedString(out), offsetMapping)
    }
}

@Composable
fun AvatarPickerDialog(onDismiss: () -> Unit, onAvatarPicked: (Uri?) -> Unit) {
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri -> onAvatarPicked(uri) }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Ảnh đại diện") },
        text = { Text("Chọn ảnh từ thư viện của bạn") },
        confirmButton = { TextButton(onClick = { launcher.launch("image/*") }) { Text("Gallery") } },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Hủy") } }
    )
}