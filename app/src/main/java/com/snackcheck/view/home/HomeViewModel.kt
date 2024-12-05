package com.snackcheck.view.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.snackcheck.data.UserRepository
import com.snackcheck.data.remote.model.HistoryData
import com.snackcheck.data.remote.model.ProfileData
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: UserRepository) : ViewModel() {
    private val _historyList = MutableLiveData<Result<List<HistoryData>?>>()
    val historyList : LiveData<Result<List<HistoryData>?>> = _historyList

    private val _userData = MutableLiveData<ProfileData?>()
    val userData: LiveData<ProfileData?> = _userData


    fun getHistory() {
        viewModelScope.launch {
            _historyList.value = repository.getHistory()
            Log.d("HomeViewModel", "History List: ${_historyList.value}")
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