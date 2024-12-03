package com.snackcheck.data

import com.snackcheck.data.pref.UserModel
import com.snackcheck.data.pref.UserPreference
import com.snackcheck.data.remote.retrofit.ApiService
import kotlinx.coroutines.flow.Flow

class UserRepository private constructor(
    private val userPreference: UserPreference,
    private val apiService: ApiService
) {

    fun getToken() = userPreference.getToken()

    suspend fun saveToken(token: String) = userPreference.saveToken(token)

    suspend fun register(username: String, fullName: String, email: String, password: String, confirmPassword: String) = apiService.register(username, fullName, email, password, confirmPassword)

    suspend fun login(username: String, password: String) = apiService.login(username, password)

    suspend fun logout() {
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