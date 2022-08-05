package com.canbazdev.hmskitsproject1.data.repository

import android.content.Context
import android.util.SparseArray
import com.canbazdev.hmskitsproject1.domain.repository.ProfileRepository
import com.canbazdev.hmskitsproject1.util.Work
import com.huawei.hms.kit.awareness.Awareness
import com.huawei.hms.kit.awareness.barrier.TimeBarrier
import com.huawei.hms.kit.awareness.capture.TimeCategoriesResponse
import javax.inject.Inject

/*
*   Created by hamzacanbaz on 8/5/2022
*/
class ProfileRepositoryImpl @Inject constructor(
    private val context: Context,
) : ProfileRepository {
    override fun getTimeOfDay(): Work<String> {
        val work = Work<String>()
        val TIME_DESCRIPTION_MAP = SparseArray<String>()
        TIME_DESCRIPTION_MAP.put(TimeBarrier.TIME_CATEGORY_WEEKDAY, "Today is weekday.")
        TIME_DESCRIPTION_MAP.put(TimeBarrier.TIME_CATEGORY_WEEKEND, "Today is weekend.")
        TIME_DESCRIPTION_MAP.put(TimeBarrier.TIME_CATEGORY_HOLIDAY, "Today is holiday.")
        TIME_DESCRIPTION_MAP.put(TimeBarrier.TIME_CATEGORY_NOT_HOLIDAY, "Today is not holiday.")
        TIME_DESCRIPTION_MAP.put(TimeBarrier.TIME_CATEGORY_MORNING, "Good morning.")
        TIME_DESCRIPTION_MAP.put(TimeBarrier.TIME_CATEGORY_AFTERNOON, "Good afternoon.")
        TIME_DESCRIPTION_MAP.put(TimeBarrier.TIME_CATEGORY_EVENING, "Good evening.")
        TIME_DESCRIPTION_MAP.put(TimeBarrier.TIME_CATEGORY_NIGHT, "Good night.")

        Awareness.getCaptureClient(context).timeCategories
            // Callback listener for execution success.
            .addOnSuccessListener { timeCategoriesResponse: TimeCategoriesResponse ->
                val categories = timeCategoriesResponse.timeCategories
                val timeInfo = categories.timeCategories
                for (timeCode in timeInfo) {
                    if (TIME_DESCRIPTION_MAP.get(timeCode).contains("Good")) {
                        work.onSuccess(TIME_DESCRIPTION_MAP.get(timeCode).removeSuffix(".") + "!")
                    }
                }
            }
            .addOnFailureListener { e: Exception? ->
                work.onFailure(e ?: java.lang.Exception(e?.localizedMessage))
            }
        return work
    }
}