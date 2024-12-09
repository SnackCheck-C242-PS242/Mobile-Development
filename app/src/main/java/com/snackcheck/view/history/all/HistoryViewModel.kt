package com.snackcheck.view.history.all

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.snackcheck.data.ResultState
import com.snackcheck.data.UserRepository
import com.snackcheck.data.remote.model.HistoryData
import com.snackcheck.data.remote.model.MessageResponse
import kotlinx.coroutines.launch

class HistoryViewModel(private val repository: UserRepository) : ViewModel(){
    private val _historyList = MutableLiveData<Result<List<HistoryData>?>>()
    val historyList : LiveData<Result<List<HistoryData>?>> = _historyList

    private val _responseClearHistory = MutableLiveData<ResultState<MessageResponse>>()
    val responseClearHistory : LiveData<ResultState<MessageResponse>> = _responseClearHistory

    fun getHistory() {
        viewModelScope.launch {
            _historyList.value = repository.getHistory()
            Log.d("HomeViewModel", "History List: ${_historyList.value}")
        }
    }

    fun clearHistory() {
        viewModelScope.launch {
            _responseClearHistory.value = ResultState.Loading
            try {
                val response = repository.clearHistory()
                _responseClearHistory.value = ResultState.Success(response)
            } catch (e: Exception) {
                _responseClearHistory.value = ResultState.Error(e.message.toString())
            }
        }
    }
}