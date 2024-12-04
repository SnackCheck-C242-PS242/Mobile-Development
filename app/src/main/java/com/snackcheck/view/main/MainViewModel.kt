package com.snackcheck.view.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.snackcheck.data.UserRepository
import com.snackcheck.data.pref.UserModel
import kotlinx.coroutines.launch

class MainViewModel(private val repository: UserRepository) : ViewModel() {
    fun getToken(): LiveData<String?> {
        return repository.getToken().asLiveData()
    }
}