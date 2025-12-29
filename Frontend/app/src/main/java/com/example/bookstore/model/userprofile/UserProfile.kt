package com.example.bookstore.model.userprofile

import com.google.gson.annotations.SerializedName

// --- API GET Response Models ---
data class UserProfileResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("data") val data: ProfileData
)

data class ProfileData(
    @SerializedName("profile") val profile: UserProfileInfo,
    @SerializedName("address") val address: UserAddressInfo?
)

data class UserProfileInfo(
    @SerializedName("fullname") val fullname: String?,
    @SerializedName("dob") val dob: String?,
    @SerializedName("gender") val gender: String?,
    @SerializedName("avatar_url") val avatarUrl: String?
)

data class UserAddressInfo(
    @SerializedName("id") val id: String?,
    @SerializedName("recipient_name") val recipientName: String?,
    @SerializedName("phone_number") val phoneNumber: String?,
    @SerializedName("street") val street: String?,
    @SerializedName("district") val district: String?,
    @SerializedName("city") val city: String?
)

// --- UI Model (Dùng để hiển thị lên Fragment) ---
data class UserProfileUIModel(
    val fullname: String = "",
    val dob: String = "", // YYYY-MM-DD
    val gender: String = "OTHERS", // MALE, FEMALE, OTHERS
    val avatarUrl: String? = null,
    val addressId: String? = null,
    val phoneNumber: String = "",
    val street: String = "",
    val district: String = "",
    val city: String = ""
)

// Extension function chuyển đổi từ Response sang UI Model
fun ProfileData.toUIModel(): UserProfileUIModel {
    val rawDate = profile.dob?.split("T")?.get(0) ?: "" // "2001-05-10"

    val uiDate = if (rawDate.contains("-")) {
        val parts = rawDate.split("-") // [2001, 05, 10]
        if (parts.size == 3) "${parts[2]}${parts[1]}${parts[0]}" else ""
    } else ""

    return UserProfileUIModel(
        fullname = profile.fullname ?: "",
        dob = uiDate, // Bây giờ UI sẽ nhận được "10052001" và hiển thị "10/05/2001"
        gender = profile.gender ?: "OTHERS",
        avatarUrl = profile.avatarUrl,
        addressId = address?.id,
        phoneNumber = address?.phoneNumber ?: "",
        street = address?.street ?: "",
        district = address?.district ?: "",
        city = address?.city ?: ""
    )
}