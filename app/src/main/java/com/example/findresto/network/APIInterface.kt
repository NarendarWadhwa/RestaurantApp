package com.example.findresto.network

import com.example.findresto.network.response.BaseResponse
import com.example.findresto.network.response.RestaurantsModel
import com.example.findresto.utils.Constants
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface APIInterface {

    @Headers("user-key: " + Constants.API_KEY)
    @GET("api/v2.1/search")
    fun getRestaurantBySearchTag(
        @Query("q") city: String,
        @Query("lat") lat: Double, @Query("lon") lon: Double
    ): Single<BaseResponse<RestaurantsModel>>
}