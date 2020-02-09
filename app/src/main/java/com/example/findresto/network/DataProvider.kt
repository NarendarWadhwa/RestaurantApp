package com.example.findresto.network

import com.example.findresto.network.response.RestaurantsModel
import com.example.findresto.utils.isNetworkAvailable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers

object DataProvider : RemoteDataProvider {

    private val mServices by lazy { APIClient.getClient().create(APIInterface::class.java) }

    private fun noInternetAvailable(error: Consumer<Throwable>) =
        error.accept(Throwable("Please Check Internet Connection"))

    private fun getDefaultDisposable(): Disposable = object : Disposable {
        override fun isDisposed() = false
        override fun dispose() {}
    }

    override fun getRestaurantBySearch(
        city: String,
        lat: Double,
        lon: Double,
        success: Consumer<ArrayList<RestaurantsModel>>,
        error: Consumer<Throwable>
    ): Disposable {
        if (isNetworkAvailable()) {
            return mServices.getRestaurantBySearchTag(city, lat, lon).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(Consumer { success.accept(it.data) }, error)
        } else {
            noInternetAvailable(error)
            return getDefaultDisposable()
        }
    }

}