package com.snackcheck.view.authorization.reset_password

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

class ResetPasswordViewModel(private val repository: UserRepository) : ViewModel() {
    private val _responseResult = MutableLiveData<ResultState<MessageResponse>>()
    val responseResult = _responseResult

    fun getResetCode(email: String) {
        viewModelScope.launch {
            try {
                _responseResult.value = ResultState.Loading
                val response = repository.getResetCode(email)
                if (response.status == "success") {
                    _responseResult.value = ResultState.Success(response)
                }else {
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
}