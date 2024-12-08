package com.snackcheck.data.remote.model

import com.google.gson.annotations.SerializedName

data class PhotoResponse(
    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("status")
    val status: String? = null,

    @field:SerializedName("url")
    val profilePhotoUrl: String? = null
)