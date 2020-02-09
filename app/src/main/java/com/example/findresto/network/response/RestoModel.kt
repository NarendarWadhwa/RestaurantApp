package com.example.findresto.network.response

import com.google.gson.annotations.SerializedName

data class RestoModel(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("cuisines") val cuisines: String,
    @SerializedName("timings") val timings: String,
    @SerializedName("thumb") val thumb: String,
    @SerializedName("location") val location: Location,
    @SerializedName("user_rating") val rating: UserRating
)

data class Location(
    @SerializedName("locality_verbose") val locality: String
)

data class UserRating(
    @SerializedName("aggregate_rating") val rating: String
)

