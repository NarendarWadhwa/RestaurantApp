package com.example.findresto.network.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class RestaurantsModel(
    @Expose @SerializedName("restaurant") val restoModel: RestoModel
)

