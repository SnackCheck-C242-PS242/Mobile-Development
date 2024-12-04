package com.snackcheck.data.remote.model

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("status")
    val status: String,

    @field:SerializedName("accessToken")
    val accessToken: String
)