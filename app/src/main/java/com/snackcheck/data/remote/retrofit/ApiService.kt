package com.snackcheck.data.remote.retrofit

import com.snackcheck.data.local.entity.SnackDetail
import com.snackcheck.data.remote.model.HistoryResponse
import com.snackcheck.data.remote.model.LoginResponse
import com.snackcheck.data.remote.model.MessageResponse
import com.snackcheck.data.remote.model.PhotoResponse
import com.snackcheck.data.remote.model.ProfileResponse
import com.snackcheck.data.remote.model.SnackPredictResponse
import com.snackcheck.data.remote.model.TokenResponse
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part

interface ApiService {
    // ** AUTHENTICATION API **
    // Token API
    @FormUrlEncoded
    @POST("auth/token")
    suspend fun getNewAccessToken(
        @Field("refreshToken") refreshToken: String
    ): TokenResponse

    // Account Verification API
    @FormUrlEncoded
    @POST("auth/accounts")
    suspend fun verifyAccount(
        @Field("email") email: String,
        @Field("verificationCode") verificationCode: String
    ): MessageResponse

    // Register API
    @FormUrlEncoded
    @POST("auth/register")
    suspend fun register(
        @Field("username") username: String,
        @Field("fullName") name: String,
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("confirmPassword") confirmPassword: String
    ): MessageResponse

    // Login API
    @FormUrlEncoded
    @POST("auth/login")
    suspend fun login(
        @Field("username") username: String,
        @Field("password") password: String
    ): LoginResponse

    // Logout API
    @FormUrlEncoded
    @POST("auth/logout")
    suspend fun logout(
        @Field("username") username: String
    ): MessageResponse

    // Password Reset API
    @FormUrlEncoded
    @POST("auth/password")
    suspend fun getResetCode(
        @Field("email") email: String
    ): MessageResponse

    @FormUrlEncoded
    @PUT("auth/reset")
    suspend fun resetPassword(
        @Field("email") email: String,
        @Field("resetCode") resetCode: String,
        @Field("password") password: String,
        @Field("confirmPassword") confirmPassword: String
    ): MessageResponse

    // ** SNACK API **
    // Predict API
    @POST("snack/predicts")
    suspend fun predictSnack(
        @Body snackDetail: SnackDetail
    ): SnackPredictResponse

    // History API
    @GET("snack/histories")
    suspend fun getHistory(): HistoryResponse

    // ** PROFILE API **
    // Profile Info API
    @GET("profile")
    suspend fun getProfile(): ProfileResponse

    // Post Profile Photo API
    @Multipart
    @POST("profile/photo")
    suspend fun postPhoto(
        @Part profilePhoto: MultipartBody.Part
    ): PhotoResponse

    // Put Profile Photo API
    @Multipart
    @POST("profile/photo")
    suspend fun putPhoto(
        @Part profilePhoto: MultipartBody.Part
    ): PhotoResponse
}