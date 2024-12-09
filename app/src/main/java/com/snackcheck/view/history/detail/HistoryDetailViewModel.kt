package com.snackcheck.view.history.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.snackcheck.data.ResultState
import com.snackcheck.data.UserRepository
import com.snackcheck.data.remote.model.HistoryDetailResponse

import kotlinx.coroutines.launch

class HistoryDetailViewModel(private val repository: UserRepository) : ViewModel(){
    private val _historyDetail = MutableLiveData<ResultState<HistoryDetailResponse>>()
    val historyDetail : LiveData<ResultState<HistoryDetailResponse>> = _historyDetail

    fun getHistoryDetail(snackId: String) {
        viewModelScope.launch {
            try {
                _historyDetail.value = ResultState.Loading
                val response = repository.getHistoryId(snackId)
                if (response.status == "success") {
                    _historyDetail.value = ResultState.Success(response)
                } else {
                    _historyDetail.value = ResultState.Error(response.status)
                }
            } catch (e: Exception) {
                _historyDetail.value = ResultState.Error(e.message.toString())
            }
        }
    }
}