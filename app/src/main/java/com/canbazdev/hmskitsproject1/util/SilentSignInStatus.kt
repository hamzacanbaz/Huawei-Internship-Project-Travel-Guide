package com.canbazdev.hmskitsproject1.util

/*
*   Created by hamzacanbaz on 7/24/2022
*/
enum class SilentSignInStatus(val id: Int) {
    LOADING(0), SUCCESS(1), FAIL(-1), DISALLOWED(-2)
}