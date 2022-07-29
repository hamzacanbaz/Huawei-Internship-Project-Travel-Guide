package com.canbazdev.hmskitsproject1.domain.model.landmark

import com.huawei.hms.site.api.model.Site

/*
*   Created by hamzacanbaz on 7/28/2022
*/
data class NearbyLandmark(
    val name: String,
    val address: String,
    val distance: Double,
    val latitude: Double,
    val longitude: Double
)

fun Site.toNearbyLandmark(): NearbyLandmark {
    return NearbyLandmark(name, formatAddress, distance, location.lat, location.lng)
}