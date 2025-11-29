package com.example.bookstore.repository

import com.example.bookstore.model.login.LoginRequest
import com.example.bookstore.model.login.LoginResponse
import com.example.bookstore.model.register.RegisterRequest
import com.example.bookstore.model.register.RegisterResponse
import com.example.bookstore.model.forgot_password.ForgotPasswordRequest
import com.example.bookstore.model.reset_password.ResetPasswordRequest
import com.example.bookstore.model.otp.ResendOtpRequest
import com.example.bookstore.model.otp.VerifyOtpRequest
import com.example.bookstore.model.otp.VerifyOtpResponse

import com.example.bookstore.network.ApiService
import com.example.bookstore.network.TokenManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import org.json.JSONObject
import kotlinx.coroutines.flow.Flow

class AuthRepository(
    private val apiService: ApiService,
    private val tokenManager: TokenManager
) {
    private fun getErrorMessage(response: Response<*>): String {
        return try {
            val errorBody = response.errorBody()?.string()
            if (!errorBody.isNullOrEmpty()) {
                val jsonObject = JSONObject(errorBody)
                jsonObject.optString("message", "Unknown error")
            } else {
                "Error: ${response.code()} ${response.message()}"
            }
        } catch (e: Exception) {
            "Unexpected error: ${response.code()}"
        }
    }

    suspend fun login(credentials: LoginRequest): Result<LoginResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.login(credentials)
                if (response.isSuccessful) {
                    response.body()?.let { loginResponse ->
                        tokenManager.saveAuthToken(loginResponse.data.token)
                        Result.success(loginResponse)
                    }
                        ?: Result.failure(Exception("Response body is empty"))
                } else {
                    Result.failure(Exception(getErrorMessage(response)))
                }
            } catch (e: Exception) {
                Result.failure(Exception("Connection error: ${e.message}", e))
            }
        }
    }

    fun getAuthToken(): Flow<String?> {
        return tokenManager.getAuthToken()
    }

    suspend fun logout() {
        tokenManager.clearAuthToken()
    }

    suspend fun loginWithGoogle(idToken: String): Result<LoginResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.loginWithGoogle(idToken)
                if (response.isSuccessful) {
                    response.body()?.let { loginResponse ->
                        tokenManager.saveAuthToken(loginResponse.data.token)
                        Result.success(loginResponse)
                    }
                        ?: Result.failure(Exception("Response body is empty"))
                } else {
                    Result.failure(Exception(getErrorMessage(response)))
                }
            } catch (e: Exception) {
                Result.failure(Exception("Connection error: ${e.message}", e))
            }
        }
    }

    suspend fun register(newUserCredentials: RegisterRequest): Result<RegisterResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.register(newUserCredentials)
                if (response.isSuccessful) {
                    response.body()?.let { Result.success(it) }
                        ?: Result.failure(Exception("Response body is empty"))
                } else {
                    Result.failure(Exception(getErrorMessage(response)))
                }
            } catch (e: Exception) {
                Result.failure(Exception("Connection error: ${e.message}", e))
            }
        }
    }

    suspend fun forgotPassword(email: ForgotPasswordRequest): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.forgotPassword(email)
                if (response.isSuccessful) {
                    Result.success(Unit)
                } else {
                    Result.failure(Exception(getErrorMessage(response)))
                }
            } catch (e: Exception) {
                Result.failure(Exception("Connection error: ${e.message}", e))
            }
        }
    }

    suspend fun resetPassword(password: ResetPasswordRequest): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.resetPassword(password)
                if (response.isSuccessful) {
                    Result.success(Unit)
                } else {
                    Result.failure(Exception(getErrorMessage(response)))
                }
            } catch (e: Exception) {
                Result.failure(Exception("Connection error: ${e.message}", e))
            }
        }
    }

    suspend fun resendOtp(resendInfo: ResendOtpRequest): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.resendOtp(resendInfo)
                if (response.isSuccessful) {
                    Result.success(Unit)
                } else {
                    Result.failure(Exception(getErrorMessage(response)))
                }
            } catch (e: Exception) {
                Result.failure(Exception("Connection error: ${e.message}", e))
            }
        }
    }

    suspend fun verifyOtp(otpInfo: VerifyOtpRequest): Result<VerifyOtpResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.verifyOtp(otpInfo)
                if (response.isSuccessful) {
                    response.body()?.let { verifyOtpResponse ->
                        val token = verifyOtpResponse.data.token
                        if (!token.isNullOrEmpty()) {
                            tokenManager.saveAuthToken(token)
                        }
                        Result.success(verifyOtpResponse)
                    }
                        ?: Result.failure(Exception("Response body is empty"))
                } else {
                    Result.failure(Exception(getErrorMessage(response)))
                }
            } catch (e: Exception) {
                Result.failure(Exception("Connection error: ${e.message}", e))
            }
        }
    }
}