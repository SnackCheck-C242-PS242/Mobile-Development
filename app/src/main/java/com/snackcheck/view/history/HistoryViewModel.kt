package com.snackcheck.view.history

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.snackcheck.data.UserRepository
import com.snackcheck.data.remote.model.HistoryData
import kotlinx.coroutines.launch

class HistoryViewModel(private val repository: UserRepository) : ViewModel(){
    private val _historyList = MutableLiveData<Result<List<HistoryData>?>>()
    val historyList : LiveData<Result<List<HistoryData>?>> = _historyList

    fun getHistory() {
        viewModelScope.launch {
            _historyList.value = repository.getHistory()
            Log.d("HomeViewModel", "History List: ${_historyList.value}")
        }
    }
}