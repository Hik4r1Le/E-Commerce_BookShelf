package com.example.bookstore.ui.forgot_password

import android.os.Bundle
import android.view.View
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.bookstore.R
import com.example.bookstore.ui.otp.Constants
import com.example.bookstore.ui.theme.BookstoreTheme
import androidx.navigation.NavDirections

class ForgotPasswordFragment : Fragment(R.layout.fragment_forgot_password) {
    private val viewModel: ForgotPasswordViewModel by viewModels {
        ForgotPasswordViewModelFactory(requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val composeView = view.findViewById<ComposeView>(R.id.composeView)
        composeView.setContent {
            BookstoreTheme {
                ForgotPasswordScreen(
                    viewModel = viewModel,
                    onBackClick = {
                        findNavController().popBackStack()
                    },
                    onSendOtpSuccess = { email ->
                        // LOGIC ĐIỀU HƯỚNG SANG MÀN HÌNH OTP
                        val action: NavDirections = ForgotPasswordFragmentDirections.actionForgotPasswordToOtp(
                            email = email,
                            otpType = Constants.OTP_TYPE_RESET_PASSWORD // <--- Đã gửi type RESET_PASSWORD
                        )
                        findNavController().navigate(action)
                    }
                )
            }
        }
    }
}

@Composable
fun ForgotPasswordScreen(
    viewModel: ForgotPasswordViewModel,
    onBackClick: () -> Unit,
    onSendOtpSuccess: (String) -> Unit
) {
    val uiState = viewModel.uiState.value

    // Xử lý sự kiện gửi OTP thành công
    LaunchedEffect(uiState.isOtpSent) {
        if (uiState.isOtpSent) {
            onSendOtpSuccess(uiState.email)
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        AuthHeader(onBackClick = onBackClick, title = "Quên mật khẩu", subtitle = "Nhập email của bạn để nhận mã xác thực")

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .offset(y = (-32).dp)
                .clip(RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))
                .background(Color.White)
                .padding(24.dp)
        ) {
            ForgotPasswordForm(
                email = uiState.email,
                onEmailChange = viewModel::onEmailChange,
                onSendRequest = { viewModel.sendOtpRequest(onSendOtpSuccess) },
                uiState = uiState
            )
        }
    }
}

@Composable
fun ForgotPasswordForm(
    email: String,
    onEmailChange: (String) -> Unit,
    onSendRequest: () -> Unit,
    uiState: ForgotPasswordUiState
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        // Email Input
        OutlinedTextField(
            value = email,
            onValueChange = onEmailChange,
            label = { Text("Địa chỉ Email") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            isError = uiState.errorMessage != null
        )

        // Hiển thị lỗi
        uiState.errorMessage?.let {
            Text(text = it, color = Color.Red, modifier = Modifier.padding(top = 8.dp))
        }

        // Hiển thị thông báo thành công
        uiState.successMessage?.let {
            Text(text = it, color = Color.Green, modifier = Modifier.padding(top = 8.dp))
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Nút Gửi OTP
        Button(
            onClick = onSendRequest,
            enabled = !uiState.isLoading,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF48FB1)),
            modifier = Modifier.fillMaxWidth().height(48.dp)
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White)
            } else {
                Text("Gửi mã OTP", fontWeight = FontWeight.Bold, color = Color.White)
            }
        }
    }
}

@Composable
fun AuthHeader(onBackClick: () -> Unit, title: String, subtitle: String) {
    // Tái sử dụng Header tương tự Login/Register
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFB0AEE0))
            .padding(horizontal = 16.dp, vertical = 24.dp)
            .height(180.dp) // Định chiều cao cố định
    ) {
        // Nút Back
        IconButton(onClick = onBackClick, modifier = Modifier.align(Alignment.TopStart)) {
            // Thay bằng Icon Arrow Back thật nếu có
            Text("<-", color = Color.White, fontSize = 24.sp)
        }

        Column(
            modifier = Modifier.align(Alignment.CenterStart).padding(top = 24.dp)
        ) {
            Text(
                text = title,
                color = Color.White,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = subtitle,
                color = Color.White,
                fontSize = 16.sp,
            )
        }
    }
}