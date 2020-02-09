package com.example.findresto

import android.app.Application
import com.facebook.stetho.Stetho

class RestoApp : Application() {

    companion object {
        lateinit var instance: RestoApp
            private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        Stetho.initializeWithDefaults(this)
    }
}

