package com.snackcheck.view.authorization.verification

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.snackcheck.R
import com.snackcheck.data.ResultState
import com.snackcheck.data.UserRepository
import com.snackcheck.data.remote.model.MessageResponse
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.net.SocketTimeoutException

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
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                val errorMessage = Gson().fromJson(errorBody, MessageResponse::class.java).message
                _responseResult.value = ResultState.Error(errorMessage.toString())
            } catch (e: SocketTimeoutException) {
                _responseResult.value = ResultState.Error(R.string.server_timeout.toString())
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
                } else if (response.status == "fail"){
                    _resetResponseResult.value = ResultState.Error(response.message.toString())
                    Log.d("VerificationViewModel", "verifyResetCode: ${response.message}")
                }
            } catch (e: HttpException){
                val errorBody = e.response()?.errorBody()?.string()
                val errorMessage = Gson().fromJson(errorBody, MessageResponse::class.java).message
                _resetResponseResult.value = ResultState.Error(errorMessage.toString())
            } catch (e: SocketTimeoutException) {
                _responseResult.value = ResultState.Error(R.string.server_timeout.toString())
            }
        }
    }
}