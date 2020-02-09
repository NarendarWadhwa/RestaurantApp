package com.example.findresto

import android.app.Application
import android.text.TextUtils
import android.view.View
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.findresto.network.DataProvider
import com.example.findresto.network.response.RestaurantsModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Consumer

class HomeViewModel(context: Application) : AndroidViewModel(context) {

    val city = ObservableField<String>("")
    var latitude = 0.0
    var longitude = 0.0
    val dialogMessage = MutableLiveData<String>("")
    val errorMessage = MutableLiveData<String>()
    val noDataFound = ObservableBoolean(false)
    val dialogVisibility = MutableLiveData<Boolean>(false)
    private val disposable = CompositeDisposable()
    private val restaurantList = MutableLiveData<ArrayList<RestaurantsModel>>()

    fun getRestaurants() = restaurantList

    fun onSearch(v: View) {
        if (!TextUtils.isEmpty(city.get())) {
            searchRestaurants(city.get().toString(), latitude, longitude)
        } else
            errorMessage.value = "Please enter city name"
    }

    private fun searchRestaurants(city: String, lat: Double, lon: Double) {
        dialogMessage.value = "Loading..."
        dialogVisibility.value = true
        disposable.add(DataProvider.getRestaurantBySearch(city, lat, lon,
            Consumer {
                dialogVisibility.value = false
                if (it.size > 0) {
                    restaurantList.value = it
                    noDataFound.set(false)
                } else
                    noDataFound.set(true)
            }, Consumer {
                dialogVisibility.value = false
                errorMessage.value = it.message
            })
        )
    }

    override fun onCleared() {
        super.onCleared()
        if (!disposable.isDisposed)
            disposable.dispose()
    }
}