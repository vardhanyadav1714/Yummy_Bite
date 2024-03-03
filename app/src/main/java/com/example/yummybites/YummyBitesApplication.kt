package com.example.yummybites

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class YummyBitesApplication : Application() {
    companion object {
        lateinit var instance: YummyBitesApplication
            private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}