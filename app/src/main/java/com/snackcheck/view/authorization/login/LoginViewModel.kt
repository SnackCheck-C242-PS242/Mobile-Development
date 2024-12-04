package com.snackcheck.view.authorization.login

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.snackcheck.data.ResultState
import com.snackcheck.data.UserRepository
import com.snackcheck.data.remote.model.LoginResponse
import kotlinx.coroutines.launch
import retrofit2.HttpException

class LoginViewModel(private val repository: UserRepository) : ViewModel() {
    private val _responseResult = MutableLiveData<ResultState<LoginResponse>>()
    val responseResult = _responseResult

    fun login(username: String, password: String) {
        viewModelScope.launch {
            try {
                _responseResult.value = ResultState.Loading
                val response = repository.login(username, password)
                if (response.accessToken.isNotEmpty()) {
                    repository.saveToken(response.accessToken)
                    repository.saveUsername(username)
                    Log.d("LoginViewModel", "Token: ${response.accessToken}, Username: $username")
                    _responseResult.value = ResultState.Success(response)
                } else {
                    _responseResult.value = ResultState.Error(response.message)
                }
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                _responseResult.value = ResultState.Error(errorBody ?: e.message())
            }
        }
    }

}