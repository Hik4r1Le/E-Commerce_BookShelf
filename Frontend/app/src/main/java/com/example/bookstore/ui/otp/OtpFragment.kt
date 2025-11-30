package com.example.bookstore.ui.otp

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.bookstore.R
import com.example.bookstore.ui.book_detail.BookDetailViewModel
import com.example.bookstore.ui.book_detail.BookDetailViewModelFactory
import com.example.bookstore.ui.theme.BookstoreTheme

class OtpFragment : Fragment(R.layout.fragment_otp) {
    // Nhận tham số từ Safe Args
    private val args: OtpFragmentArgs by navArgs()
    private lateinit var initialEmail: String
    private lateinit var initialType: String
    private lateinit var viewModel: OtpViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initialEmail = args.email
        initialType = args.otpType

        viewModel = ViewModelProvider(this, OtpViewModelFactory(requireContext(), initialEmail, initialType))
            .get(OtpViewModel::class.java)

        val composeView = view.findViewById<ComposeView>(R.id.composeView)
        composeView.setContent {
            BookstoreTheme {
                OtpScreen(
                    viewModel = viewModel,
                    onVerifySuccess = {
                        when (initialType) {
                            // 1. Đăng ký (Verify Email)
                            Constants.OTP_TYPE_VERIFY_EMAIL -> {
                                // Sau khi xác thực email đăng ký, chuyển về màn hình Login
                                val action = OtpFragmentDirections.actionOtpToLogin()
                                findNavController().navigate(action)
                            }

                            // 2. Quên Mật khẩu (Reset Password)
                            Constants.OTP_TYPE_RESET_PASSWORD -> {
                                // Sau khi xác thực OTP, chuyển sang màn hình Reset Password
                                val action = OtpFragmentDirections.actionOtpToResetPassword()
                                findNavController().navigate(action)
                            }
                        }
                    },
                    onBackClick = {
                        findNavController().popBackStack()
                    }
                )
            }
        }
    }
}

@Composable
fun OtpScreen(
    viewModel: OtpViewModel,
    onVerifySuccess: () -> Unit,
    onBackClick: () -> Unit
) {
    val uiState = viewModel.uiState.value

    // Xử lý thông báo thành công sau xác thực
    LaunchedEffect(uiState.isVerified) {
        if (uiState.isVerified) {
            onVerifySuccess()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF9F9F9))
    ) {
        AuthHeader(onBackClick = onBackClick, title = "Xác thực OTP", subtitle = "Nhập mã OTP được gửi qua email")

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .offset(y = (-32).dp)
                .clip(RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))
                .background(Color.White)
                .padding(24.dp)
        ) {
            OtpForm(
                email = uiState.email,
                otpCode = uiState.otpCode,
                onOtpCodeChange = viewModel::onOtpCodeChange,
                onVerify = { viewModel.verifyOtp(onVerifySuccess) },
                onResend = viewModel::resendOtp,
                uiState = uiState
            )
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

@Composable
fun OtpForm(
    email: String,
    otpCode: String,
    onOtpCodeChange: (String) -> Unit,
    onVerify: () -> Unit,
    onResend: () -> Unit,
    uiState: OtpUiState
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth().padding(top = 16.dp, bottom = 16.dp)
    ) {
        Text(
            text = "Mã OTP đã được gửi tới:",
            fontSize = 16.sp,
            color = Color.DarkGray
        )
        Text(
            text = email,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        // Ô nhập OTP
        OutlinedTextField(
            value = otpCode,
            onValueChange = onOtpCodeChange,
            label = { Text("Nhập mã OTP (6 chữ số)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            isError = uiState.errorMessage != null
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Nút Xác thực
        Button(
            onClick = onVerify,
            enabled = !uiState.isLoading && otpCode.length >= 4,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF48FB1)),
            modifier = Modifier.fillMaxWidth().height(48.dp)
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White)
            } else {
                Text("Xác thực", fontWeight = FontWeight.Bold, color = Color.White)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Nút Gửi lại OTP
        Text(
            text = "Không nhận được mã? Gửi lại",
            color = Color(0xFFB0AEE0),
            modifier = Modifier.clickable(onClick = onResend)
        )

        // Hiển thị trạng thái/lỗi
        uiState.errorMessage?.let {
            Text(text = it, color = Color.Red, modifier = Modifier.padding(top = 8.dp))
        }
        uiState.successMessage?.let {
            Text(text = it, color = Color.Green, modifier = Modifier.padding(top = 8.dp))
        }
    }
}