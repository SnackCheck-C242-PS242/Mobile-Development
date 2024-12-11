package com.snackcheck.view.authorization.input_new_password

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

class InputNewPasswordViewModel(private val repository: UserRepository) : ViewModel() {
    private val _responseResult = MutableLiveData<ResultState<MessageResponse>>()
    val responseResult = _responseResult

    fun resetPassword(
        email: String,
        resetCode: String,
        newPassword: String,
        confirmPassword: String,
    ) {
        viewModelScope.launch {
            try {
                _responseResult.value = ResultState.Loading
                val response =
                    repository.resetPassword(email, resetCode, newPassword, confirmPassword)
                if (response.status == "success") {
                    _responseResult.value = ResultState.Success(response)
                }
            } catch (e: Exception) {
                _responseResult.value = ResultState.Error(e.message.toString())
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