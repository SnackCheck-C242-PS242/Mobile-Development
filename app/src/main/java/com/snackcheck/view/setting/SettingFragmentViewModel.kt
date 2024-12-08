package com.snackcheck.view.setting

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.snackcheck.data.UserRepository
import com.snackcheck.data.pref.UserPreference
import kotlinx.coroutines.launch

class SettingFragmentViewModel(private val repository: UserRepository, private val pref: UserPreference) : ViewModel() {
    private val _logoutStatus = MutableLiveData<Boolean>()
    val logoutStatus: LiveData<Boolean> get() = _logoutStatus



    fun logout() {
        viewModelScope.launch {
            try {
                repository.logout()
                _logoutStatus.value = true
            } catch (e: Exception) {
                _logoutStatus.value = false
            }
        }
    }
}