package com.snackcheck.view.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.snackcheck.data.UserRepository
import com.snackcheck.data.pref.UserPreference
import kotlinx.coroutines.launch

class SettingFragmentViewModel(private val repository: UserRepository, private val pref: UserPreference) : ViewModel() {
    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }
}