package com.canbazdev.hmskitsproject1.data.source.remote

import com.canbazdev.hmskitsproject1.data.model.AccessTokenModel
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Headers
import retrofit2.http.POST

/*
*   Created by hamzacanbaz on 8/1/2022
*/
interface AccessTokenService {

    @FormUrlEncoded
    @Headers("Content-Type:application/x-www-form-urlencoded; charset=UTF-8")
    @POST("oauth2/v3/token")
    fun createAccessToken(
        @Field("grant_type") grant_type: String,
        @Field("client_secret") client_secret: String,
        @Field("client_id") client_id: String
    ): Call<AccessTokenModel>


}