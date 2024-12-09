package com.snackcheck.view.splash

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

    fun getThemeSetting(): LiveData<Boolean> {
        return repository.getThemeSetting().asLiveData()
    }

    fun getToken(): LiveData<String?> {
        return repository.getToken().asLiveData()
    }

    fun getProfile() {
        viewModelScope.launch {
            val data = repository.getUserDataPreferences()
            _userData.value = data
        }
    }
}