package com.snackcheck.view.authorization.register

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.snackcheck.data.ResultState
import com.snackcheck.data.UserRepository
import com.snackcheck.data.remote.model.MessageResponse
import kotlinx.coroutines.launch
import retrofit2.HttpException

class SignUpViewModel(private val repository: UserRepository) : ViewModel() {
    private val _responseResult = MutableLiveData<ResultState<MessageResponse>>()
    val responseResult = _responseResult

    fun register(
        username: String,
        fullName: String,
        email: String,
        password: String,
        confirmPassword: String
    ) {
        viewModelScope.launch {
            try {
                _responseResult.value = ResultState.Loading
                val response = repository.register(username, fullName, email, password, confirmPassword)
                if (response.status=="success") {
                    _responseResult.value = ResultState.Success(response)
                }
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                val errorMessage = Gson().fromJson(errorBody, MessageResponse::class.java).message
                _responseResult.value = ResultState.Error(errorMessage.toString())
            }
        }
    }
}