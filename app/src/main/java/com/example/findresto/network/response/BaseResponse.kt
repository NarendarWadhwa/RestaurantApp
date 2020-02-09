package com.example.findresto.network.response

import com.google.gson.annotations.SerializedName

data class BaseResponse<T>(
    @SerializedName("results_found") val resultFound: Int,
    @SerializedName("restaurants") val data: ArrayList<T>
)