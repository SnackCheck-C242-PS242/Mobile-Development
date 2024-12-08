package com.snackcheck.view.authorization.reset_password

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.snackcheck.data.ResultState
import com.snackcheck.data.UserRepository
import com.snackcheck.data.remote.model.MessageResponse
import kotlinx.coroutines.launch

class ResetPasswordViewModel(private val repository: UserRepository) : ViewModel() {
    private val _responseResult = MutableLiveData<ResultState<MessageResponse>>()
    val responseResult = _responseResult

    fun getResetCode(email: String) {
        viewModelScope.launch {
            try {
                _responseResult.value = ResultState.Loading
                val response = repository.getResetCode(email)
                if (response.status=="success") {
                    _responseResult.value = ResultState.Success(response)
                }
            } catch (e: Exception) {
                _responseResult.value = ResultState.Error(e.message.toString())
            }
        }
    }
}