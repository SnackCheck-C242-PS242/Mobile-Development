package com.snackcheck.view.authorization.login

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

    fun login(email: String, password: String) {
        viewModelScope.launch {
            try {
                _responseResult.value = ResultState.Loading
                val response = repository.login(email, password)
                if (response.loginResult.token.isNotEmpty()) {
                    repository.saveToken(response.loginResult.token)

                    _responseResult.value = ResultState.Success(response)
                }
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                _responseResult.value = ResultState.Error(errorBody ?: e.message())
            }
        }
    }

}