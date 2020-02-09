package com.example.findresto

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.findresto.databinding.ActivityHomeBinding
import com.example.findresto.utils.Constants
import com.example.findresto.utils.LoadingDialog
import com.example.findresto.utils.RestaurantListAdapter
import com.example.findresto.utils.showMessage
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.OnCompleteListener


class HomeActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityHomeBinding
    private lateinit var mViewModel: HomeViewModel
    private val fusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_home)
        mViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        mBinding.viewModel = mViewModel
        createDialog()
        setUpData()
        getUserLocation()
    }

    private fun setUpData() {
        mBinding.rvRestaurants.layoutManager = LinearLayoutManager(this)
        val adapter = RestaurantListAdapter(this)
        mBinding.rvRestaurants.adapter = adapter
        mViewModel.getRestaurants().observe(this, Observer {
            adapter.updateList(it)
        })
    }

    private fun createDialog() {
        val dialog = LoadingDialog(this)
        mViewModel.dialogVisibility.observe(this, Observer { show ->
            dialog.apply {
                if (show) show() else hide()
            }
        })
        mViewModel.dialogMessage.observe(this, Observer {
            dialog.setMessage(it)
        })
        mViewModel.errorMessage.observe(this, Observer {
            showMessage(it)
        })
    }

    private fun getUserLocation() {
        if (checkPermission()) {
            if (isLocationEnabled()) {
                fusedLocationProviderClient.lastLocation.addOnCompleteListener(
                    OnCompleteListener {
                        it.result?.run {
                            mViewModel.latitude = latitude
                            mViewModel.longitude = longitude
                        }
                        if (it.result == null) {
                            requestLocation()
                        }
                    }
                )
            } else {
                showGPSEnableAlert()
            }
        } else {
            requestPermission()
        }
    }

    private fun showGPSEnableAlert() {
        val alertDialog =
            AlertDialog.Builder(this)
        alertDialog.setMessage("Please enable GPS in order to provide accurate results")
        alertDialog.setCancelable(false)
            .setPositiveButton("Enable GPS") { dialog, id ->
                val callGPSSettingIntent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivityForResult(callGPSSettingIntent, Constants.LOCATION_REQUEST)
            }
        alertDialog.setNegativeButton("Cancel") { dialog, id ->
            showMessage("Please enable GPS in order to show accurate results")
            dialog.cancel()
        }
        val alert = alertDialog.create()
        alert.show()

    }

    private fun isLocationEnabled(): Boolean {
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    private fun checkPermission(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            Constants.LOCATION_REQUEST
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == Constants.LOCATION_REQUEST) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getUserLocation()
            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Constants.LOCATION_REQUEST) {
            getUserLocation()
        }
    }

    private fun requestLocation() {
        val locationRequest = LocationRequest()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 5000
        locationRequest.fastestInterval = 0
        locationRequest.numUpdates = 1
        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.myLooper()
        )
    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val lastLocation = locationResult.lastLocation
            mViewModel.latitude = lastLocation.latitude
            mViewModel.longitude = lastLocation.longitude
        }
    }

}
