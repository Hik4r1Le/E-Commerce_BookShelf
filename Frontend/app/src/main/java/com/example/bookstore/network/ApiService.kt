package com.example.bookstore.network

import com.example.bookstore.model.login.LoginRequest
import com.example.bookstore.model.login.LoginResponse
import com.example.bookstore.model.register.RegisterRequest
import com.example.bookstore.model.register.RegisterResponse
import com.example.bookstore.model.forgot_password.ForgotPasswordRequest
import com.example.bookstore.model.reset_password.ResetPasswordRequest
import com.example.bookstore.model.otp.ResendOtpRequest
import com.example.bookstore.model.otp.VerifyOtpRequest
import com.example.bookstore.model.otp.VerifyOtpResponse
import com.example.bookstore.model.products.HomeProductResponse
import com.example.bookstore.model.products.ProductDetailResponse

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Path
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PATCH
import retrofit2.http.DELETE

interface ApiService {
    // Login API
    @POST("/api/v1/auth/login")
    suspend fun login(@Body credentials: LoginRequest): Response<LoginResponse>
    // Register API
    @POST("/api/v1/auth/register")
    suspend fun register(@Body newUserCredentials: RegisterRequest): Response<RegisterResponse>
    // Login with Google API
    @GET("/api/v1/auth/oauth/google/mobile")
    suspend fun loginWithGoogle(@Body idToken: String): Response<LoginResponse>
    // Forgot password API
    @POST("/api/v1/auth/forgot-password")
    suspend fun forgotPassword(@Body email: ForgotPasswordRequest): Response<Unit>
    // Reset Password API
    @POST("/api/v1/auth/reset-password")
    suspend fun resetPassword(@Body password: ResetPasswordRequest): Response<Unit>
    // Resend OTP API
    @POST("/api/v1/auth/otp/resend")
    suspend fun resendOtp(@Body resendInfo: ResendOtpRequest): Response<Unit>
    // Verify OTP API
    @POST("/api/v1/auth/verify-otp")
    suspend fun verifyOtp(@Body otpInfo: VerifyOtpRequest): Response<VerifyOtpResponse>
    // Home API
    @GET("/api/v1/products/home")
    suspend fun getHomeProducts(): Response<HomeProductResponse>
    // Product detail API
    @GET("/api/v1/products/{id}")
    suspend fun getProductDetail(@Path("id") productId: String): Response<ProductDetailResponse>
}