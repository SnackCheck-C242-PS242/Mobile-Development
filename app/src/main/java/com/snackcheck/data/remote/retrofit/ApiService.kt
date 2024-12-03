package com.snackcheck.data.remote.retrofit

import com.snackcheck.data.remote.model.LoginResponse
import com.snackcheck.data.remote.model.MessageResponse
import com.snackcheck.data.remote.model.RegisterResponse
import com.snackcheck.data.remote.model.TokenResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import retrofit2.http.PUT

interface ApiService {
    // Token API
    @FormUrlEncoded
    @POST("token")
    suspend fun getNewAccessToken(
        @Field("refreshToken") refreshToken: String
    ): TokenResponse

    // Account Verification API
    @FormUrlEncoded
    @POST("accounts")
    suspend fun verifyAccount(
        @Field("email") email: String,
        @Field("verificationCode") verificationCode: String
    ): MessageResponse

    // Register API
    @FormUrlEncoded
    @POST("register")
    suspend fun register(
        @Field("username") username: String,
        @Field("fullname") name: String,
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("confirmPassword") confirmPassword: String
    ): RegisterResponse

    // Login API
    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("username") username: String,
        @Field("password") password: String
    ): LoginResponse

    // Logout API
    @FormUrlEncoded
    @POST("logout")
    suspend fun logout(
        @Field("username") username: String
    ): MessageResponse

    // Password Reset API
    @FormUrlEncoded
    @POST("password")
    suspend fun getPassword(
        @Field("email") email: String
    ): MessageResponse

    @FormUrlEncoded
    @PUT("reset")
    suspend fun resetPassword(
        @Field("email") email: String,
        @Field("resetCode") resetCode: String,
        @Field("password") password: String,
        @Field("confirmPassword") confirmPassword: String
    ): MessageResponse

}