package com.snackcheck.data.remote.model

import com.google.gson.annotations.SerializedName

data class TokenResponse(
    @field:SerializedName("status")
    val status: String,

    @field:SerializedName("accessToken")
    val accessToken: String,
)

