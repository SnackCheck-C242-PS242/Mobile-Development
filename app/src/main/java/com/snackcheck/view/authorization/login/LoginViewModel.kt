package com.snackcheck.view.authorization.login

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.snackcheck.data.ResultState
import com.snackcheck.data.UserRepository
import com.snackcheck.data.remote.model.LoginResponse
import com.snackcheck.data.remote.model.ProfileResponse
import kotlinx.coroutines.launch
import retrofit2.HttpException

class LoginViewModel(private val repository: UserRepository) : ViewModel() {
    private val _responseResult = MutableLiveData<ResultState<LoginResponse>>()
    val responseResult = _responseResult

    private val _profileResult = MutableLiveData<ResultState<ProfileResponse>>()
    val profileResult = _profileResult

    fun login(username: String, password: String) {
        viewModelScope.launch {
            try {
                _responseResult.value = ResultState.Loading
                val response = repository.login(username, password)
                if (response.accessToken.isNotEmpty()) {
                    repository.saveToken(response.accessToken)
                    getProfile()

                    _profileResult.observeForever { result ->
                        when (result) {
                            is ResultState.Success -> {
                                val profileData = result.data.data
                                viewModelScope.launch {
                                    repository.saveUserDataPreferences(profileData)
                                    Log.d("LoginViewModel", "User Data: $profileData")
                                }
                            }
                            is ResultState.Error -> {
                                Log.d("LoginViewModel", "Error: ${result.error}")
                            }
                            else -> {}
                        }                        }
                    Log.d("LoginViewModel", "Token: ${repository.getToken()}")
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

    private fun getProfile() {
        viewModelScope.launch {
            try {
                _profileResult.value = ResultState.Loading
                val response = repository.getApiProfile()
                if (response.status == "success") {
                    _profileResult.value = ResultState.Success(response)
                    Log.d("LoginViewModel", "Profile Response: $response")
                } else {
                    _profileResult.value = ResultState.Error(response.status)
                }
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                _profileResult.value = ResultState.Error(errorBody ?: e.message())
            }
        }
    }
}