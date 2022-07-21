package com.canbazdev.hmskitsproject1

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/*
*   Created by hamzacanbaz on 7/20/2022
*/
@HiltAndroidApp
class HmsKitsApp : Application() {
    override fun onCreate() {
        super.onCreate()
        /*CloudDbWrapper.initialize(applicationContext) {
            Log.i("Application", it.toString())
        }*/
    }
}