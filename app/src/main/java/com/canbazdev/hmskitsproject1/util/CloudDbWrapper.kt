package com.canbazdev.hmskitsproject1.util

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.canbazdev.hmskitsproject1.data.model.ObjectTypeInfoHelper
import com.canbazdev.hmskitsproject1.data.model.PostInfo
import com.canbazdev.hmskitsproject1.util.Constants.ID
import com.huawei.agconnect.AGCRoutePolicy
import com.huawei.agconnect.AGConnectInstance
import com.huawei.agconnect.AGConnectOptionsBuilder
import com.huawei.agconnect.auth.AGConnectAuth
import com.huawei.agconnect.cloud.database.AGConnectCloudDB
import com.huawei.agconnect.cloud.database.CloudDBZone
import com.huawei.agconnect.cloud.database.CloudDBZoneConfig
import com.huawei.agconnect.cloud.database.CloudDBZoneQuery

/*
*   Created by hamzacanbaz on 7/20/2022
*/
class CloudDbWrapper {
    companion object {

        private const val TAG = "CloudDbWrapper"

        var cloudDB: AGConnectCloudDB? = null
        private var config: CloudDBZoneConfig? = null
        var cloudDBZone: CloudDBZone? = null
        var instance: AGConnectInstance? = null

        fun initialize(
            context: Context,
            cloudDbInitializeResponse: (Boolean) -> Unit
        ) {
            AGConnectCloudDB.initialize(context)

            if (cloudDBZone != null) {
                cloudDbInitializeResponse(true)
                return
            }


            val agcConnectOptions =
                AGConnectOptionsBuilder().setRoutePolicy(AGCRoutePolicy.GERMANY).build(context);
            instance = AGConnectInstance.buildInstance(agcConnectOptions);


            try {
                cloudDB = AGConnectCloudDB.getInstance(
                    instance,
                    AGConnectAuth.getInstance(instance)
                )

            } catch (e: RuntimeException) {
                Toast.makeText(context, e.localizedMessage, Toast.LENGTH_SHORT).show()
                println(e.localizedMessage)
            }

            cloudDB?.createObjectType(ObjectTypeInfoHelper.getObjectTypeInfo())

            config = CloudDBZoneConfig(
                Constants.CloudDbZoneName,
                CloudDBZoneConfig.CloudDBZoneSyncProperty.CLOUDDBZONE_CLOUD_CACHE,
                CloudDBZoneConfig.CloudDBZoneAccessProperty.CLOUDDBZONE_PUBLIC
            )

            config!!.persistenceEnabled = true
            val task = cloudDB?.openCloudDBZone2(config!!, true)
            task?.addOnSuccessListener {
                println("task success")
                Log.i(TAG, "Open cloudDBZone success")
                cloudDBZone = it
                cloudDbInitializeResponse(true)
            }?.addOnFailureListener {
                println("task fail")
                Log.w(TAG, "Open cloudDBZone failed for " + it.message)
                cloudDbInitializeResponse(false)
            }
        }

        fun checkMeetingId(meetingId: String, resultListener: ResultListener) {
            if (cloudDBZone == null)
                Log.d(TAG, "Cloud DB Zone is null, try re-open it")

            val query = CloudDBZoneQuery.where(PostInfo::class.java).equalTo(ID, meetingId)
            val queryTask = cloudDBZone!!.executeQuery(
                query,
                CloudDBZoneQuery.CloudDBZoneQueryPolicy.POLICY_QUERY_DEFAULT
            )

            queryTask.addOnSuccessListener {
                if (it.snapshotObjects.size() > 0)
                    resultListener.onSuccess(arrayListOf(it.snapshotObjects.get(0)))
                else
                    resultListener.onFailure(Exception("noElements"))
            }.addOnFailureListener {
                Log.e(TAG, "Query User is failed ${it.message}")
                resultListener.onFailure(it)
            }
        }

        fun closeCloudDBZone() {
            try {
                cloudDB?.closeCloudDBZone(cloudDBZone)
                Log.w("CloudDB zone close", "Cloud was closed")
            } catch (e: Exception) {
                Log.w("CloudDBZone", e)
            }
        }
    }

    interface ResultListener {
        fun onSuccess(result: Any?)
        fun onFailure(e: Exception)
    }

}