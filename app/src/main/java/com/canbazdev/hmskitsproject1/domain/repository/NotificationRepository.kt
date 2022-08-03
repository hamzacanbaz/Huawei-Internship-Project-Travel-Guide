package com.canbazdev.hmskitsproject1.domain.repository

/*
*   Created by hamzacanbaz on 8/1/2022
*/interface NotificationRepository {
    fun postNotification(accessToken: String, pushToken: String)
    fun getAccessToken(pushToken: String): String
}