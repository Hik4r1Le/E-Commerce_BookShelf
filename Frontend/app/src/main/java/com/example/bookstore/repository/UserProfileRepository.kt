package com.example.bookstore.repository

import com.example.bookstore.model.userprofile.UserProfileResponse
import com.example.bookstore.network.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import org.json.JSONObject

class UserProfileRepository(private val apiService: ApiService) {
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

    suspend fun getUserProfile(): Result<UserProfileResponse> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.getUserProfile()
            if (response.isSuccessful) {
                response.body()?.let { Result.success(it) } ?: Result.failure(Exception("Empty body"))
            } else Result.failure(Exception(getErrorMessage(response)))
        } catch (e: Exception) {
            Result.failure(Exception("Connection error: ${e.message}", e))
        }
    }

    suspend fun updateUserProfile(
        avatar: MultipartBody.Part?,
        data: Map<String, RequestBody>
    ): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.updateUserProfile(
                avatar = avatar,
                avatarUrl = data["avatar_url"],
                fullname = data["fullname"]!!,
                dob = data["dob"]!!,
                gender = data["gender"]!!,
                addressId = data["address_id"],
                phoneNumber = data["phone_number"]!!,
                street = data["street"]!!,
                district = data["district"]!!,
                city = data["city"]!!
            )
            if (response.isSuccessful) Result.success(Unit)
            else Result.failure(Exception(getErrorMessage(response)))
        } catch (e: Exception) {
            Result.failure(Exception("Connection error: ${e.message}", e))
        }
    }
}