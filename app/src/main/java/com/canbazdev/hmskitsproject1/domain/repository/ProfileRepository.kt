package com.canbazdev.hmskitsproject1.domain.repository

/*
*   Created by hamzacanbaz on 8/5/2022
*/
interface ProfileRepository {
    suspend fun getTimeOfDay(): String
}