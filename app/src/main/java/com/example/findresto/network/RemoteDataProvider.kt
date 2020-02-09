package com.example.findresto.network

import com.example.findresto.network.response.RestaurantsModel
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer

interface RemoteDataProvider {

    fun getRestaurantBySearch(
        city: String, lat: Double, lon: Double,
        success: Consumer<ArrayList<RestaurantsModel>>,
        error: Consumer<Throwable>
    ): Disposable

}