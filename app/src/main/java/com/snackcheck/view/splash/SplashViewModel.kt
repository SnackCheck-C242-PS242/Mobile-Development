package com.snackcheck.view.splash

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.snackcheck.data.UserRepository
import com.snackcheck.data.remote.model.ProfileData
import kotlinx.coroutines.launch

class SplashViewModel(private val repository: UserRepository) : ViewModel() {
    private val _userData = MutableLiveData<ProfileData?>()
    val userData: LiveData<ProfileData?> = _userData

    fun getToken(): LiveData<String?> {
        return repository.getToken().asLiveData()
    }

    fun refreshToken(refreshToken: String) {
        viewModelScope.launch {
            val newAccessToken = repository.refreshToken(refreshToken)
            Log.d("MainViewModel", "Old token: $refreshToken")
            Log.d("MainViewModel", "New token: $newAccessToken")
            repository.saveToken(newAccessToken.accessToken)
        }
    }

    fun getProfile() {
        viewModelScope.launch {
            val data = repository.getUserDataPreferences()
            _userData.value = data
            Log.d("HomeViewModel", "User Data: ${_userData.value}")
        }
    }
}