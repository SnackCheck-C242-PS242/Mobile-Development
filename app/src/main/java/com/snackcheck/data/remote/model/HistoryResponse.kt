package com.snackcheck.data.remote.model

import com.google.gson.annotations.SerializedName

data class HistoryResponse(
    @field:SerializedName("status")
    val status: String,

    @field:SerializedName("data")
    val data: List<HistoryData>,
)

data class HistoryData(
    @field:SerializedName("snackId")
    val snackId: String,

    @field:SerializedName("snackName")
    val snackName: String,

    @field:SerializedName("nutritions")
    val nutritions: Nutritions,

    @field:SerializedName("health_status")
    val healthStatus: String,

    @field:SerializedName("recommendation")
    val recommendation: String,

    @field:SerializedName("createdAt")
    val createdAt: String,
)