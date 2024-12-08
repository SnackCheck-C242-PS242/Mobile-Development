package com.snackcheck.data.remote.model

import com.google.gson.annotations.SerializedName

data class ProfileResponse(
    @field:SerializedName("status")
    val status: String,

    @field:SerializedName("data")
    val data: ProfileData,
)

data class ProfileData(
    @field:SerializedName("fullName")
    val fullName: String,

    @field:SerializedName("username")
    val username: String,

    @field:SerializedName("email")
    val email: String,

    @field:SerializedName("profilePhotoUrl")
    val profilePhotoUrl: String,
)