package com.canbazdev.hmskitsproject1.domain.model.login

import java.io.Serializable

/*
*   Created by hamzacanbaz on 7/29/2022
*/
data class UserFirebase(
    var id: String = "",
    var email: String = "",
    var name: String = ""
) : Serializable