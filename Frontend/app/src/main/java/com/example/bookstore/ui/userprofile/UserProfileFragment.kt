package com.example.bookstore.ui.profile

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.bookstore.R
import com.example.bookstore.model.UserProfileUiState
import com.example.bookstore.ui.userprofile.UserProfileViewModel

private val HeaderPurple = Color(0xFFA7AAE1)
private val AccentYellow = Color(0xFFFFC107)

// Fragment
class UserProfileFragment : Fragment() {

    private val viewModel: UserProfileViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                val uiState by viewModel.uiState.collectAsState()
                var showAvatarPicker by remember { mutableStateOf(false) }

                MaterialTheme {
                    UserProfileScreen(
                        uiState = uiState,
                        onBack = {
                            requireActivity()
                                .onBackPressedDispatcher
                                .onBackPressed()
                        },
                        onEdit = {},
                        onPickAvatar = { showAvatarPicker = true },
                        onSave = { viewModel.saveProfile() },
                        onNameChange = viewModel::onNameChange,
                        onPhoneChange = viewModel::onPhoneChange,
                        onAddressChange = viewModel::onAddressChange
                    )

                    if (showAvatarPicker) {
                        AvatarPickerDialog(
                            onDismiss = { showAvatarPicker = false },
                            onAvatarPicked = {
                                viewModel.onAvatarChange(it)
                                showAvatarPicker = false
                            }
                        )
                    }
                }
            }
        }
    }
}

// Screen
@Composable
fun UserProfileScreen(
    uiState: UserProfileUiState,
    onBack: () -> Unit,
    onEdit: () -> Unit,
    onPickAvatar: () -> Unit,
    onSave: () -> Unit,
    onNameChange: (String) -> Unit,
    onPhoneChange: (String) -> Unit,
    onAddressChange: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFF8F3))
    ) {
        UserProfileHeader(uiState, onEdit, onPickAvatar)
        Spacer(modifier = Modifier.height(16.dp))
        UserProfileForm(
            uiState = uiState,
            onBack = onBack,
            onSave = onSave,
            onNameChange = onNameChange,
            onPhoneChange = onPhoneChange,
            onAddressChange = onAddressChange,
            bottomSpacer = 90.dp
        )
    }
}

// Header
@Composable
fun UserProfileHeader(
    uiState: UserProfileUiState,
    onEdit: () -> Unit,
    onPickAvatar: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .background(HeaderPurple)
            .padding(16.dp)
    ) {

        // Edit button
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .offset(x = (-16).dp)
                .size(34.dp)
                .clip(CircleShape)
                .background(Color.White)
                .clickable { onEdit() },
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.Edit, contentDescription = null, tint = AccentYellow)
        }

        // Avatar + name/email
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterStart)
                .padding(start = 32.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box {
                Box(
                    modifier = Modifier
                        .size(96.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFF5D3C4)),
                    contentAlignment = Alignment.Center
                ) {
                    if (uiState.avatarUri != null) {
                        AndroidView(
                            factory = { context ->
                                ImageView(context).apply {
                                    scaleType = ImageView.ScaleType.CENTER_CROP
                                    setImageURI(uiState.avatarUri)
                                }
                            },
                            modifier = Modifier
                                .size(88.dp)
                                .clip(CircleShape)
                        )
                    } else {
                        Image(
                            painter = painterResource(R.drawable.avatar_demo),
                            contentDescription = null,
                            modifier = Modifier
                                .size(88.dp)
                                .clip(CircleShape)
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .offset(6.dp, 6.dp)
                        .size(26.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                        .clickable { onPickAvatar() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.CameraAlt,
                        contentDescription = null,
                        tint = AccentYellow,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(24.dp))

            Column {
                Text(
                    uiState.name,
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    uiState.email,
                    color = Color.Black,
                    fontSize = 14.sp
                )
            }
        }

        // Seller Panel - Notifications - Logout
        Row(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 16.dp, bottom = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Seller Panel", color = Color.Red)
            Icon(
                Icons.Default.Notifications,
                contentDescription = "Notifications",
                tint = AccentYellow
            )
            Text("Đăng xuất", color = Color.Red)
        }
    }
}

// Form
@Composable
fun UserProfileForm(
    uiState: UserProfileUiState,
    onBack: () -> Unit,
    onSave: () -> Unit,
    onNameChange: (String) -> Unit,
    onPhoneChange: (String) -> Unit,
    onAddressChange: (String) -> Unit,
    bottomSpacer: Dp
) {
    Column {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
        ) {
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

        Column(
            modifier = Modifier.padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            ProfileTextField("Họ và tên", uiState.name, onNameChange)
            ProfileTextField("Email (*)", uiState.email, {})
            ProfileTextField("Số điện thoại", uiState.phone, onPhoneChange)
            ProfileTextField("Địa chỉ", uiState.address, onAddressChange)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onSave,
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .fillMaxWidth()
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF2AEBB)),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                "LƯU",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 30.sp
            )
        }

        Spacer(modifier = Modifier.height(bottomSpacer))
    }
}

@Composable
fun ProfileTextField(
    label: String,
    value: String,
    onChange: (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onChange,
        label = { Text(label) },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp)
    )
}

// Avatar Picker Dialog
@Composable
fun AvatarPickerDialog(
    onDismiss: () -> Unit,
    onAvatarPicked: (Uri?) -> Unit
) {
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        onAvatarPicked(uri)
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Chọn ảnh đại diện") },
        text = { Text("Chọn ảnh từ thư viện") },
        confirmButton = {
            TextButton(onClick = { launcher.launch("image/*") }) {
                Text("Gallery")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Hủy")
            }
        }
    )
}

// Previews
@Preview(showBackground = true)
@Composable
fun Preview_Header() {
    UserProfileHeader(
        uiState = UserProfileUiState(
            name = "Nguyễn Văn A",
            email = "nguyenvana123@gmail.com"
        ),
        onEdit = {},
        onPickAvatar = {}
    )
}

@Preview(showBackground = true, heightDp = 500)
@Composable
fun Preview_Form() {
    UserProfileForm(
        uiState = UserProfileUiState(
            name = "Nguyễn Văn A",
            email = "nguyenvana123@gmail.com",
            phone = "0987654321",
            address = "Khu phố 34, P. Linh Xuân, TP. Hồ Chí Minh"
        ),
        onBack = {},
        onSave = {},
        onNameChange = {},
        onPhoneChange = {},
        onAddressChange = {},
        bottomSpacer = 10.dp
    )
}

@Preview(showBackground = true, heightDp = 800)
@Composable
fun Preview_Full() {
    UserProfileScreen(
        uiState = UserProfileUiState(
            name = "Nguyễn Văn A",
            email = "nguyenvana123@gmail.com",
            phone = "0987654321",
            address = "Khu phố 34, P. Linh Xuân, TP. Hồ Chí Minh"
        ),
        onBack = {},
        onEdit = {},
        onPickAvatar = {},
        onSave = {},
        onNameChange = {},
        onPhoneChange = {},
        onAddressChange = {}
    )
}