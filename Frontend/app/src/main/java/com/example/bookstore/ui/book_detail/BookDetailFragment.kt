package com.example.bookstore.ui.login

import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
class BookDetailFragment : Fragment(R.layout.fragment_book_detail) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val composeView = view.findViewById<ComposeView>(R.id.composeView)
        composeView.setContent {
            BookstoreTheme {
                BookDetailScreen()
            }
        }
    }
}

// Data
data class RegisterCredentials(
    var username: String = "",
    var fullname: String = "",
    var email: String = "",
    var password: String = "",
    var confirmPassword: String = ""
)

// Header
@Composable
fun BookDetailHeader() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFB0AEE0))
            .padding(horizontal = 16.dp, vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "STORYA",
            fontSize = 36.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(3f),
                verticalArrangement = Arrangement.Top
            ) {
                Text(
                    text = "Đăng ký",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = "Hãy tạo tài khoản để tiếp tục!",
                    fontSize = 14.sp,
                    color = Color.White
                )
            }

            Image(
                painter = painterResource(id = R.drawable.app_icon),
                contentDescription = "Logo",
                modifier = Modifier
                    .weight(1f)
                    .size(80.dp)
            )
        }
    }
}

// Form
@Composable
fun BookDetailForm(
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
            // Username
            OutlinedTextField(
                value = credentials.username,
                onValueChange = { onValueChange(credentials.copy(username = it)) },
                label = { Text("Tên tài khoản (*)") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Fullname
            OutlinedTextField(
                value = credentials.fullname,
                onValueChange = { onValueChange(credentials.copy(fullname = it)) },
                label = { Text("Họ và tên (*)") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Email
            OutlinedTextField(
                value = credentials.email,
                onValueChange = { onValueChange(credentials.copy(email = it)) },
                label = { Text("Email (*)") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Password
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

            // Confirm password
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

// Full Screen
@Composable
fun BookDetailScreen(
    onRegister: (RegisterCredentials) -> Unit = {}
) {
    var credentials by remember { mutableStateOf(RegisterCredentials()) }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize()) {
        BookDetailHeader()
        BookDetailForm(
            credentials = credentials,
            onValueChange = { credentials = it },
            passwordVisible = passwordVisible,
            onPasswordToggle = { passwordVisible = !passwordVisible },
            confirmPasswordVisible = confirmPasswordVisible,
            onConfirmPasswordToggle = { confirmPasswordVisible = !confirmPasswordVisible },
            onRegister = { onRegister(credentials) }
        )
    }
}

// Previews
@Preview(showBackground = true)
@Composable
fun PreviewDetailHeader() {
    BookstoreTheme {
        BookDetailHeader()
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewDetailForm() {
    BookstoreTheme {
        var credentials by remember { mutableStateOf(RegisterCredentials()) }
        var passwordVisible by remember { mutableStateOf(false) }
        var confirmPasswordVisible by remember { mutableStateOf(false) }

        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp), // padding bên trái phải
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Username
                OutlinedTextField(
                    value = credentials.username,
                    onValueChange = { credentials = credentials.copy(username = it) },
                    label = { Text("Tên tài khoản (*)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(0.9f)
                )
                Spacer(modifier = Modifier.height(12.dp))

                // Fullname
                OutlinedTextField(
                    value = credentials.fullname,
                    onValueChange = { credentials = credentials.copy(fullname = it) },
                    label = { Text("Họ và tên (*)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(0.9f)
                )
                Spacer(modifier = Modifier.height(12.dp))

                // Email
                OutlinedTextField(
                    value = credentials.email,
                    onValueChange = { credentials = credentials.copy(email = it) },
                    label = { Text("Email (*)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(0.9f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                )
                Spacer(modifier = Modifier.height(12.dp))

                // Password
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
                    modifier = Modifier.fillMaxWidth(0.9f)
                )
                Spacer(modifier = Modifier.height(12.dp))

                // Confirm password
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
                    modifier = Modifier.fillMaxWidth(0.9f)
                )

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = {},
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF48FB1)),
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .height(44.dp)
                ) {
                    Text("Đăng ký", fontWeight = FontWeight.Bold, color = Color.White)
                }
            }
        }
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
fun PreviewDetailScreen() {
    BookstoreTheme {
        BookDetailScreen()
    }
}
