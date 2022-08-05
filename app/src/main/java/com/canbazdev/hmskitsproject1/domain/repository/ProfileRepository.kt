package com.canbazdev.hmskitsproject1.domain.repository

import com.canbazdev.hmskitsproject1.util.Work

/*
*   Created by hamzacanbaz on 8/5/2022
*/
interface ProfileRepository {
    fun getTimeOfDay(): Work<String>
}