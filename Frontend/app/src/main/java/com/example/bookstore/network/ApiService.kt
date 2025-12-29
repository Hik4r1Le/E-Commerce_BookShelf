package com.example.bookstore.network

import com.example.bookstore.model.login.LoginRequest
import com.example.bookstore.model.login.LoginResponse
import com.example.bookstore.model.login.GoogleLoginRequest
import com.example.bookstore.model.register.RegisterRequest
import com.example.bookstore.model.register.RegisterResponse
import com.example.bookstore.model.forgot_password.ForgotPasswordRequest
import com.example.bookstore.model.reset_password.ResetPasswordRequest
import com.example.bookstore.model.otp.ResendOtpRequest
import com.example.bookstore.model.otp.VerifyOtpRequest
import com.example.bookstore.model.otp.VerifyOtpResponse
import com.example.bookstore.model.products.HomeProductResponse
import com.example.bookstore.model.products.ProductDetailResponse
import com.example.bookstore.model.cart.CartDetailResponse
import com.example.bookstore.model.cart.AddToCartRequest
import com.example.bookstore.model.cart.UpdateCartItemRequest

import com.example.bookstore.model.checkout.CheckoutReviewRequest
import com.example.bookstore.model.checkout.CheckoutReviewResponse
import com.example.bookstore.model.order.CreateOrderRequest
import com.example.bookstore.model.order.OrderResponse
import com.example.bookstore.model.userprofile.UserProfileResponse

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Path
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PATCH
import retrofit2.http.DELETE

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Multipart
import retrofit2.http.Part

interface ApiService {
    // Login API
    @POST("/api/v1/auth/login")
    suspend fun login(@Body credentials: LoginRequest): Response<LoginResponse>
    // Register API
    @POST("/api/v1/auth/register")
    suspend fun register(@Body newUserCredentials: RegisterRequest): Response<RegisterResponse>
    // Login with Google API
    @POST("/api/v1/auth/oauth/google/mobile")
    suspend fun loginWithGoogle(@Body request: GoogleLoginRequest): Response<LoginResponse>
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

    // ------------------ CART APIs ------------------

    // API Xem chi tiết các sản phẩm trong giỏ hàng
    @GET("/api/v1/cart/details")
    suspend fun getCartDetails(): Response<CartDetailResponse>

    // API Thêm một sản phẩm vào giỏ hàng
    @POST("/api/v1/cart/details")
    suspend fun addToCart(@Body item: AddToCartRequest): Response<Unit>

    // API Cập nhập số lượng sản phẩm của 1 cart item
    @PATCH("/api/v1/cart/details/{id}")
    suspend fun updateCartItem(
        @Path("id") cartItemId: String,
        @Body updateInfo: UpdateCartItemRequest
    ): Response<Unit>

    // API Xóa một cart item
    @DELETE("/api/v1/cart/details/{id}")
    suspend fun deleteCartItem(@Path("id") cartItemId: String): Response<Unit>

    // API Xóa tất cả cart item
    @DELETE("/api/v1/cart/clear")
    suspend fun clearCart(): Response<Unit>

    // ------------------ CHECKOUT & ORDER APIs ------------------

    // API khi vào checkout
    @POST("/api/v1/checkout/review")
    suspend fun checkoutReview(@Body request: CheckoutReviewRequest): Response<CheckoutReviewResponse>

    // API Tạo order sau khi checkout
    @POST("/api/v1/order")
    suspend fun createOrder(@Body request: CreateOrderRequest): Response<Unit>

    // API Xem các đơn hàng
    @GET("/api/v1/order")
    suspend fun getOrders(): Response<OrderResponse>

    // User Profile API
    @GET("/api/v1/user-profile")
    suspend fun getUserProfile(): Response<UserProfileResponse>

    @Multipart
    @PATCH("/api/v1/user-profile")
    suspend fun updateUserProfile(
        @Part avatar: MultipartBody.Part?,
        @Part("avatar_url") avatarUrl: RequestBody?,
        @Part("fullname") fullname: RequestBody,
        @Part("dob") dob: RequestBody,
        @Part("gender") gender: RequestBody,
        @Part("address_id") addressId: RequestBody?,
        @Part("phone_number") phoneNumber: RequestBody,
        @Part("street") street: RequestBody,
        @Part("district") district: RequestBody,
        @Part("city") city: RequestBody
    ): Response<Unit>
}