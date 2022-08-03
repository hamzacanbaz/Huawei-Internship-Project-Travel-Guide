package com.canbazdev.hmskitsproject1.domain.usecase.notification

import com.canbazdev.hmskitsproject1.domain.repository.NotificationRepository
import javax.inject.Inject

/*
*   Created by hamzacanbaz on 8/1/2022
*/
class SendNotificationUseCase @Inject constructor(
    private val notificationRepository: NotificationRepository
) {
    operator fun invoke(
        pushToken: String
    ) {
        notificationRepository.getAccessToken(pushToken)
    }
}