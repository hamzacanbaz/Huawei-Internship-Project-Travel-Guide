package com.canbazdev.hmskitsproject1.data.model

/*
*   Created by hamzacanbaz on 8/1/2022
*/
data class AccessTokenModel(
    var access_token: String,
    var expires_in: Int,
    var token_type: String
)