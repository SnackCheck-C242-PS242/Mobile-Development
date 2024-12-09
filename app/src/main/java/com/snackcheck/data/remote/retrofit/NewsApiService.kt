package com.snackcheck.data.remote.retrofit

import com.snackcheck.data.remote.model.NewsResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApiService{
    @GET("top-headlines")
    suspend fun getNews(
        @Query("q") q: String,
        @Query("category") category: String,
        @Query("language") language: String,
        @Query("apiKey") apiKey: String
    ): NewsResponse
}