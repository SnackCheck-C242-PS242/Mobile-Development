package com.snackcheck.data

import android.util.Log
import com.snackcheck.data.local.entity.SnackDetail
import com.snackcheck.data.pref.UserPreference
import com.snackcheck.data.remote.model.HistoryData
import com.snackcheck.data.remote.model.ProfileData
import com.snackcheck.data.remote.retrofit.ApiService
import kotlinx.coroutines.flow.first

class UserRepository private constructor(
    private val userPreference: UserPreference,
    private val apiService: ApiService
) {

    fun getToken() = userPreference.getToken()

    private fun getUsername() = userPreference.getUsername()

    suspend fun getApiProfile() = apiService.getProfile()

    suspend fun getUserDataPreferences(): ProfileData? {
        val data = userPreference.getProfileData().first()
        return data
    }

    suspend fun saveUserDataPreferences(profileData: ProfileData) {
        userPreference.saveProfile(profileData)
    }

    suspend fun getHistory(): Result<List<HistoryData>?>{
        return try {
            val response = apiService.getHistory()
            if (response.status == "success") {
                Result.success(response.data)
            } else {
                Result.failure(Exception("Error: ${response.status}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun saveToken(token: String) = userPreference.saveToken(token)



    suspend fun predictSnack(snackDetail: SnackDetail) = apiService.predictSnack(snackDetail)

    suspend fun refreshToken(refreshToken: String) = apiService.getNewAccessToken(refreshToken)

    suspend fun register(username: String, fullName: String, email: String, password: String, confirmPassword: String) = apiService.register(username, fullName, email, password, confirmPassword)

    suspend fun login(username: String, password: String) = apiService.login(username, password)

    suspend fun logout() {
        val username = getUserDataPreferences()?.username
        apiService.logout(username!!)
        Log.d("UserRepository", "username: $username")
        userPreference.logout()
    }

    suspend fun verifyAccount(email: String, verificationCode: String) = apiService.verifyAccount(email, verificationCode)

    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            userPreference: UserPreference,
            apiService: ApiService,
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(userPreference, apiService)
            }.also { instance = it }
    }
}