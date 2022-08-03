package com.canbazdev.hmskitsproject1.data.model


import com.google.gson.annotations.SerializedName

data class NotificationServiceResponse(
    @SerializedName("code")
    val code: String,
    @SerializedName("msg")
    val msg: String,
    @SerializedName("requestId")
    val requestId: String
)