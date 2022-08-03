package com.canbazdev.hmskitsproject1.data.repository

import com.canbazdev.hmskitsproject1.data.model.AccessTokenModel
import com.canbazdev.hmskitsproject1.data.model.NotificationMessageBody
import com.canbazdev.hmskitsproject1.data.model.NotificationServiceResponse
import com.canbazdev.hmskitsproject1.data.source.remote.AccessTokenService
import com.canbazdev.hmskitsproject1.data.source.remote.LandmarkService
import com.canbazdev.hmskitsproject1.domain.repository.NotificationRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

/*
*   Created by hamzacanbaz on 8/1/2022
*/
class NotificationRepositoryImpl @Inject constructor(
    private val landmarkService: LandmarkService,
    private val accessTokenService: AccessTokenService
) : NotificationRepository {

    override fun postNotification(
        accessToken: String,
        pushToken: String
    ) {
        println("first $accessToken $pushToken")
        val notifyMessageBody: NotificationMessageBody = NotificationMessageBody.Builder(
            "Congratulations!", "Your landmark is published",
            arrayOf(pushToken)
        ).build()
        landmarkService.createNotification("Bearer $accessToken", notifyMessageBody)
            .enqueue(object : Callback<NotificationServiceResponse> {
                override fun onResponse(
                    call: Call<NotificationServiceResponse>,
                    response: Response<NotificationServiceResponse>
                ) {
                    println("yeter be " + response.body())
                }

                override fun onFailure(call: Call<NotificationServiceResponse>, t: Throwable) {
                    println("ohhhh")
                }

            })

    }

    override fun getAccessToken(pushToken: String): String {
        println("nerelere geldik")
        var token = ""
        accessTokenService.createAccessToken(
            "client_credentials",
            "b13ab29b75c078fbf9bde5a202f0a2e6dd1c7009f3823c71ed5ee21e87426130",
            "106620513"
        ).enqueue(
            object : Callback<AccessTokenModel> {
                override fun onResponse(
                    call: Call<AccessTokenModel>,
                    response: Response<AccessTokenModel>
                ) {
                    println("of gelemedik")
                    if (response.isSuccessful) {
                        token = response.body()?.access_token ?: ""
                        postNotification(token, pushToken)
                    } else {
                        println(response)
                    }
                }

                override fun onFailure(call: Call<AccessTokenModel>, t: Throwable) {
                    println("ohhhhhhhhhhhhhhh")
                }

            }
        )
        return token
    }

}