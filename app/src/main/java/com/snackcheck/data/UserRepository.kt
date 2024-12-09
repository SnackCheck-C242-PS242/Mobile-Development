package com.snackcheck.data

import android.util.Log
import com.snackcheck.data.local.entity.SnackDetail
import com.snackcheck.data.pref.UserPreference
import com.snackcheck.data.remote.model.HistoryData
import com.snackcheck.data.remote.model.ProfileData
import com.snackcheck.data.remote.retrofit.ApiService
import kotlinx.coroutines.flow.first
import okhttp3.MultipartBody

class UserRepository private constructor(
    private val userPreference: UserPreference,
    private val apiService: ApiService
) {

    fun getToken() = userPreference.getToken()

    suspend fun getApiProfile() = apiService.getProfile()

    suspend fun postPhotoProfile(
        profilePhoto: MultipartBody.Part
    ) = apiService.postPhoto(profilePhoto)

    suspend fun putPhotoProfile(
        profilePhoto: MultipartBody.Part
    ) = apiService.putPhoto(profilePhoto)

    suspend fun getUserDataPreferences(): ProfileData? {
        val data = userPreference.getProfileData().first()
        return data
    }

    suspend fun saveUserDataPreferences(profileData: ProfileData) {
        userPreference.saveProfile(profileData)
    }

    suspend fun saveProfilePhotoUrl(profilePhotoUrl: String) {
        userPreference.saveProfilePhotoUrl(profilePhotoUrl)
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

    suspend fun getHistoryId(snackId: String) = apiService.getHistoryById(snackId)

    suspend fun clearHistory() = apiService.clearHistory()

    suspend fun saveToken(token: String) = userPreference.saveToken(token)

    suspend fun predictSnack(snackDetail: SnackDetail) = apiService.predictSnack(snackDetail)

    suspend fun refreshToken(refreshToken: String) = apiService.getNewAccessToken(refreshToken)

    suspend fun register(username: String, fullName: String, email: String, password: String, confirmPassword: String) = apiService.register(username, fullName, email, password, confirmPassword)

    suspend fun login(username: String, password: String) = apiService.login(username, password)

    suspend fun logout() : Result<String> {
        val username = getUserDataPreferences()?.username
        apiService.logout(username!!)
        Log.d("UserRepository", "username: $username")
        userPreference.logout()
        return Result.success("Success")
    }

    suspend fun clearUserData() = userPreference.logout()

    suspend fun verifyAccount(email: String, verificationCode: String) = apiService.verifyAccount(email, verificationCode)

    suspend fun getResetCode(email: String) = apiService.getResetCode(email)

    suspend fun resetPassword(email: String, resetCode: String, newPassword: String, confirmPassword: String) = apiService.resetPassword(email, resetCode, newPassword, confirmPassword)

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