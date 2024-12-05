package com.snackcheck.view.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.snackcheck.data.UserRepository
import kotlinx.coroutines.launch

class MainViewModel(private val repository: UserRepository) : ViewModel() {
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
}