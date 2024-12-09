package com.snackcheck.data.remote.model

import com.google.gson.annotations.SerializedName

data class HistoryDetailResponse (
    @field:SerializedName("status")
    val status: String,

    @field:SerializedName("data")
    val data: HistoryData
)