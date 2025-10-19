package com.example.bookstore.ui.register

import android.os.Bundle
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
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
import androidx.compose.ui.tooling.preview.Preview
import com.example.bookstore.R
import com.example.bookstore.ui.theme.BookstoreTheme

// Fragment
class RegisterFragment : Fragment() {
    override fun onCreateView(
        inflater: android.view.LayoutInflater,
        container: android.view.ViewGroup?,
        savedInstanceState: Bundle?
    ): android.view.View {
        return ComposeView(requireContext()).apply {
            setContent {
                BookstoreTheme {
                    RegisterScreen(
                        onLoginClick = { /* TODO: navigate to login */ },
                        onRegister = { /* TODO: handle register */ }
                    )
                }
            }
        }
    }
}

// Data class
data class RegisterCredentials(
    var username: String = "",
    var fullname: String = "",
    var email: String = "",
    var password: String = "",
    var confirmPassword: String = ""
)

// Header
@Composable
fun RegisterHeader(
    onLoginClick: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFB0AEE0))
            .padding(horizontal = 16.dp, vertical = 24.dp)
    ) {
        // Tên thương hiệu
        Text(
            text = "STORYA",
            color = Color.White,
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.TopCenter)
        )

        // Nút Đăng nhập
        Text(
            text = "Đăng nhập",
            color = Color.White,
            fontSize = 16.sp,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .clickable { onLoginClick() }
        )

        Row(
            modifier = Modifier.padding(top = 48.dp)
        ) {
            Column(
                modifier = Modifier.weight(3f),
                verticalArrangement = Arrangement.Top
            ) {
                Text(
                    text = "Đăng ký",
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Hãy tạo tài khoản để tiếp tục!",
                    maxLines = 2,
                    color = Color.White,
                    fontSize = 14.sp
                )
            }

            Icon(
                painter = painterResource(id = R.drawable.app_icon),
                contentDescription = "Logo",
                tint = Color(0xFFFFB300),
                modifier = Modifier
                    .padding(top = 8.dp, end = 8.dp)
                    .size(96.dp)
                    .weight(1f)
            )
        }
    }
}

// Form đăng ký
@Composable
fun RegisterForm(
    credentials: RegisterCredentials,
    onValueChange: (RegisterCredentials) -> Unit,
    passwordVisible: Boolean,
    onPasswordToggle: () -> Unit,
    confirmPasswordVisible: Boolean,
    onConfirmPasswordToggle: () -> Unit,
    onRegister: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .offset(y = (-32).dp)
            .clip(RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))
            .background(Color.White)
            .padding(24.dp)
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

            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Bằng việc đăng nhập/ đăng ký, bạn đồng ý với Điều khoản và Chính sách bảo mật của chúng tôi",
                color = Color.Gray,
                fontSize = 12.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }
    }
}

// Toàn màn hình
@Composable
fun RegisterScreen(
    onLoginClick: () -> Unit = {},
    onRegister: () -> Unit = {}
) {
    var credentials by remember { mutableStateOf(RegisterCredentials()) }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize()) {
        RegisterHeader(onLoginClick = onLoginClick)
        RegisterForm(
            credentials = credentials,
            onValueChange = { credentials = it },
            passwordVisible = passwordVisible,
            onPasswordToggle = { passwordVisible = !passwordVisible },
            confirmPasswordVisible = confirmPasswordVisible,
            onConfirmPasswordToggle = { confirmPasswordVisible = !confirmPasswordVisible },
            onRegister = onRegister
        )
    }
}

// Previews
@Preview(showBackground = true)
@Composable
fun PreviewRegisterHeader() {
    BookstoreTheme {
        RegisterHeader(onLoginClick = {})
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewRegisterForm() {
    BookstoreTheme {
        var credentials by remember { mutableStateOf(RegisterCredentials()) }
        var passwordVisible by remember { mutableStateOf(false) }
        var confirmPasswordVisible by remember { mutableStateOf(false) }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))
                .background(Color.White)
                .padding(24.dp)
        ) {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                OutlinedTextField(
                    value = credentials.username,
                    onValueChange = { credentials = credentials.copy(username = it) },
                    label = { Text("Tên tài khoản (*)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = credentials.fullname,
                    onValueChange = { credentials = credentials.copy(fullname = it) },
                    label = { Text("Họ và tên (*)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = credentials.email,
                    onValueChange = { credentials = credentials.copy(email = it) },
                    label = { Text("Email (*)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                )
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = credentials.password,
                    onValueChange = { credentials = credentials.copy(password = it) },
                    label = { Text("Mật khẩu (*)") },
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

                OutlinedTextField(
                    value = credentials.confirmPassword,
                    onValueChange = { credentials = credentials.copy(confirmPassword = it) },
                    label = { Text("Nhập lại mật khẩu (*)") },
                    singleLine = true,
                    trailingIcon = {
                        val icon = if (confirmPasswordVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility
                        IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) { Icon(icon, contentDescription = null) }
                    },
                    visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {},
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF48FB1)),
                    modifier = Modifier.fillMaxWidth().height(48.dp)
                ) {
                    Text("Đăng ký", fontWeight = FontWeight.Bold, color = Color.White)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewRegisterScreen() {
    BookstoreTheme {
        RegisterScreen(onLoginClick = {}, onRegister = {})
    }
}
