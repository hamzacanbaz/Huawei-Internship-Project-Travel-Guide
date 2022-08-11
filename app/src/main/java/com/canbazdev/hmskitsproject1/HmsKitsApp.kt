package com.canbazdev.hmskitsproject1

import android.app.Application
import com.huawei.hms.ads.HwAds
import com.huawei.hms.analytics.HiAnalytics
import com.huawei.hms.analytics.HiAnalyticsTools
import com.huawei.hms.maps.MapsInitializer
import com.huawei.hms.mlsdk.common.MLApplication
import dagger.hilt.android.HiltAndroidApp

/*
*   Created by hamzacanbaz on 7/20/2022
*/
@HiltAndroidApp
class HmsKitsApp : Application() {
    override fun onCreate() {
        super.onCreate()
        MLApplication.initialize(applicationContext)
        MLApplication.getInstance().apiKey =
            "DAEDAJucCbh6nmVXWf3K0R+u79eN2LIjyT3qudeHBb6KSM4QiehF/sGVWF8don02ZWlnLc2l9nXxqZufAalXUHzLYRnZRe9Vsgaa4Q=="
        MapsInitializer.setApiKey("DAEDAJucCbh6nmVXWf3K0R+u79eN2LIjyT3qudeHBb6KSM4QiehF/sGVWF8don02ZWlnLc2l9nXxqZufAalXUHzLYRnZRe9Vsgaa4Q==")
        HwAds.init(this)

        HiAnalyticsTools.enableLog()
        val instance = HiAnalytics.getInstance(this)
        instance.setUserProfile("user", "Created")


    }
}