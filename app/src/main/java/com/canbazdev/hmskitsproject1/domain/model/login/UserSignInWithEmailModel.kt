package com.canbazdev.hmskitsproject1.domain.model.login

/*
*   Created by hamzacanbaz on 7/24/2022
*/data class UserSignInWithEmailModel(
    private val email: String,
    private val password: String,
    private val verificationCode: String
)
