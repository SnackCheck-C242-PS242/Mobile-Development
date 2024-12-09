package com.snackcheck.view.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.snackcheck.BuildConfig
import com.snackcheck.data.ResultState
import com.snackcheck.data.UserRepository
import com.snackcheck.data.remote.model.ArticlesItem
import com.snackcheck.data.remote.model.HistoryData
import com.snackcheck.data.remote.model.ProfileData
import com.snackcheck.data.remote.retrofit.ApiConfig
import kotlinx.coroutines.launch
import retrofit2.HttpException

class HomeViewModel(private val repository: UserRepository) : ViewModel() {
    private val _historyList = MutableLiveData<Result<List<HistoryData>?>>()
    val historyList : LiveData<Result<List<HistoryData>?>> = _historyList

    private val _userData = MutableLiveData<ProfileData?>()
    val userData: LiveData<ProfileData?> = _userData

    private val _newsResponse = MutableLiveData<ResultState<List<ArticlesItem>>>()
    val newsResponse = _newsResponse

    fun getNews(language: String) {
        viewModelScope.launch {
            try {
                _newsResponse.value = ResultState.Loading
                val response =
                    ApiConfig.getNewsApiService().getNews("food", "health", language, BuildConfig.API_KEY)
                if (response.status == "ok") {
                    val filteredArticles = response.articles.filter { article ->
                        article.title != "[Removed]" && article.description != "[Removed]" && article.content != "[Removed]"
                    }
                    _newsResponse.value = ResultState.Success(filteredArticles)
                }
            } catch (e: HttpException) {
                _newsResponse.value = ResultState.Error(e.message())
            }
        }
    }

    fun getHistory() {
        viewModelScope.launch {
            _historyList.value = repository.getHistory()
        }
    }

    fun getProfile() {
        viewModelScope.launch {
            val data = repository.getUserDataPreferences()
            _userData.value = data
        }
    }
}