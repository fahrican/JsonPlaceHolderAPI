package com.example.jsonplaceholderapi

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class JsonPlaceHolderApiApp : Application() {

    companion object {
        lateinit var instance: JsonPlaceHolderApiApp
    }

    init {
        instance = this
    }

}