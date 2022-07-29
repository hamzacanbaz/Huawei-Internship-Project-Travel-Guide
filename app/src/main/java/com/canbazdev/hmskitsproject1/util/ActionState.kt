package com.canbazdev.hmskitsproject1.util

import com.canbazdev.hmskitsproject1.domain.model.landmark.Post

/*
*   Created by hamzacanbaz on 7/26/2022
*/
sealed class ActionState {
    object NavigateToHome : ActionState()
    object NavigateToRegister : ActionState()
    data class NavigateTDetailLandmark(val post: Post) : ActionState()
}