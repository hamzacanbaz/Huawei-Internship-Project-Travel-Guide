package com.canbazdev.hmskitsproject1.domain.model

import java.io.Serializable

/*
*   Created by hamzacanbaz on 7/21/2022
*/
data class Post(
    var id: String? = null,
    var landmarkName: String? = null,
    var landmarkLocation: String? = null,
    var landmarkInfo: String? = null,
    var landmarkImage: String? = null,
    var landmarkLatitude: Double? = null,
    var landmarkLongitude: Double? = null
):Serializable