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
import com.example.bookstore.ui.theme.BookstoreTheme
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.compose.runtime.LaunchedEffect
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.example.bookstore.R.string as R_string

class LoginFragment : Fragment(R.layout.fragment_login) {
    private val viewModel: LoginViewModel by viewModels {
        LoginViewModelFactory(requireContext())
    }

    private lateinit var googleSignInClient: GoogleSignInClient

    // Contract để xử lý kết quả trả về từ Google Sign-In Activity
    private val googleSignInLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account = task.getResult(ApiException::class.java)
                val idToken = account.idToken

                if (idToken != null) {
                    // Gửi ID Token lên ViewModel
                    viewModel.loginWithGoogleIdToken(idToken)
                } else {
                    viewModel.onErrorMessageChange("Đăng nhập Google thất bại: Không nhận được ID Token.")
                }
            } catch (e: ApiException) {
                // Xử lý lỗi (ví dụ: người dùng hủy, lỗi mạng)
                viewModel.onErrorMessageChange("Đăng nhập Google thất bại: Lỗi ${e.statusCode}")
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.google_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)
    }

    // Hàm được gọi khi nhấn nút Google
    private fun signInWithGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        googleSignInLauncher.launch(signInIntent)
        viewModel.clearError()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val composeView = view.findViewById<ComposeView>(R.id.composeView)
        composeView.setContent {
            BookstoreTheme {
                LaunchedEffect(viewModel.loginResponse) {
                    if (viewModel.loginResponse != null) {
                        // Chuyển sang màn hình chính
                        val action = LoginFragmentDirections.actionLoginToHome()
                        findNavController().navigate(action)
                        viewModel.clearLoginResponse()
                    }
                }

                LoginScreen(
                    viewModel = viewModel,
                    onRegisterClick = {
                        val action = LoginFragmentDirections.actionLoginToRegister()
                        findNavController().navigate(action)
                    },
                    onForgotPassword = {
                        val action = LoginFragmentDirections.actionLoginToForgotPassword()
                        findNavController().navigate(action)
                    },
                    onGoogleLogin = { signInWithGoogle() },
                    onLogin = { viewModel.login() }
                )
            }
        }
    }
}

@Composable
fun LoginScreen(
    viewModel: LoginViewModel,
    onRegisterClick: () -> Unit = {},
    onForgotPassword: () -> Unit = {},
    onGoogleLogin: () -> Unit = {},
    onLogin: () -> Unit
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
                viewModel = viewModel,
                onForgotPassword = onForgotPassword,
                onGoogleLogin = onGoogleLogin,
                onLogin = onLogin
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
    viewModel: LoginViewModel,
    onForgotPassword: () -> Unit,
    onGoogleLogin: () -> Unit,
    onLogin: () -> Unit
) {
    val credentials = viewModel.credentials
    val isLoading = viewModel.isLoading
    val errorMessage = viewModel.errorMessage

    var passwordVisible by remember { mutableStateOf(false) }

    if (errorMessage != null) {
        // Bạn có thể dùng Snackbar hoặc AlertDialog để hiển thị lỗi.
        // Ví dụ: hiển thị một Text màu đỏ đơn giản
        Text(
            text = errorMessage,
            color = Color.Red,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        // Trong dự án thực tế, bạn nên dùng LaunchedEffect để hiển thị Toast/Snackbar
        // và tự động clearError() sau khi hiển thị.
    }

    Column(
        modifier = Modifier
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Email
        OutlinedTextField(
            value = credentials.email,
            onValueChange = { viewModel.onEmailChange(it) },
            label = { Text("Email người dùng") },
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Password
        OutlinedTextField(
            value = credentials.password,
            onValueChange = { viewModel.onPasswordChange(it) },
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
        LoginButton(
            isLoading = isLoading,
            onClick = { onLogin() }
        )

        Spacer(modifier = Modifier.height(16.dp))
        GoogleLoginButton(
            onClick = {
                viewModel.clearError()
                onGoogleLogin()
            }
        )
    }
}

@Composable
fun LoginButton(
    isLoading: Boolean,
    onClick: () -> Unit
) {
    Button(
        onClick = { if (!isLoading) onClick() }, // tránh spam click
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF48FB1)),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
    ) {
        if (isLoading) {
            androidx.compose.material3.CircularProgressIndicator(
                modifier = Modifier.size(20.dp),
                color = Color.White,
                strokeWidth = 2.dp
            )
        } else {
            Text("Đăng nhập", fontWeight = FontWeight.Bold, color = Color.White)
        }
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


//@Preview (
//    showBackground = true,
//    uiMode = Configuration.UI_MODE_NIGHT_NO
//)
//@Composable
//fun PreviewLoginForm() {
//    LoginForm(
//        onLogin = {},
//        onForgotPassword = {},
//        onGoogleLogin = {},
//    )
//}
//
//@Preview (
//    showBackground = true,
//    uiMode = Configuration.UI_MODE_NIGHT_NO
//)
//@Composable
//fun PreviewLoginScreen() {
//    BookstoreTheme { //dùng này để xem preview với font chữ mà em cài sẵn
//        LoginScreen(
//            onLogin = {},
//            onForgotPassword = {},
//            onGoogleLogin = {},
//        )
//    }
//}