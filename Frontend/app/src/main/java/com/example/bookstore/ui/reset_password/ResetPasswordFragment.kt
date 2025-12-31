package com.example.bookstore.ui.reset_password

import android.os.Bundle
import android.view.View
import androidx.compose.foundation.background
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.bookstore.R
import com.example.bookstore.ui.theme.BookstoreTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff


class ResetPasswordFragment : Fragment(R.layout.fragment_reset_password) {
    private val viewModel: ResetPasswordViewModel by viewModels {
        ResetPasswordViewModelFactory(requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val composeView = view.findViewById<ComposeView>(R.id.composeView)
        composeView.setContent {
            BookstoreTheme {
                ResetPasswordScreen(
                    viewModel = viewModel,
                    onBackClick = {
                        findNavController().popBackStack()
                    },
                    onResetSuccess = {
                        val action = ResetPasswordFragmentDirections.actionResetPasswordToLogin()
                        findNavController().navigate(action)
                    }
                )
            }
        }
    }
}

@Composable
fun ResetPasswordScreen(
    viewModel: ResetPasswordViewModel,
    onBackClick: () -> Unit,
    onResetSuccess: () -> Unit
) {
    val uiState = viewModel.uiState.value

    // Xử lý sự kiện đổi mật khẩu thành công
    LaunchedEffect(uiState.isPasswordReset) {
        if (uiState.isPasswordReset) {
            onResetSuccess()
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        AuthHeader(onBackClick = onBackClick, title = "Đặt lại Mật khẩu", subtitle = "Tạo mật khẩu mới cho tài khoản của bạn")

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .offset(y = (-32).dp)
                .clip(RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))
                .background(Color.White)
                .padding(24.dp)
        ) {
            ResetPasswordForm(
                newPassword = uiState.newPassword,
                confirmPassword = uiState.confirmPassword,
                onNewPasswordChange = viewModel::onNewPasswordChange,
                onConfirmPasswordChange = viewModel::onConfirmPasswordChange,
                onResetPassword = { viewModel.resetPassword(onResetSuccess) },
                uiState = uiState
            )
        }
    }
}

@Composable
fun ResetPasswordForm(
    newPassword: String,
    confirmPassword: String,
    onNewPasswordChange: (String) -> Unit,
    onConfirmPasswordChange: (String) -> Unit,
    onResetPassword: () -> Unit,
    uiState: ResetPasswordUiState
) {
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth().padding(top = 16.dp, bottom = 16.dp)
    ) {
        // Mật khẩu mới
        OutlinedTextField(
            value = newPassword,
            onValueChange = onNewPasswordChange,
            label = { Text("Mật khẩu mới (*)") },
            singleLine = true,
            trailingIcon = {
                val icon = if (passwordVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility
                IconButton(onClick = { passwordVisible = !passwordVisible }) { Icon(icon, contentDescription = null) }
            },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Nhập lại mật khẩu mới
        OutlinedTextField(
            value = confirmPassword,
            onValueChange = onConfirmPasswordChange,
            label = { Text("Nhập lại mật khẩu (*)") },
            singleLine = true,
            trailingIcon = {
                val icon = if (confirmPasswordVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility
                IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) { Icon(icon, contentDescription = null) }
            },
            visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier.fillMaxWidth(),
            isError = uiState.errorMessage != null && uiState.errorMessage?.contains("khớp") == true
        )

        // Hiển thị lỗi
        uiState.errorMessage?.let {
            Text(text = it, color = Color.Red, modifier = Modifier.padding(top = 8.dp))
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Nút Đổi mật khẩu
        Button(
            onClick = onResetPassword,
            enabled = !uiState.isLoading,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF48FB1)),
            modifier = Modifier.fillMaxWidth().height(48.dp)
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White)
            } else {
                Text("Đổi Mật khẩu", fontWeight = FontWeight.Bold, color = Color.White)
            }
        }

        // Hiển thị thông báo thành công
        uiState.successMessage?.let {
            Text(text = it, color = Color.Green, modifier = Modifier.padding(top = 16.dp))
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