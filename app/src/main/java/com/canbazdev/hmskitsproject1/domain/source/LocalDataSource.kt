package com.canbazdev.hmskitsproject1.domain.source

/*
*   Created by hamzacanbaz on 7/24/2022
*/interface LocalDataSource {
    suspend fun checkSilentSignInEnabled(): Boolean
}