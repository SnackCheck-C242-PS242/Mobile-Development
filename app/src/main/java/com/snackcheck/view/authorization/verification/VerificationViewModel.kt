package com.snackcheck.view.authorization.verification

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.snackcheck.data.ResultState
import com.snackcheck.data.UserRepository
import com.snackcheck.data.remote.model.MessageResponse
import kotlinx.coroutines.launch

class VerificationViewModel(private val repository: UserRepository) : ViewModel() {
    private val _responseResult = MutableLiveData<ResultState<MessageResponse>>()
    val responseResult = _responseResult

    private val _resetResponseResult = MutableLiveData<ResultState<MessageResponse>>()
    val resetResponseResult = _resetResponseResult

    fun verify(email: String, verificationCode: String) {
        viewModelScope.launch {
            try {
                _responseResult.value = ResultState.Loading
                val response = repository.verifyAccount(email, verificationCode)
                if (response.status == "success") {
                    _responseResult.value = ResultState.Success(response)
                } else {
                    _responseResult.value = ResultState.Error(response.message.toString())
                }
            } catch (e: Exception) {
                _responseResult.value = ResultState.Error(e.message.toString())
            }
        }
    }

    fun verifyResetCode(email: String, resetCode: String) {
        viewModelScope.launch {
            try {
                _resetResponseResult.value = ResultState.Loading
                val response = repository.verifyResetCode(email, resetCode)
                if (response.status == "success") {
                    _resetResponseResult.value = ResultState.Success(response)
                } else {
                    _resetResponseResult.value = ResultState.Error(response.message.toString())
                }
            } catch (e: Exception) {
                _resetResponseResult.value = ResultState.Error(e.message.toString())
            }
        }
    }
}