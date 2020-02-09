package com.example.findresto.utils

import android.content.Context
import android.net.ConnectivityManager
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.databinding.BindingAdapter
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.findresto.R
import com.example.findresto.RestoApp


fun AppCompatActivity.showMessage(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun isNetworkAvailable(): Boolean {
    val cm = RestoApp.instance.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    cm.activeNetworkInfo?.run {
        val activeNetwork = cm.activeNetworkInfo
        return activeNetwork.isConnected
    }
    return false
}

@BindingAdapter("showHide")
fun showHide(view: View, showHide: Boolean) {
    view.visibility = if (showHide) View.VISIBLE else View.GONE
}

@BindingAdapter("imageUrl")
fun AppCompatImageView.setImage(url: String?) {
    val options = RequestOptions()
        .error(R.drawable.ic_placeholder)
    Glide.with(this.context)
        .setDefaultRequestOptions(options)
        .load(url)
        .into(this)
}