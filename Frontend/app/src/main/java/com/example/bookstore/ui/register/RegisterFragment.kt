package com.example.bookstore.ui.register

import android.os.Bundle
import android.view.View
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.ui.tooling.preview.Preview
import com.example.bookstore.R
import com.example.bookstore.ui.theme.BookstoreTheme

class RegisterFragment : Fragment(R.layout.fragment_register) {

    private val viewModel: RegisterViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val composeView = view.findViewById<ComposeView>(R.id.composeView)
        composeView.setContent {
            BookstoreTheme {
                RegisterScreen(
                    uiState = viewModel.uiState.collectAsState().value,
                    onRegister = { credentials ->
                        viewModel.updateCredentials(credentials)
                        viewModel.register {
                            // TODO: navigate next
                        }
                    },
                    onLoginClick = {}
                )
            }
        }
    }
}

// Composables
@Composable
fun RegisterScreen(
    onLoginClick: () -> Unit = {},
    onRegister: (RegisterCredentials) -> Unit = {},
    uiState: RegisterUiState
) {
    var credentials by remember { mutableStateOf(RegisterCredentials()) }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize()) {
        RegisterHeader(onLoginClick = onLoginClick)

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .offset(y = (-32).dp)
                .clip(RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))
                .background(Color.White)
                .padding(24.dp)
        ) {
            RegisterForm(
                credentials = credentials,
                onValueChange = { credentials = it },
                passwordVisible = passwordVisible,
                onPasswordToggle = { passwordVisible = !passwordVisible },
                confirmPasswordVisible = confirmPasswordVisible,
                onConfirmPasswordToggle = { confirmPasswordVisible = !confirmPasswordVisible },
                onRegister = { onRegister(credentials) },
                uiState = uiState
            )
        }
    }
}

@Composable
fun RegisterHeader(onLoginClick: () -> Unit = {}) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFB0AEE0))
            .padding(horizontal = 16.dp, vertical = 24.dp)
    ) {
        Text(
            text = "STORYA",
            color = Color.White,
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.TopCenter)
        )
        Text(
            text = "Đăng nhập",
            color = Color.White,
            fontSize = 16.sp,
            modifier = Modifier.align(Alignment.TopEnd).clickable { onLoginClick() }
        )
        Row(modifier = Modifier.padding(top = 48.dp)) {
            Column(modifier = Modifier.weight(3f), verticalArrangement = Arrangement.Top) {
                Text(
                    text = "Đăng ký",
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Hãy tạo tài khoản để tiếp tục!",
                    color = Color.White,
                    fontSize = 14.sp
                )
            }
            Icon(
                painter = painterResource(id = R.drawable.app_icon),
                contentDescription = "Logo",
                tint = Color(0xFFFFB300),
                modifier = Modifier.padding(top = 8.dp, end = 8.dp).size(96.dp).weight(1f)
            )
        }
    }
}

@Composable
fun RegisterForm(
    credentials: RegisterCredentials,
    onValueChange: (RegisterCredentials) -> Unit,
    passwordVisible: Boolean,
    onPasswordToggle: () -> Unit,
    confirmPasswordVisible: Boolean,
    onConfirmPasswordToggle: () -> Unit,
    onRegister: () -> Unit,
    uiState: RegisterUiState
) {
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = credentials.username,
            onValueChange = { onValueChange(credentials.copy(username = it)) },
            label = { Text("Tên tài khoản (*)") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = credentials.fullname,
            onValueChange = { onValueChange(credentials.copy(fullname = it)) },
            label = { Text("Họ và tên (*)") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = credentials.email,
            onValueChange = { onValueChange(credentials.copy(email = it)) },
            label = { Text("Email (*)") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = credentials.password,
            onValueChange = { onValueChange(credentials.copy(password = it)) },
            label = { Text("Mật khẩu (*)") },
            singleLine = true,
            trailingIcon = {
                val icon = if (passwordVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility
                IconButton(onClick = onPasswordToggle) { Icon(icon, contentDescription = null) }
            },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = credentials.confirmPassword,
            onValueChange = { onValueChange(credentials.copy(confirmPassword = it)) },
            label = { Text("Nhập lại mật khẩu (*)") },
            singleLine = true,
            trailingIcon = {
                val icon = if (confirmPasswordVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility
                IconButton(onClick = onConfirmPasswordToggle) { Icon(icon, contentDescription = null) }
            },
            visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = onRegister,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF48FB1)),
            modifier = Modifier.fillMaxWidth().height(48.dp)
        ) {
            Text("Đăng ký", fontWeight = FontWeight.Bold, color = Color.White)
        }

        if (uiState.isLoading) {
            Spacer(modifier = Modifier.height(16.dp))
            LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
        }

        uiState.errorMessage?.let {
            Text(text = it, color = Color.Red, modifier = Modifier.padding(top = 8.dp))
        }

        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Bằng việc đăng nhập/ đăng ký, bạn đồng ý với Điều khoản và Chính sách bảo mật của chúng tôi",
            color = Color.Gray,
            fontSize = 12.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

// Previews
@Preview(showBackground = true)
@Composable
fun PreviewRegisterHeader() {
    BookstoreTheme { RegisterHeader(onLoginClick = {}) }
}

@Preview(showBackground = true)
@Composable
fun PreviewRegisterForm() {
    BookstoreTheme {
        val credentials = remember { mutableStateOf(RegisterCredentials()) }
        val passwordVisible = remember { mutableStateOf(false) }
        val confirmPasswordVisible = remember { mutableStateOf(false) }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 8.dp)
        ) {
            RegisterForm(
                credentials = credentials.value,
                onValueChange = { credentials.value = it },
                passwordVisible = passwordVisible.value,
                onPasswordToggle = { passwordVisible.value = !passwordVisible.value },
                confirmPasswordVisible = confirmPasswordVisible.value,
                onConfirmPasswordToggle = { confirmPasswordVisible.value = !confirmPasswordVisible.value },
                onRegister = {},
                uiState = RegisterUiState()
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewRegisterScreen() {
    val fakeUiState = RegisterUiState(isLoading = false)
    BookstoreTheme {
        RegisterScreen(
            onLoginClick = {},
            onRegister = {},
            uiState = fakeUiState
        )
    }
}
