package com.snackcheck.view.profile.photo

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.snackcheck.data.ResultState
import com.snackcheck.data.UserRepository
import com.snackcheck.data.remote.model.PhotoResponse
import com.snackcheck.data.remote.model.ProfileData
import kotlinx.coroutines.launch
import okhttp3.MultipartBody

class PhotoProfileViewModel(private val repository: UserRepository) : ViewModel() {
    private val _postResponseResult = MutableLiveData<ResultState<PhotoResponse>>()
    val postResponseResult: LiveData<ResultState<PhotoResponse>> = _postResponseResult

    private val _putResponseResult = MutableLiveData<ResultState<PhotoResponse>>()
    val putResponseResult: LiveData<ResultState<PhotoResponse>> = _putResponseResult

    private val _userData = MutableLiveData<ProfileData?>()
    val userData: LiveData<ProfileData?> = _userData

    var currentPhotoProfileUri: Uri? = null

    fun getProfile() {
        viewModelScope.launch {
            val data = repository.getUserDataPreferences()
            _userData.value = data
        }
    }

    fun postPhotoProfile(
        profilePhoto: MultipartBody.Part
    ) {
        viewModelScope.launch {
            _postResponseResult.value = ResultState.Loading
            val response = repository.postPhotoProfile(profilePhoto)
            if (response.status == "success") {
                _postResponseResult.value = ResultState.Success(response)
                repository.saveProfilePhotoUrl(response.profilePhotoUrl.toString())
                getProfile()
            } else {
                _postResponseResult.value = ResultState.Error(response.message.toString())
            }
        }
    }

    fun putPhotoProfile(
        profilePhoto: MultipartBody.Part
    ) {
        viewModelScope.launch {
            _putResponseResult.value = ResultState.Loading
            val response = repository.putPhotoProfile(profilePhoto)
            if (response.status == "success") {
                _putResponseResult.value = ResultState.Success(response)
                repository.saveProfilePhotoUrl(response.profilePhotoUrl.toString())
                getProfile()
            } else {
                _putResponseResult.value = ResultState.Error(response.message.toString())
            }
        }
    }
}