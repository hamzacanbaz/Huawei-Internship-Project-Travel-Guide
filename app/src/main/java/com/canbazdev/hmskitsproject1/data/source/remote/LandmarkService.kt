package com.canbazdev.hmskitsproject1.data.source.remote

import com.canbazdev.hmskitsproject1.data.model.NotificationMessageBody
import com.canbazdev.hmskitsproject1.data.model.NotificationServiceResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST


/*
*   Created by hamzacanbaz on 8/1/2022
*/

interface LandmarkService {
    @POST("v2/99536292102451188/messages:send")
    fun createNotification(
        @Header("Authorization") authorization: String?,
        @Body notificationBody: NotificationMessageBody?
    ): Call<NotificationServiceResponse>
}