package com.example.bookstore.network

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "auth_prefs")

class TokenManager(private val context: Context) {
    // Khai báo key để lưu token
    companion object {
        private val AUTH_TOKEN_KEY = stringPreferencesKey("auth_token")
    }

    /**
     * Hàm lưu token vào DataStore.
     * @param token Chuỗi token nhận được từ API login.
     */
    suspend fun saveAuthToken(token: String) {
        context.dataStore.edit { preferences ->
            preferences[AUTH_TOKEN_KEY] = token
        }
    }

    /**
     * Hàm đọc token từ DataStore.
     * Trả về Flow<String?> để theo dõi sự thay đổi của token.
     */
    fun getAuthToken(): Flow<String?> {
        return context.dataStore.data
            .map { preferences ->
                // Trả về token, nếu không có thì là null
                preferences[AUTH_TOKEN_KEY]
            }
    }

    /**
     * Hàm xóa token khi đăng xuất.
     */
    suspend fun clearAuthToken() {
        context.dataStore.edit { preferences ->
            preferences.remove(AUTH_TOKEN_KEY)
        }
    }
}