package com.snackcheck.view.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.snackcheck.data.UserRepository
import com.snackcheck.data.remote.model.ProfileData
import kotlinx.coroutines.launch

class MainViewModel(private val repository: UserRepository) : ViewModel() {
    private val _userData = MutableLiveData<ProfileData?>()
    val userData: LiveData<ProfileData?> = _userData

    fun clearUserData() {
        viewModelScope.launch {
            repository.clearUserData()
        }
    }

    fun getToken(): LiveData<String?> {
        return repository.getToken().asLiveData()
    }

    fun getProfile() {
        viewModelScope.launch {
            val data = repository.getUserDataPreferences()
            _userData.value = data
            Log.d("HomeViewModel", "User Data: ${_userData.value}")
        }
    }
}