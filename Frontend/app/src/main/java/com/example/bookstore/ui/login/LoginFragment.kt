package com.example.bookstore.ui.login

import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import com.example.bookstore.R
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.example.bookstore.model.LoginCredentials
import com.example.bookstore.ui.theme.BookstoreTheme


class LoginFragment : Fragment(R.layout.fragment_login) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val composeView = view.findViewById<ComposeView>(R.id.composeView)
        composeView.setContent {
            BookstoreTheme {
                LoginScreen (

                )
            }
        }
    }
}

@Composable
fun LoginScreen(
    onRegisterClick: () -> Unit = {},
    onForgotPassword: () -> Unit = {},
    onLogin: (LoginCredentials) -> Unit = {},
    onGoogleLogin: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF9F9F9)),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LoginHeader(onRegisterClick = onRegisterClick)

        Box(
            modifier = Modifier
                .offset(y = (-32).dp) // pulls the white form up slightly
                .fillMaxWidth()
                .clip(RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))
                .background(Color.White)
                .padding(24.dp)
        ) {
            LoginForm(
                onLogin = onLogin,
                onForgotPassword = onForgotPassword,
                onGoogleLogin = onGoogleLogin
            )
        }
    }
}

@Composable
fun LoginHeader(
    onRegisterClick: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = Color(0xFFB0AEE0),
            )
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
            text = "Đăng ký",
            color = Color.White,
            fontSize = 16.sp,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .clickable { onRegisterClick() }
        )
    Row() {
        Column(
            modifier = Modifier
                .padding(top = 48.dp)
                .weight(3f),
            verticalArrangement = Arrangement.Top
        ) {
            Text(
                text = "Đăng nhập",
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Đăng nhập bằng tài khoản của bạn hoặc đăng ký",
                maxLines = 2,
                color = Color.White,
                fontSize = 14.sp,
            )
        }
        Icon(
            painter = painterResource(id = R.drawable.app_icon),
            contentDescription = null,
            tint = Color(0xFFFFB300),
            modifier = Modifier
                //.align(Alignment.TopEnd)
                .padding(top = 40.dp, end = 8.dp)
                .size(96.dp)
                .weight(1f)
            )
        }
    }
}

@Composable
fun LoginForm(
    onLogin: (LoginCredentials) -> Unit,
    onForgotPassword: () -> Unit,
    onGoogleLogin: () -> Unit
) {
    var credentials by remember { mutableStateOf(LoginCredentials()) }
    var passwordVisible by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Username
        OutlinedTextField(
            value = credentials.username,
            onValueChange = { credentials = credentials.copy(username = it) },
            label = { Text("Tên tài khoản") },
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Password
        OutlinedTextField(
            value = credentials.password,
            onValueChange = { credentials = credentials.copy(password = it) },
            label = { Text("Mật khẩu") },
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            trailingIcon = {
                val icon = if (passwordVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(icon, contentDescription = null)
                }
            },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Quên mật khẩu?",
            color = Color.Gray,
            fontSize = 14.sp,
            textAlign = TextAlign.End,
            fontWeight = FontWeight.Light,
            modifier = Modifier
                .align(Alignment.End)
                .clickable { onForgotPassword() }
        )

        Spacer(modifier = Modifier.height(24.dp))
        LoginButton { onLogin(credentials) }

        Spacer(modifier = Modifier.height(16.dp))
        GoogleLoginButton(onGoogleLogin)
    }
}

@Composable
fun LoginButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF48FB1)),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
    ) {
        Text("Đăng nhập", fontWeight = FontWeight.Bold, color = Color.White)
    }
}
@Composable
fun GoogleLoginButton(onClick: () -> Unit) {
    OutlinedButton(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp),
        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Black)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.google_icon), // Replace with Google icon
                contentDescription = "Google",
                tint = Color.Unspecified,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Tiếp tục với Google")
        }
    }
}

@Preview
@Composable
fun PreviewLoginHeader() {
    LoginHeader(
        onRegisterClick = {}
    )
}

@Preview (
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Composable
fun PreviewLoginForm() {
    LoginForm(
        onLogin = {},
        onForgotPassword = {},
        onGoogleLogin = {},
    )
}

@Preview (
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Composable
fun PreviewLoginScreen() {
    BookstoreTheme { //dùng này để xem preview với font chữ mà em cài sẵn
        LoginScreen(
            onLogin = {},
            onForgotPassword = {},
            onGoogleLogin = {},
        )
    }
}